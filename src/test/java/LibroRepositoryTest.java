import builder.Libro;
import builder.Stato;
import constants.Common_constants;
import db.MyJavaDBC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.LibroRepository;
import repository.LibroRepositoryImpl;
import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class LibroRepositoryTest {

    private LibroRepository libroRepository;
    private Connection testConnection;
    // Salva l'URL originale del DB principale per ripristinarlo dopo la suite di test
    private static String originalDbUrl;

    public LibroRepositoryTest(){
    }


    @BeforeEach
    void setUp() throws Exception {

        if (originalDbUrl == null) {
            originalDbUrl = Common_constants.DB_URL;
            System.out.println("DEBUG: Salvato URL DB originale: " + originalDbUrl);
        }

        MyJavaDBC.getInstance().closeConnection();
        System.out.println("DEBUG: Connessione MyJavaDBC chiuso per setup test");

        //per testare il singleton che gestisce connessioni globali
        java.lang.reflect.Field instanceField = MyJavaDBC.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        Common_constants.DB_URL = "jdbc:h2:mem:testdb_" + UUID.randomUUID().toString() + ";MODE=MySQL;DB_CLOSE_DELAY=-1";

        testConnection = MyJavaDBC.getInstance().getConnection();
        System.out.println("DEBUG: Connessione di test H2 in-memory stabilita.");

        try (Statement stmt = testConnection.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS " + Common_constants.DB_LIBRI_TABLE_NAME);
            System.out.println("DEBUG: Tabella '" + Common_constants.DB_LIBRI_TABLE_NAME + "' droppata (se esisteva).");
            String createTableSQL = "CREATE TABLE " + Common_constants.DB_LIBRI_TABLE_NAME + " (" +
                    "codISBN VARCHAR(255) PRIMARY KEY NOT NULL," +
                    "titolo VARCHAR(255) NOT NULL," +
                    "autore VARCHAR(255) NOT NULL," +
                    "genere VARCHAR(255)," +
                    "valutazione INT," +
                    "stat VARCHAR(50) NOT NULL" +
                    ");";
            stmt.executeUpdate(createTableSQL);
            System.out.println("DEBUG: Tabella '" + Common_constants.DB_LIBRI_TABLE_NAME + "' ricreata in DB H2 in-memory.");
        } catch (SQLException e) {
            System.err.println("ERRORE: Errore durante la pulizia/creazione del database di test H2: " + e.getMessage());
            e.printStackTrace();
            throw e; // Rilancia per fallire il setup del test se la pulizia/ricreazione non riesce
        }

        libroRepository = new LibroRepositoryImpl();
        System.out.println("DEBUG: LibroRepositoryImpl inizializzato per i test.");

    }


    //dopo ogniTest
    @AfterEach
    void tearDown() throws SQLException{

        try(Statement stmt = testConnection.createStatement()) {
            stmt.executeUpdate("DELETE FROM " + Common_constants.DB_LIBRI_TABLE_NAME);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    /*
        metodi di TEST, qui verranno testati i metodi di:
        -inserimento corretto e errato
        -cancellazione
        -modifica corretta e errata
        -filtro
        -ordine
        -ricerca
     */

    @Test
    void testInserisciLibro_buono() throws SQLException{
        Libro libro = new Libro.Builder()
                .setTitolo("Origin")
                .setAutore("DanBrown")
                .setCodiceISBN("21312sdf")
                .setGenereAppartenenza("Thriller")
                .setValutazione(5)
                .setStato(Stato.LETTO)
                .build();

        libroRepository.inserisciLibro(libro);

        //verifichiamo
        //ci aspettiamo che ci sia il libro appena inserito
        List<Libro> libriPresenti = libroRepository.tuttiLibri();
        assertNotNull(libriPresenti);
        assertEquals(1, libriPresenti.size());
        assertEquals("Origin", libriPresenti.get(0).getTitolo(), "il titolo non è corretto");
        assertEquals("DanBrown",libriPresenti.get(0).getAutore());
        assertEquals("21312sdf",libriPresenti.get(0).getCodice_ISBN());
        assertEquals("Thriller", libriPresenti.get(0).getGenere_appartenenza());
        assertEquals(5,libriPresenti.get(0).getValutazione());
        assertEquals(Stato.LETTO,libriPresenti.get(0).getStato());
    }

    //todo test per lanciare errore

    //test per verificare che funzioni la cancellazione
    @Test
    void testRimuoviLibro() throws SQLException{
        Libro libro = new Libro.Builder()
                .setTitolo("DaEliminare")
                .setAutore("Elimina")
                .setCodiceISBN("21312sdf")
                .setGenereAppartenenza("Thriller")
                .setValutazione(5)
                .setStato(Stato.LETTO)
                .build();
        //inserisco il libro e poi lo elimino
        libroRepository.inserisciLibro(libro);
        //elimino e poi controllo
        libroRepository.eliminaLibro(libro);
        List<Libro> libriPresenti = libroRepository.tuttiLibri();
        //verifico che sia vuoto
        assertTrue(libriPresenti.isEmpty());
    }


    //test per la modifica
    @Test
    void modificaLibro_buono() throws SQLException{

        System.out.println("TEST: Esecuzione testModificaLibro_Successo");
        Libro libroOriginale = new Libro.Builder()
                .setTitolo("Il Vecchio e il Mare")
                .setAutore("Ernest Hemingway")
                .setCodiceISBN("OLD-BOOK-123")
                .setGenereAppartenenza("Narrativa")
                .setValutazione(4)
                .setStato(Stato.DA_LEGGERE)
                .build();
        libroRepository.inserisciLibro(libroOriginale);
        Libro libroModificato = new Libro.Builder()
                .setTitolo("Il Vecchio e il Mare (Edizione Aggiornata)") // Modifica titolo
                .setAutore("Ernest Hemingway")
                .setCodiceISBN("OLD-BOOK-123") // Stesso ISBN per identificare
                .setGenereAppartenenza("Classico") // Modifica genere
                .setValutazione(5) // Modifica valutazione
                .setStato(Stato.LETTO) // Modifica stato
                .build();

        libroRepository.modificaLibro(libroModificato);

        Libro libroRecuperatoDopoModifica = libroRepository.LibroConIsbn("OLD-BOOK-123");

        assertNotNull(libroRecuperatoDopoModifica, "Il libro modificato dovrebbe essere recuperabile.");
        assertEquals("Il Vecchio e il Mare (Edizione Aggiornata)", libroRecuperatoDopoModifica.getTitolo(), "Il titolo non è stato aggiornato.");
        assertEquals("Classico", libroRecuperatoDopoModifica.getGenere_appartenenza(), "Il genere non è stato aggiornato.");
        assertEquals(5, libroRecuperatoDopoModifica.getValutazione(), "La valutazione non è stata aggiornata.");
        assertEquals(Stato.LETTO, libroRecuperatoDopoModifica.getStato(), "Lo stato non è stato aggiornato.");
        assertEquals(1, libroRepository.tuttiLibri().size(), "Dovrebbe esserci ancora solo un libro nel DB.");
    }





}
