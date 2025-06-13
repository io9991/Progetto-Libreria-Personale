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


    private MyJavaDBC(){

        initializeConnection();
        createTableIfNotExist();

    }

    //inizializza la connessione
    private void initializeConnection(){

        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
            //carica il driver H2
            Class.forName("org.h2.Driver");

            connection = DriverManager.getConnection(
                    Common_constants.DB_URL,
                    Common_constants.DB_USERNAME,
                    Common_constants.DB_PASSWORD
            );
            System.out.println("Connessione al database H2 '" + Common_constants.DB_URL + "' stabilita con successo.");


        }catch(SQLException | ClassNotFoundException e){
            //tengo conto di quello che accade con dei messaggi nel terminale
            System.err.println("Errore durante la connessione al database H2: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Impossibile connettersi al database H2.", e);
        }
    }


    /*
        crea la tabella dei libri se questa non esite, andando a rispettare
        i vincoli imposti per cui la chiave primaria è il codice, e poi titolo,
        autore, codice e stat sono not null
     */
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


    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            System.err.println("Connessione al database persa o chiusa. Tentativo di ristabilire la connessione...");
            initializeConnection(); // Prova a ristabilire
        }
        return connection;
    }


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
