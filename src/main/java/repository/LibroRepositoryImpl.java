package repository;

import builder.Libro;
import builder.Stato;
import constants.Common_constants;
import db.MyJavaDBC;

import java.security.PublicKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//implemento i metodi con delle query sql



public class LibroRepositoryImpl implements LibroRepository {

    public LibroRepositoryImpl() {
    }

    private Connection getConnection() throws SQLException {
        return MyJavaDBC.getInstance().getConnection();
    }

    @Override
    public void inserisciLibro(Libro libro) throws SQLException {
        // Nomi delle colonne usati come da te: (titolo, autore, codISBN, genere, valutazione, stat)
        String sql = "INSERT INTO " + Common_constants.DB_LIBRI_TABLE_NAME +
                " (titolo, autore, codISBN, genere, valutazione, stat)" + // Nomi colonne come da te
                " VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connessione = getConnection();
             PreparedStatement inserisciLibroStmt = connessione.prepareStatement(sql)) {

            inserisciLibroStmt.setString(1, libro.getTitolo());
            inserisciLibroStmt.setString(2, libro.getAutore());
            inserisciLibroStmt.setString(3, libro.getCodice_ISBN()); // Assumo che codISBN nel DB sia l'ISBN del Libro
            inserisciLibroStmt.setString(4, libro.getGenere_appartenenza());
            inserisciLibroStmt.setInt(5, libro.getValutazione());
            inserisciLibroStmt.setString(6, libro.getStato().name()); // Assumo che 'stat' nel DB salvi il nome dell'enum

            int righeAffette = inserisciLibroStmt.executeUpdate(); // <-- Esecuzione mancante, ORA AGGIUNTA
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

    @Override
    public void modificaLibro(Libro libro) throws SQLException {
        // Nomi delle colonne e ordine dei parametri come da te
        String sql = "UPDATE " + Common_constants.DB_LIBRI_TABLE_NAME +
                " SET titolo = ?, autore = ?, genere = ?, valutazione = ?, stat = ? WHERE codISBN = ?"; // <-- Nomi colonne e spazio corretto

        try (Connection connessione = getConnection();
             PreparedStatement modificaStmt = connessione.prepareStatement(sql)) {

            modificaStmt.setString(1, libro.getTitolo());
            modificaStmt.setString(2, libro.getAutore());
            modificaStmt.setString(3, libro.getGenere_appartenenza()); // Corrisponde a 'genere'
            modificaStmt.setInt(4, libro.getValutazione()); // Corrisponde a 'valutazione'
            modificaStmt.setString(5, libro.getStato().name()); // Corrisponde a 'stat'
            modificaStmt.setString(6, libro.getCodice_ISBN()); // Corrisponde a 'codISBN' nella WHERE clause

            int righeAffette = modificaStmt.executeUpdate(); // <-- Esecuzione mancante, ORA AGGIUNTA
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
        // Nomi delle colonne come da te
        String sql = "SELECT titolo, autore, codISBN, genere, valutazione, stat FROM " + Common_constants.DB_LIBRI_TABLE_NAME +
                " WHERE codISBN = ?"; // <-- Spazi e nome colonna corretti

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
                            .setStato(Stato.valueOf(ris.getString("stat"))) // <-- CORREZIONE CRITICA: usa colonna "stat"
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
    public List<Libro> tuttiLibri() throws SQLException { // <-- Implementazione completa e throws SQLException
        List<Libro> libri = new ArrayList<>();
        // Nomi delle colonne come da te
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
                        .setStato(Stato.valueOf(rs.getString("stat"))) // <-- Usa colonna "stat"
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
}