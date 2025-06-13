import builder.Libro;
import builder.Stato;
import observer.Observer;
import org.junit.jupiter.api.*;
import repository.LibroRepository;
import service.GestoreLibreria;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class GestoreLibreriaObserverTest {

    private GestoreLibreria gestoreLibreria;
    private LibroRepository mockLibroRepository; // Useremo un mock del repository per isolare il test dal DB
    private Observer mockObserver; // Useremo un mock dell'Observer per verificare le notifiche

    @BeforeEach
    void setUp() {

        mockLibroRepository = mock(LibroRepository.class);

        gestoreLibreria = GestoreLibreria.getInstance(mockLibroRepository);

        mockObserver = mock(Observer.class);

        gestoreLibreria.attach(mockObserver);

        try {
            // Quando inserisciLibro viene chiamato sul mock, non fare nulla (simula un'operazione di successo)
            doNothing().when(mockLibroRepository).inserisciLibro(any(Libro.class));
            // Fa lo stesso per eliminaLibro e modificaLibro
            doNothing().when(mockLibroRepository).eliminaLibro(any(Libro.class));
            doNothing().when(mockLibroRepository).modificaLibro(any(Libro.class));
            // Quando tuttiLibri viene chiamato, restituisce una lista vuota per default
            when(mockLibroRepository.tuttiLibri()).thenReturn(new java.util.ArrayList<>());
            when(mockLibroRepository.getAutoriDistinti()).thenReturn(new java.util.ArrayList<>());
            when(mockLibroRepository.getGeneriDistinti()).thenReturn(new java.util.ArrayList<>());
            // Puoi aggiungere altri "when" per i metodi che GestoreLibreria chiama sul repository
        } catch (SQLException e) {
            // Se c'è un errore nella configurazione del mock, il test fallisce
            fail("Errore nella configurazione del mock del LibroRepository: " + e.getMessage());
        }
    }

    /**
     * Test per verificare che l'Observer venga notificato quando un libro viene aggiunto.
     */
    @Test
    void testObserverNotifiedOnAggiungiLibro() throws SQLException {
        System.out.println("TEST: Esecuzione testObserverNotifiedOnAggiungiLibro");
        // Crea un libro di test da aggiungere
        Libro libroDaAggiungere = new Libro.Builder()
                .setTitolo("Libro Nuovo Test")
                .setAutore("Autore A")
                .setCodiceISBN("ADD-TEST-001")
                .setStato(Stato.DA_LEGGERE)
                .build();

        gestoreLibreria.aggiungiLibro(libroDaAggiungere);

        verify(mockObserver, times(1)).update();
        System.out.println("TEST: mockObserver.update() chiamato 1 volta.");
    }

    /**
     * Test per verificare che l'Observer venga notificato quando un libro viene rimosso.
     */
    @Test
    void testObserverNotifiedOnRimuoviLibro() throws SQLException {
        System.out.println("TEST: Esecuzione testObserverNotifiedOnRimuoviLibro");
        // Crea un libro di test da rimuovere
        Libro libroDaRimuovere = new Libro.Builder()
                .setTitolo("Libro da Rimuovere Test")
                .setAutore("Autore B")
                .setCodiceISBN("REM-TEST-002")
                .setStato(Stato.LETTO)
                .build();

        gestoreLibreria.rimuoviLibro(libroDaRimuovere);

        // Verifica: update() dovrebbe essere stato chiamato una volta.
        verify(mockObserver, times(1)).update();
        System.out.println("TEST: mockObserver.update() chiamato 1 volta.");
    }

    /**
     * Test per verificare che l'Observer venga notificato quando un libro viene modificato.
     */
    @Test
    void testObserverNotifiedOnAggiornaLibro() throws SQLException {
        System.out.println("TEST: Esecuzione testObserverNotifiedOnAggiornaLibro");
        // Crea un libro di test da modificare
        Libro libroDaModificare = new Libro.Builder()
                .setTitolo("Libro da Aggiornare Test")
                .setAutore("Autore C")
                .setCodiceISBN("UPD-TEST-003")
                .setStato(Stato.IN_LETTURA)
                .build();

        // Esegui l'operazione di aggiornamento sul GestoreLibreria.
        gestoreLibreria.aggiornaLibro(libroDaModificare);

        // Verifica: update() dovrebbe essere stato chiamato una volta.
        verify(mockObserver, times(1)).update();
        System.out.println("TEST: mockObserver.update() chiamato 1 volta.");
    }


    @Test
    void testObserverNotNotifiedIfOperationFails() throws SQLException {
        System.out.println("TEST: Esecuzione testObserverNotNotifiedIfOperationFails");
        // Crea un libro di test che causerà un fallimento nell'operazione del repository
        Libro libroCheFallisce = new Libro.Builder().setCodiceISBN("FAIL-004").build();

        // Configura il mockLibroRepository per lanciare una SQLException quando inserisciLibro viene chiamato.
        try {
            doThrow(new SQLException("Errore simulato nel DB")).when(mockLibroRepository).inserisciLibro(any(Libro.class));
        } catch (SQLException e) {
            fail("Errore nella configurazione del mock per test di fallimento: " + e.getMessage());
        }

        assertThrows(SQLException.class, () -> gestoreLibreria.aggiungiLibro(libroCheFallisce),
                "L'operazione dovrebbe lanciare una SQLException simulata.");

        // Verifica: update() non dovrebbe essere stato chiamato.
        verify(mockObserver, never()).update();
        System.out.println("TEST: mockObserver.update() non chiamato.");
    }

    /**
     * Test per verificare che l'Observer NON venga notificato dopo essere stato staccato (detached).
     */
    @Test
    void testObserverNotNotifiedAfterDetach() throws SQLException {
        System.out.println("TEST: Esecuzione testObserverNotNotifiedAfterDetach");
        // Crea un libro di test
        Libro libroDopoDetach = new Libro.Builder()
                .setTitolo("Libro post-detach")
                .setAutore("Autore D")
                .setCodiceISBN("DETACH-005")
                .setStato(Stato.DA_LEGGERE)
                .build();

        // Stacca l'observer mock dal GestoreLibreria.
        gestoreLibreria.detach(mockObserver);

        gestoreLibreria.aggiungiLibro(libroDopoDetach);

        // Verifica: update() non dovrebbe essere stato chiamato.
        verify(mockObserver, never()).update();
        System.out.println("TEST: mockObserver.update() non chiamato dopo detach.");
    }

    /**
     * Test per verificare che multipli Observer vengano notificati correttamente.
     */
    @Test
    void testMultipleObserversNotified() throws SQLException {
        System.out.println("TEST: Esecuzione testMultipleObserversNotified");
        Observer mockObserver2 = mock(Observer.class);
        Observer mockObserver3 = mock(Observer.class);

        gestoreLibreria.attach(mockObserver2);
        gestoreLibreria.attach(mockObserver3);

        Libro libro = new Libro.Builder().setTitolo("MultiObs").setAutore("X").setCodiceISBN("MO1").setStato(Stato.LETTO).build();
        gestoreLibreria.aggiungiLibro(libro);

        verify(mockObserver, times(1)).update();
        verify(mockObserver2, times(1)).update();
        verify(mockObserver3, times(1)).update();
        System.out.println("TEST: Tutti e 3 i mockObserver.update() chiamati 1 volta.");
    }

}
