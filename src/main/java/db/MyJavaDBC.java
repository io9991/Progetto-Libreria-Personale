package db;

//ho deciso di implementare questa classe usando il dp SINGLETON

//importo quello che mi serve
import java.sql.*;
import java.util.List;

import builder.Libro;
import constants.Common_constants;
import constants.Common_constants.*;

public class MyJavaDBC {

    private static MyJavaDBC instance = null;
    private Connection connection;

    //costruttore vuoto
    private MyJavaDBC(){

        try{
            this.connection = DriverManager.getConnection(Common_constants.DB_URL, Common_constants.DB_USERNAME, Common_constants.DB_PASSWORD);
            System.out.println("Connessione al database stabilita con successo.");
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
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

            try {
                // Tentativo di riconnessione
                connection = DriverManager.getConnection(Common_constants.DB_URL, Common_constants.DB_USERNAME, Common_constants.DB_PASSWORD);
                System.out.println("Riconnessione al database riuscita.");
            } catch (SQLException e) {
                System.err.println("Fallimento nella riconnessione al database: " + e.getMessage());
                throw e; // Rilancia l'eccezione originale o una nuova
            }
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
