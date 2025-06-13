package repository;

import builder.Libro;
import builder.Stato;
import constants.Common_constants;
import db.MyJavaDBC;

import java.security.PublicKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/*
    implementazione dell'interfaccia LibroRepository, dove definisco i metodi
    facendo uso del linguaggio SQL, e quindi delle query che mi permetternno
    di manipolare al meglio il db.
 */


public class LibroRepositoryImpl implements LibroRepository {

    public LibroRepositoryImpl() {
    }

    //mi connetto al singleton
    private Connection getConnection() throws SQLException {
        return MyJavaDBC.getInstance().getConnection();
    }


    //prende il libro da inserire
    @Override
    public void inserisciLibro(Libro libro) throws SQLException {

        String sql = "INSERT INTO " + Common_constants.DB_LIBRI_TABLE_NAME +
                " (titolo, autore, codISBN, genere, valutazione, stat)" + // Nomi colonne
                " VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connessione = getConnection();
             PreparedStatement inserisciLibroStmt = connessione.prepareStatement(sql)) {

            inserisciLibroStmt.setString(1, libro.getTitolo());
            inserisciLibroStmt.setString(2, libro.getAutore());
            inserisciLibroStmt.setString(3, libro.getCodice_ISBN());
            inserisciLibroStmt.setString(4, libro.getGenere_appartenenza());
            inserisciLibroStmt.setInt(5, libro.getValutazione());
            inserisciLibroStmt.setString(6, libro.getStato().name());

            int righeAffette = inserisciLibroStmt.executeUpdate();
            if (righeAffette > 0) {
                System.out.println("Repository: Libro '" + libro.getTitolo() + "' inserito con successo. Righe affette: " + righeAffette);
            } else {
                System.out.println("Repository: Nessuna riga inserita per il libro '" + libro.getTitolo() + "'.");
            }

        } catch (SQLException e) {
            System.err.println("ERRORE DATABASE: Errore durante l'inserimento del libro: " + e.getMessage());
            e.printStackTrace();
            throw e; // <-- Rilancia l'eccezione
        }
    }

    //prende il libro da eliminare
    @Override
    public void eliminaLibro(Libro libro) throws SQLException {
        String sql = "DELETE FROM " + Common_constants.DB_LIBRI_TABLE_NAME +
                " WHERE codISBN = ?";

        try (Connection connessione = getConnection();
             PreparedStatement eliminaLibroStmt = connessione.prepareStatement(sql)) {

            eliminaLibroStmt.setString(1, libro.getCodice_ISBN());

            int righeCambiate = eliminaLibroStmt.executeUpdate();
            if (righeCambiate > 0) {
                System.out.println("Repository: Libro con ISBN '" + libro.getCodice_ISBN() + "' eliminato con successo. Righe cambiate: " + righeCambiate);
            } else {
                System.out.println("Repository: Nessun libro trovato o eliminato con ISBN '" + libro.getCodice_ISBN() + "'.");
            }

        } catch (SQLException e) {
            System.err.println("ERRORE DATABASE: Errore durante l'eliminazione del libro: " + e.getMessage());
            e.printStackTrace();
            throw e; // <-- Rilancia l'eccezione
        }
    }

    //prende il libro da modificare
    @Override
    public void modificaLibro(Libro libro) throws SQLException {

        String sql = "UPDATE " + Common_constants.DB_LIBRI_TABLE_NAME +
                " SET titolo = ?, autore = ?, genere = ?, valutazione = ?, stat = ? WHERE codISBN = ?";

        try (Connection connessione = getConnection();
             PreparedStatement modificaStmt = connessione.prepareStatement(sql)) {

            modificaStmt.setString(1, libro.getTitolo());
            modificaStmt.setString(2, libro.getAutore());
            modificaStmt.setString(3, libro.getGenere_appartenenza());
            modificaStmt.setInt(4, libro.getValutazione());
            modificaStmt.setString(5, libro.getStato().name());
            modificaStmt.setString(6, libro.getCodice_ISBN());

            int righeAffette = modificaStmt.executeUpdate();
            if (righeAffette > 0) {
                System.out.println("Repository: Libro '" + libro.getTitolo() + "' (ISBN: " + libro.getCodice_ISBN() + ") aggiornato con successo. Righe affette: " + righeAffette);
            } else {
                System.out.println("Repository: Nessun libro trovato o aggiornato con ISBN '" + libro.getCodice_ISBN() + "'.");
            }

        } catch (SQLException e) {
            System.err.println("ERRORE DATABASE: Errore durante la modifica del libro: " + e.getMessage());
            e.printStackTrace();
            throw e; // <-- Rilancia l'eccezione
        }
    }

    @Override
    public Libro LibroConIsbn(String isbn) throws SQLException {

        String sql = "SELECT titolo, autore, codISBN, genere, valutazione, stat FROM " + Common_constants.DB_LIBRI_TABLE_NAME +
                " WHERE codISBN = ?";

        try (Connection connessione = getConnection();
             PreparedStatement cercaLibroStmt = connessione.prepareStatement(sql)) {

            cercaLibroStmt.setString(1, isbn);

            try (ResultSet ris = cercaLibroStmt.executeQuery()) {
                if (ris.next()) {
                    return new Libro.Builder()
                            .setTitolo(ris.getString("titolo"))
                            .setAutore(ris.getString("autore"))
                            .setCodiceISBN(ris.getString("codISBN"))
                            .setGenereAppartenenza(ris.getString("genere"))
                            .setValutazione(ris.getInt("valutazione"))
                            .setStato(Stato.valueOf(ris.getString("stat")))
                            .build();
                }
            }
        } catch (SQLException e) {
            System.err.println("ERRORE DATABASE: Errore durante la ricerca del libro per ISBN: " + e.getMessage());
            e.printStackTrace();
            throw e; // <-- Rilancia l'eccezione
        }
        return null;
    }

    @Override
    public List<Libro> tuttiLibri() throws SQLException {
        List<Libro> libri = new ArrayList<>();

        String sql = "SELECT titolo, autore, codISBN, genere, valutazione, stat FROM " + Common_constants.DB_LIBRI_TABLE_NAME;

        try (Connection connessione = getConnection();
             PreparedStatement tuttiLibriStmt = connessione.prepareStatement(sql);
             ResultSet rs = tuttiLibriStmt.executeQuery()) {

            while (rs.next()) {
                Libro libro = new Libro.Builder()
                        .setTitolo(rs.getString("titolo"))
                        .setAutore(rs.getString("autore"))
                        .setCodiceISBN(rs.getString("codISBN"))
                        .setGenereAppartenenza(rs.getString("genere"))
                        .setValutazione(rs.getInt("valutazione"))
                        .setStato(Stato.valueOf(rs.getString("stat")))
                        .build();
                libri.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("ERRORE DATABASE: Errore durante il recupero di tutti i libri: " + e.getMessage());
            e.printStackTrace();
            throw e; // <-- Rilancia l'eccezione
        } catch (IllegalArgumentException e) {
            System.err.println("ERRORE MAPPING: Errore nella conversione dello stato dell'enum per un libro: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Errore di mapping dello stato del libro (valore non valido in colonna 'stat').", e);
        }
        System.out.println("DEBUG: LibroRepositoryImpl.tuttiLibri - Trovati " + libri.size() + " libri dal database.");
        return libri;
    }

    //metodo per la ricerca
    @Override
    public List<Libro> cercaLibri(String query) throws SQLException{

        List<Libro> libriTrovati = new ArrayList<>();
        String cercaTerm = "%" + query.toLowerCase() + "%";

        //query sql per la ricerca
        String sql = "SELECT * FROM " + Common_constants.DB_LIBRI_TABLE_NAME +
                " WHERE LOWER(titolo) LIKE ?" +
                " OR LOWER(autore) LIKE ?" +
                " OR LOWER(codISBN) LIKE ?" +
                " OR LOWER(genere) LIKE ?";
        //try catch
        try(Connection connessione = getConnection();
            PreparedStatement cercaStms = connessione.prepareStatement(sql)){

            cercaStms.setString(1, cercaTerm);
            cercaStms.setString(2, cercaTerm);
            cercaStms.setString(3, cercaTerm);
            cercaStms.setString(4, cercaTerm);

            try(ResultSet rs = cercaStms.executeQuery()){
                while (rs.next()){
                    libriTrovati.add(creaLibroDaResultSet(rs));
                }
            }
        }catch (SQLException e){
            System.err.println("errore nella ricerca");
            e.printStackTrace();
            throw e;
        }
        return libriTrovati;
    }

    //funzione creaLibroDaResult
    private Libro creaLibroDaResultSet(ResultSet rs) throws SQLException{
        return new Libro.Builder()
                .setTitolo(rs.getString("titolo"))
                .setAutore(rs.getString("autore"))
                .setCodiceISBN(rs.getString("codISBN"))
                .setGenereAppartenenza(rs.getString("genere"))
                .setValutazione(rs.getInt("valutazione"))
                .setStato(Stato.valueOf(rs.getString("stat")))
                .build();
    }


    @Override
    public List<String> getAutoriDistinti() throws SQLException{

        //prendo gli autori dal database
        List<String> autoriDistinti = new ArrayList<>();
        //scrivo la query
        String sql = "SELECT DISTINCT autore FROM " +
                Common_constants.DB_LIBRI_TABLE_NAME +
                " ORDER BY autore ASC";
        try (Connection connessione = getConnection();
             Statement smt = connessione.createStatement();
             ResultSet rs = smt.executeQuery(sql)){
            while(rs.next()){
                autoriDistinti.add(rs.getString("autore"));
            }

        }catch(SQLException e){
            System.out.println("ERRORE");
            e.printStackTrace();
            throw e;
        }
        return autoriDistinti;
    }


    @Override
    public List<String> getGeneriDistinti() throws SQLException{

        //prendo gli autori dal database
        List<String> generiDistinti = new ArrayList<>();
        //scrivo la query
        String sql = "SELECT DISTINCT genere FROM " +
                Common_constants.DB_LIBRI_TABLE_NAME +
                " ORDER BY genere ASC"; // *** CORREZIONE QUI: "ORDER BY" invece di "ORDERED BY" ***
        try (Connection connessione = getConnection();
             Statement smt = connessione.createStatement();
             ResultSet rs = smt.executeQuery(sql)){
            while(rs.next()){
                generiDistinti.add(rs.getString("genere"));
            }

        }catch(SQLException e){
            System.out.println("ERRORE");
            e.printStackTrace();
            throw e;
        }
        return generiDistinti;
    }

    @Override
    public List<Libro> cercaLibriByAutore(String autore) throws SQLException {
        List<Libro> libri = new ArrayList<>();
        String sql = "SELECT * FROM " + Common_constants.DB_LIBRI_TABLE_NAME + " WHERE LOWER(autore) LIKE ?";
        try (Connection connessione = getConnection();
             PreparedStatement pstmt = connessione.prepareStatement(sql)) {
            pstmt.setString(1, "%" + autore.toLowerCase() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    libri.add(creaLibroDaResultSet(rs));
                }
            }
        } catch (SQLException e) {

            System.err.println("ERRORE DATABASE: Errore durante la ricerca per autore: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return libri;
    }

    @Override
    public List<Libro> cercaLibriByGenere(String genere) throws SQLException {
        List<Libro> libri = new ArrayList<>();
        String sql = "SELECT * FROM " + Common_constants.DB_LIBRI_TABLE_NAME + " WHERE LOWER(genere) LIKE ?";
        try (Connection connessione = getConnection();
             PreparedStatement pstmt = connessione.prepareStatement(sql)) {
            pstmt.setString(1, "%" + genere.toLowerCase() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    libri.add(creaLibroDaResultSet(rs));
                }
            }
        } catch (SQLException e) {
            // È meglio rilanciare SQLException invece di RuntimeException qui
            System.err.println("ERRORE DATABASE: Errore durante la ricerca per genere: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return libri;
    }

    @Override
    public List<Libro> cercaLibriByValutazione(int valutazione) throws SQLException {
        List<Libro> libri = new ArrayList<>();

        String sql = "SELECT * FROM " + Common_constants.DB_LIBRI_TABLE_NAME + " WHERE valutazione = ?";
        try (Connection connessione = getConnection();
             PreparedStatement pstmt = connessione.prepareStatement(sql)) {
            pstmt.setInt(1, valutazione); // Imposta l'intero direttamente
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    libri.add(creaLibroDaResultSet(rs));
                }
            }
        } catch (SQLException e) {

            System.err.println("ERRORE DATABASE: Errore durante la ricerca per valutazione: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return libri;
    }

    //per l'ordinamento utilizzo statement
    @Override
    public List<Libro> libriOrdinati(String criterio, String direzione) throws SQLException {

        List<Libro> ordinati = new ArrayList<>();
        String sql = "SELECT * FROM " + Common_constants.DB_LIBRI_TABLE_NAME;

        //order by
        if(criterio != null || !criterio.isEmpty() && (direzione.equalsIgnoreCase("ASC") || direzione.equalsIgnoreCase("DESC"))){
            sql += " ORDER BY " + criterio + " " + direzione;
        }else {
            System.out.println("Parametri non validi");
        }

        try (Connection connessione = getConnection();
            Statement stmt = connessione.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()){
                ordinati.add(creaLibroDaResultSet(rs));
            }

        }catch (SQLException e){
            System.out.println("ERRORE");
            e.printStackTrace();
            throw e;
        }
        return ordinati;
    }


}