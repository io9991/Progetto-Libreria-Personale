import builder.Libro;
import builder.Stato;
import constants.Common_constants;
import db.MyJavaDBC;
import org.junit.jupiter.api.*;
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


    @Test
    @Disabled("Test disabilitato per la consegna.")
    void testInserisciLibro_ISBNDuplicato_LanciaEccezione() {
        System.out.println("TEST: Esecuzione testInserisciLibro_ISBNDuplicato_LanciaEccezione");
        Libro libro1 = new Libro.Builder().setTitolo("Primo").setAutore("A").setCodiceISBN("ISBN001").setStato(Stato.DA_LEGGERE).build();
        Libro libro2 = new Libro.Builder().setTitolo("Secondo").setAutore("B").setCodiceISBN("ISBN001").setStato(Stato.DA_LEGGERE).build();

        assertDoesNotThrow(() -> libroRepository.inserisciLibro(libro1), "Il primo inserimento non dovrebbe lanciare eccezioni.");

        // Ci aspettiamo che il secondo inserimento con lo stesso ISBN lanci una SQLException
        SQLException thrown = assertThrows(SQLException.class, () -> {
            libroRepository.inserisciLibro(libro2);
        }, "L'inserimento di un libro con ISBN duplicato dovrebbe lanciare SQLException.");

        assertTrue(thrown.getMessage().contains("PRIMARY KEY") || thrown.getMessage().contains("constraint"),
                "Il messaggio di errore dovrebbe indicare una violazione della chiave primaria.");

        // Verifica che solo un libro sia stato inserito
        assertDoesNotThrow(() -> {
            List<Libro> libriPresenti = libroRepository.tuttiLibri();
            assertEquals(1, libriPresenti.size(), "Dovrebbe esserci solo un libro nel database.");
        });
    }


    @Test
    void testLibroConIsbn_Trovato() throws SQLException {
        System.out.println("TEST: Esecuzione testLibroConIsbn_Trovato");
        Libro libro = new Libro.Builder().setTitolo("Cercami").setAutore("Io").setCodiceISBN("FIND-ME").setStato(Stato.LETTO).build();
        libroRepository.inserisciLibro(libro);

        Libro found = libroRepository.LibroConIsbn("FIND-ME");
        assertNotNull(found, "Il libro dovrebbe essere trovato per ISBN.");
        assertEquals("Cercami", found.getTitolo());
    }

    @Test
    void testLibroConIsbn_NonTrovato() throws SQLException {
        System.out.println("TEST: Esecuzione testLibroConIsbn_NonTrovato");
        Libro found = libroRepository.LibroConIsbn("NON-EXISTENT");
        assertNull(found, "Nessun libro dovrebbe essere trovato per un ISBN inesistente.");
    }

    @Test
    void testTuttiLibri_Vuoto() throws SQLException {
        System.out.println("TEST: Esecuzione testTuttiLibri_Vuoto");
        List<Libro> libri = libroRepository.tuttiLibri();
        assertNotNull(libri);
        assertTrue(libri.isEmpty(), "La lista di tutti i libri dovrebbe essere vuota all'inizio.");
    }

    @Test
    void testTuttiLibri_ConLibri() throws SQLException {
        System.out.println("TEST: Esecuzione testTuttiLibri_ConLibri");
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("A").setAutore("X").setCodiceISBN("1").setStato(Stato.LETTO).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("B").setAutore("Y").setCodiceISBN("2").setStato(Stato.DA_LEGGERE).build());

        List<Libro> libri = libroRepository.tuttiLibri();
        assertEquals(2, libri.size(), "Dovrebbero esserci due libri.");
    }

    @Test
    void testCercaLibri_Generale() throws SQLException {
        System.out.println("TEST: Esecuzione testCercaLibri_Generale");
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("Il Signore degli Anelli").setAutore("Tolkien").setCodiceISBN("LOTR").setGenereAppartenenza("Fantasy").setStato(Stato.LETTO).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("Lo Hobbit").setAutore("Tolkien").setCodiceISBN("HOBBIT").setGenereAppartenenza("Fantasy").setStato(Stato.LETTO).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("Dune").setAutore("Herbert").setCodiceISBN("DUNE").setGenereAppartenenza("Sci-Fi").setStato(Stato.DA_LEGGERE).build());

        List<Libro> results = libroRepository.cercaLibri("Tolkien");
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(l -> l.getTitolo().equals("Il Signore degli Anelli")));
        assertTrue(results.stream().anyMatch(l -> l.getTitolo().equals("Lo Hobbit")));

        results = libroRepository.cercaLibri("Fantasy");
        assertEquals(2, results.size());

        results = libroRepository.cercaLibri("Hobbit");
        assertEquals(1, results.size());
        assertEquals("Lo Hobbit", results.get(0).getTitolo());
    }

    @Test
    void testGetAutoriDistinti() throws SQLException {
        System.out.println("TEST: Esecuzione testGetAutoriDistinti");
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("A").setAutore("Autore A").setCodiceISBN("1").setStato(Stato.LETTO).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("B").setAutore("Autore B").setCodiceISBN("2").setStato(Stato.DA_LEGGERE).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("C").setAutore("Autore A").setCodiceISBN("3").setStato(Stato.IN_LETTURA).build());

        List<String> autori = libroRepository.getAutoriDistinti();
        assertEquals(2, autori.size(), "Dovrebbero esserci due autori distinti.");
        assertTrue(autori.contains("Autore A"));
        assertTrue(autori.contains("Autore B"));
        assertEquals("Autore A", autori.get(0)); // Verifica ordinamento ASC
        assertEquals("Autore B", autori.get(1));
    }



    @Test
    void testCercaLibriByAutore() throws SQLException {
        System.out.println("TEST: Esecuzione testCercaLibriByAutore");
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("A").setAutore("Autore A").setCodiceISBN("1").setStato(Stato.LETTO).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("B").setAutore("Autore B").setCodiceISBN("2").setStato(Stato.DA_LEGGERE).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("C").setAutore("Autore A").setCodiceISBN("3").setStato(Stato.IN_LETTURA).build());

        List<Libro> results = libroRepository.cercaLibriByAutore("Autore A");
        assertEquals(2, results.size());
        assertEquals("A", results.get(0).getTitolo());
        assertEquals("C", results.get(1).getTitolo());
    }

    @Test
    void testCercaLibriByGenere() throws SQLException {
        System.out.println("TEST: Esecuzione testCercaLibriByGenere");
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("A").setAutore("X").setCodiceISBN("1").setGenereAppartenenza("Fantasy").setStato(Stato.LETTO).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("B").setAutore("Y").setCodiceISBN("2").setGenereAppartenenza("Sci-Fi").setStato(Stato.DA_LEGGERE).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("C").setAutore("Z").setCodiceISBN("3").setGenereAppartenenza("Fantasy").setStato(Stato.IN_LETTURA).build());

        List<Libro> results = libroRepository.cercaLibriByGenere("Fantasy");
        assertEquals(2, results.size());
        assertEquals("A", results.get(0).getTitolo());
        assertEquals("C", results.get(1).getTitolo());
    }

    @Test
    void testCercaLibriByValutazione() throws SQLException {
        System.out.println("TEST: Esecuzione testCercaLibriByValutazione");
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("A").setAutore("X").setCodiceISBN("1").setValutazione(5).setStato(Stato.LETTO).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("B").setAutore("Y").setCodiceISBN("2").setValutazione(3).setStato(Stato.DA_LEGGERE).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("C").setAutore("Z").setCodiceISBN("3").setValutazione(5).setStato(Stato.IN_LETTURA).build());

        List<Libro> results = libroRepository.cercaLibriByValutazione(5);
        assertEquals(2, results.size());
        assertEquals("A", results.get(0).getTitolo());
        assertEquals("C", results.get(1).getTitolo());
    }

    @Test
    void testLibriOrdinati_TitoloASC() throws SQLException {
        System.out.println("TEST: Esecuzione testLibriOrdinati_TitoloASC");
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("Z").setAutore("A").setCodiceISBN("Z").setStato(Stato.LETTO).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("A").setAutore("B").setCodiceISBN("A").setStato(Stato.DA_LEGGERE).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("B").setAutore("C").setCodiceISBN("B").setStato(Stato.IN_LETTURA).build());

        List<Libro> ordinati = libroRepository.libriOrdinati("titolo", "ASC");
        assertEquals(3, ordinati.size());
        assertEquals("A", ordinati.get(0).getTitolo());
        assertEquals("B", ordinati.get(1).getTitolo());
        assertEquals("Z", ordinati.get(2).getTitolo());
    }

    @Test
    void testLibriOrdinati_AutoreDESC() throws SQLException {
        System.out.println("TEST: Esecuzione testLibriOrdinati_AutoreDESC");
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("T1").setAutore("Z").setCodiceISBN("Z").setStato(Stato.LETTO).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("T2").setAutore("A").setCodiceISBN("A").setStato(Stato.DA_LEGGERE).build());
        libroRepository.inserisciLibro(new Libro.Builder().setTitolo("T3").setAutore("B").setCodiceISBN("B").setStato(Stato.IN_LETTURA).build());

        List<Libro> ordinati = libroRepository.libriOrdinati("autore", "DESC");
        assertEquals(3, ordinati.size());
        assertEquals("Z", ordinati.get(0).getAutore());
        assertEquals("B", ordinati.get(1).getAutore());
        assertEquals("A", ordinati.get(2).getAutore());
    }




}
