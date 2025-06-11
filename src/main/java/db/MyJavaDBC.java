package db;



//importo quello che mi serve
import java.sql.*;
import java.util.List;

import builder.Libro;
import constants.Common_constants;
import constants.Common_constants.*;

/*
    Classe che rappresenta il database, il quale sarà al centro
    delle operazioni che verranno fatte, in particolare data
    l'esigenza che si ha di connettersi a questo db, ho deciso di implementarlo
    seguendo la struttura del design pattern SINGLETON, applicando la tecnica
    del lazy inizialization
 */

public class MyJavaDBC {

    private static MyJavaDBC instance = null;
    private Connection connection;

//    //attributi per gestire URL della connessione
//    //serve per il testing
//    private String hostCorrente;
//    private String portaCorrente;
//    private String dbNameCorrente;
//    private String usernameCorrente;
//    private String passCorrente;
////
//    //costruttore privato
//    private MyJavaDBC(){
//        try{
//            this.connection = DriverManager.getConnection(Common_constants.DB_URL, Common_constants.DB_USERNAME, Common_constants.DB_PASSWORD);
//            System.out.println("Connessione al database stabilita con successo.");
//        }catch(SQLException e){
//            e.printStackTrace();
//            System.exit(1);
//        }
//    }

    private MyJavaDBC(){
//        String urlIntero = Common_constants.DB_URL;
//        //a questo punto basta scrivere solo un url diverso
//        //che varia per l'ultima parte
//        String[] split = urlIntero.split("/");
//        String parteHost = split[2];
//        String[] portaHost = parteHost.split(":");
//
//        this.hostCorrente = portaHost[0];
//        this.portaCorrente = portaHost[1];
//        this.dbNameCorrente = split[split.length - 1];
//
//        this.usernameCorrente = Common_constants.DB_USERNAME;
//        this.passCorrente = Common_constants.DB_PASSWORD;
        initializeConnection();
        createTableIfNotExist();

    }

    private void initializeConnection(){

        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
            //carica il driver H2
            Class.forName("org.h2.Driver");
//
//            String connectionUrl = "jdbc:mysql://" + hostCorrente + ":" + portaCorrente + "/" + dbNameCorrente;
//            connection = DriverManager.getConnection(connectionUrl, usernameCorrente, passCorrente);
//            System.out.println("Connessione al database '" + connectionUrl + "' stabilita con successo.");
            connection = DriverManager.getConnection(
                    Common_constants.DB_URL,
                    Common_constants.DB_USERNAME,
                    Common_constants.DB_PASSWORD
            );
            System.out.println("Connessione al database H2 '" + Common_constants.DB_URL + "' stabilita con successo.");


        }catch(SQLException | ClassNotFoundException e){
            System.err.println("Errore durante la connessione al database H2: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Impossibile connettersi al database H2.", e);
        }
    }


    //metodo per creare la tabella LIBRI se non esiste
    private void createTableIfNotExist(){

        String creaTable = "CREATE TABLE IF NOT EXISTS " + Common_constants.DB_LIBRI_TABLE_NAME + " (" +
                            "codISBN  VARCHAR(255) PRIMARY KEY NOT NULL," +
                            "titolo VARCHAR(255) NOT NULL," +
                            "autore VARCHAR(255) NOT NULL," +
                            "genere VARCHAR(255)," +
                            "valutazione INT," +
                            "stat VARCHAR(50) NOT NULL" +
                            ");";

        try (Statement stmt = connection.createStatement()){
            stmt.executeUpdate(creaTable);
            System.out.println("Tabella '" + Common_constants.DB_LIBRI_TABLE_NAME + "' creata o già esistente.");
        }catch(SQLException e){
            System.err.println("Errore durante la creazione della tabella: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Impossibile creare la tabella 'LIBRI'.", e);
        }

    }


    public static synchronized MyJavaDBC getInstance(){
        if(instance == null){
            instance = new MyJavaDBC();
        }
        return instance;
    }

//    public Connection getConnection() throws SQLException {
//        if (connection == null || connection.isClosed()) {
//            System.err.println("Connessione al database persa o chiusa. Tentativo di ristabilire la connessione...");
//
//            try {
//                // Tentativo di riconnessione
//                connection = DriverManager.getConnection(Common_constants.DB_URL, Common_constants.DB_USERNAME, Common_constants.DB_PASSWORD);
//                System.out.println("Riconnessione al database riuscita.");
//            } catch (SQLException e) {
//                System.err.println("Fallimento nella riconnessione al database: " + e.getMessage());
//                throw e; // Rilancia l'eccezione originale o una nuova
//            }
//        }
//        return connection;
//    }
//
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            System.err.println("Connessione al database persa o chiusa. Tentativo di ristabilire la connessione...");
            initializeConnection(); // Prova a ristabilire
        }
        return connection;
    }


    //per il testing
//    public void setNameForTest(String testDbName){
//
//        if(!this.dbNameCorrente.equals(testDbName)){
//            this.dbNameCorrente = testDbName;
//            initializeConnection();
//        }
//
//    }


    //chiudiamo la connessione
    public void closeConnection(){
        if (this.connection != null){
            try{
                this.connection.close();
                this.connection = null;
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }


}
