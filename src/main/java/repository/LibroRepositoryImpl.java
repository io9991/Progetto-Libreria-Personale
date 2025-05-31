package repository;

import builder.Libro;
import builder.Stato;
import constants.Common_constants;
import db.MyJavaDBC;

import java.security.PublicKey;
import java.sql.*;
import java.util.List;

//implemento i metodi con delle query sql

public class LibroRepositoryImpl implements LibroRepository{

    public LibroRepositoryImpl(){
    }

    public Connection getConnection() throws SQLException{
        //mi "connetto" al database andando a richiamare l'istanza
        //ovviamente perchè è un singleton MySQL
        return MyJavaDBC.getInstance().getConnection();
    }


    @Override
    public void inserisciLibro(Libro libro) throws SQLException {
        try {
            Connection connessione = getConnection();
            //query per l'inserimento
            PreparedStatement inserisciLibro = connessione.prepareStatement(
                    "INSERT INTO " + Common_constants.DB_LIBRI_TABLE_NAME + "(titolo, autore, codISBN, genere, valutazione, stat)" +
                    "VALUES(?,?,?,?,?,?)");
            //inserisco i parametri nella query
            inserisciLibro.setString(1, libro.getTitolo());
            inserisciLibro.setString(2, libro.getAutore());
            inserisciLibro.setString(3, libro.getCodice_ISBN());
            inserisciLibro.setString(4, libro.getGenere_appartenenza());
            inserisciLibro.setInt(5, libro.getValutazione());
            inserisciLibro.setString(6, libro.getStato().name());

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void eliminaLibro(Libro libro) throws SQLException {
        try {
            Connection connessione = getConnection();

            PreparedStatement eliminaLibro = connessione.prepareStatement("" +
                    "DELETE FROM " + Common_constants.DB_LIBRI_TABLE_NAME +
                    "WHERE codISBN = ?");
            eliminaLibro.setString(1, libro.getCodice_ISBN());
            //controllo se effettivamente il libro era presente e quindi se ho
            //apportato cambiamenti alla struttura
            int righeCambiate = eliminaLibro.executeUpdate();
            if(righeCambiate > 0){
                //significa che ho effettivamente eliminato qualcosa
                System.out.println("libro : " + libro.getCodice_ISBN() + "eliminato");
            }else{
                System.out.println("non era presente il libro che cerchi di eliminare");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void modificaLibro(Libro libro) throws SQLException {
        try {
            Connection connessione = getConnection();
            PreparedStatement modifica = connessione.prepareStatement(
                    "UPDATE " + Common_constants.DB_LIBRI_TABLE_NAME +
                    "SET titolo = ?, autore = ?, genere = ?, valutazione = ?, stato = ? WHERE codiceISBN = ?");
            modifica.setString(1, libro.getTitolo());
            modifica.setString(2, libro.getAutore());
            modifica.setString(3, libro.getGenere_appartenenza());
            modifica.setString(4, libro.getStato().name());
            modifica.setInt(5, libro.getValutazione());
            modifica.setString(6, libro.getCodice_ISBN());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Libro LibroConIsbn(String isbn) throws SQLException {
        try {
            Connection connessione = getConnection();
            //query per l'inserimento
            PreparedStatement cercaLibro = connessione.prepareStatement(
                    "SELECT titolo, autore, codISBN, genere, valutazione, stato FROM" + Common_constants.DB_LIBRI_TABLE_NAME +
                            "WHERE codISBN = ? ");
            //inserisco i parametri nella query
            cercaLibro.setString(1, isbn);
            ResultSet ris = cercaLibro.executeQuery();
            if (ris.next()){
                return new Libro.Builder()
                        .setTitolo(ris.getString("titolo"))
                        .setAutore(ris.getString("autore"))
                        .setCodiceISBN(ris.getString("codISBN"))
                        .setGenereAppartenenza(ris.getString("genere"))
                        .setValutazione(ris.getInt("valutazione"))
                        .setStato(Stato.valueOf(ris.getString("valutazione")))
                        .build();

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Libro> tuttiLibri() {
        return List.of();
    }
}
