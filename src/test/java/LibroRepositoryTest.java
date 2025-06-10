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

    @BeforeEach
    void setUp() throws SQLException{

        //sfrutto le classi che ho aggiunto nel
        //db per il testing
        MyJavaDBC.getInstance().setNameForTest("library_test_schema");

        testConnection = MyJavaDBC.getInstance().getConnection();

        //pulisco tutto per non rischiare di avere problemi

        try(Statement stmt = testConnection.createStatement()){
            stmt.executeUpdate("DELETE FROM " + Common_constants.DB_LIBRI_TABLE_NAME);
            System.out.println("Tabella '" + Common_constants.DB_LIBRI_TABLE_NAME + "' pulizia in DB di test");

        }catch (SQLException e){
            System.out.println("Errore");
            e.printStackTrace();
            throw e;
        }

        libroRepository = new LibroRepositoryImpl();
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
    void testInserisciLibro() throws SQLException{
        Libro libro = new Libro.Builder()
                .setTitolo("Origin")
                .setAutore("Dan Brown")
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

    /*
    //Test per la modifica, creo un libro, lo inserisco e lo modifico e poi controllo
    @Test
    void modificaLibro() throws SQLException{

        //mettiamo il nostro libro
        Libro libroVecchio = new Libro.Builder()
                .setTitolo("DivinaCommedia-DaModificare")
                .setAutore("Dante")
                .setCodiceISBN("VECCHIO-21312sdf")
                .setGenereAppartenenza("Narrativa")
                .setValutazione(5)
                .setStato(Stato.DA_LEGGERE)
                .build();
        libroRepository.inserisciLibro(libroVecchio);
        //lo modifico

        Libro recupera = libroRepository.LibroConIsbn("VECCHIO-21312sdf");
        assertNotNull(recupera);
        assertEquals("DivinaCommedia-DaModificare", recupera.getTitolo());
        assertEquals(Stato.DA_LEGGERE, recupera.getStato());

        Libro libModificato = new Libro.Builder()
                .setTitolo("Divina Commedia (nuova edizione)")
                .setAutore("Dante Alighieri")
                .setCodiceISBN("VECCHIO-21312sdf")
                .setGenereAppartenenza("Narrativa")
                .setValutazione(5)
                .setStato(Stato.LETTO)
                .build();

        libroRepository.modificaLibro(libModificato);

        Libro libroDopoModifica = libroRepository.LibroConIsbn("VECCHIO-21312sdf");

        assertEquals("Divina Commedia (nuova edizione)", libroDopoModifica.getTitolo(), "il titolo non è aggiornato correttamente");


    }

     */


}
