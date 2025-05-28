/*
package gui;

import java.awt.*;
//importo il package relativo alle costanti che ho creato in precedenza
import constants.*;
//importo la libreria che mi permetterà di poter utilizzare il pacchetto grafico
import javax.swing.*;

public class HomeForm extends Form{

    public HomeForm(String titolo) {
        super("Library");
        //richiamo la funzione per l'aggiunta dei componenti grafici
        //per quanto riguarda il layout mi organizzo tutto dividendomi in zone
        getContentPane().setLayout(new BorderLayout());
        addGuiComponent();
    }

    private void addGuiComponent(){

        //nella parte superiore della schermata voglio che ci sia il titolo e la barra di ricerca
        JPanel superiore = new JPanel(new BorderLayout(10, 0));
        superiore.setBackground(Common_constants.colore_primario);
        superiore.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
        //il titolo lo metto in una label
        JLabel titolo = new JLabel("Personal Library");
        //camnio colore del font e tipologia
        titolo.setForeground(Common_constants.colore_font_titoli);
        titolo.setFont(new Font("Dialog", Font.BOLD, 30));
        //alto a sinistra
        superiore.add(titolo, BorderLayout.WEST);
        //aggiungo la barra di ricerca
        JTextField ricerca = new JTextField(20);
        ricerca.setPreferredSize(new Dimension(300, 35));
        ricerca.setBackground(Common_constants.colore_secondario);
        ricerca.setForeground(Common_constants.colore_font_titoli);
        ricerca.setFont(new Font("Dialog", Font.PLAIN, 15));

        ricerca.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

        superiore.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        superiore.add(ricerca, BorderLayout.EAST);

        getContentPane().add(superiore, BorderLayout.NORTH);


        //passiamo alla parte centrale dove mettiamo la griglia per i libri
        JPanel griglia = new JPanel();
        griglia.setLayout(new GridLayout(0,2,20,20));
        griglia.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        griglia.setBackground(Common_constants.colore_primario);
        //metto la barra per scorrere
        JScrollPane barraScorrimento = new JScrollPane(griglia);
        //quando serve appare
        barraScorrimento.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        barraScorrimento.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        barraScorrimento.setBorder(BorderFactory.createEmptyBorder());
        griglia.add(new PanelLibro("Titolo", "Autore", "codice", "builder.Stato", 4));

        add(barraScorrimento, BorderLayout.CENTER);

        JScrollPane centrale = new JScrollPane(griglia);
      //  centrale.add(barraScorrimento, BorderLayout.CENTER);
        getContentPane().add(centrale, BorderLayout.CENTER);

        //pannello a destra con bottone per aggiungere
        //filtrare e ordinare

        JPanel latoOperazioni = new JPanel();
        latoOperazioni.setLayout(new BoxLayout(latoOperazioni, BoxLayout.Y_AXIS));
        latoOperazioni.setBorder(BorderFactory.createEmptyBorder(20,10,20,20));
        latoOperazioni.setBackground(Common_constants.colore_primario);

        //bottone add, filtro e sort
        JButton aggiungi = bottonePersonalizzato("Aggiungi");
        JButton filtra = bottonePersonalizzato("Filtra");
        JButton ordina = bottonePersonalizzato("Ordina");

        latoOperazioni.add(Box.createVerticalGlue());
        latoOperazioni.add(aggiungi);
        latoOperazioni.add(Box.createRigidArea(new Dimension(0,15)));
        latoOperazioni.add(filtra);
        latoOperazioni.add(Box.createRigidArea(new Dimension(0,15)));
        latoOperazioni.add(ordina);
        latoOperazioni.add(Box.createVerticalGlue());

        getContentPane().add(latoOperazioni, BorderLayout.EAST);

    }

    //metodo che serve per creare un bottone personalizzato
    private JButton bottonePersonalizzato(String testo){
        JButton bottone = new JButton(testo);
        bottone.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottone.setBackground(Common_constants.colore_secondario);
        bottone.setForeground(Common_constants.colore_font_titoli);
        bottone.setFont(new Font("Arial", Font.BOLD, 14));
        bottone.setPreferredSize(new Dimension(100, 40));
        bottone.setMaximumSize(new Dimension(150,40));
        bottone.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Common_constants.colore_secondario, 1),
                BorderFactory.createEmptyBorder(5,15,5,15)
        ));
        bottone.setFocusPainted(false);
        return bottone;
    }

}






 */

package gui;

import java.awt.*;
import constants.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Per il filtraggio

import builder.Libro; // Assicurati che l'importazione sia corretta per la classe Libro
import builder.Stato; // Assicurati che l'importazione sia corretta per l'enum Stato

// Per ora, non implementiamo LibreriaObserver e LibreriaManager
// observer.LibreriaObserver;
// service.LibreriaManager;


public class HomeForm extends Form {

    private JPanel mainContentPanel; // Pannello principale che conterrà le categorie scrollabili

    public HomeForm(String titolo) {
        super(titolo);
        getContentPane().setLayout(new BorderLayout());
        addGuiComponent();

        // Popola la GUI con dati di prova divisi per categoria
        refreshLibriCategoriesMock();
    }

    private void addGuiComponent(){

        // --- Pannello superiore: Titolo e Barra di Ricerca ---
        JPanel superiore = new JPanel(new BorderLayout(10, 0));
        superiore.setBackground(Common_constants.colore_primario);
        superiore.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));

        JLabel titoloLabel = new JLabel("Private Library");
        titoloLabel.setForeground(Common_constants.colore_font_titoli);
        titoloLabel.setFont(new Font("Dialog", Font.BOLD, 30));
        superiore.add(titoloLabel, BorderLayout.WEST);

        JTextField ricerca = new JTextField(20);
        ricerca.setPreferredSize(new Dimension(300, 35));
        ricerca.setBackground(Common_constants.colore_secondario);
        ricerca.setForeground(Common_constants.colore_font_titoli);
        ricerca.setFont(new Font("Dialog", Font.PLAIN, 15));
        ricerca.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(ricerca);
        JButton searchButton = new JButton("🔍");
        searchButton.setPreferredSize(new Dimension(40, 35));
        searchButton.setBackground(Common_constants.colore_secondario);
        searchButton.setForeground(Common_constants.colore_font_titoli);
        searchButton.setFont(new Font("Dialog", Font.BOLD, 18));
        searchButton.setBorder(BorderFactory.createLineBorder(Common_constants.colore_secondario, 1));
        searchButton.setFocusPainted(false);
        searchPanel.add(searchButton);

        superiore.add(searchPanel, BorderLayout.EAST);
        getContentPane().add(superiore, BorderLayout.NORTH);


        // --- Pannello centrale contenitore per tutte le categorie (con scroll verticale) ---
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS)); // Impila le categorie verticalmente
        mainContentPanel.setBackground(Common_constants.colore_primario);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding generale

        JScrollPane mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setOpaque(false);
        mainScrollPane.getViewport().setOpaque(false);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Niente scroll orizzontale su questo scroll principale
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder()); // Rimuovi bordo predefinito
        getContentPane().add(mainScrollPane, BorderLayout.CENTER);


        // --- Pannello a destra con bottoni ---
        JPanel latoOperazioni = new JPanel();
        latoOperazioni.setLayout(new BoxLayout(latoOperazioni, BoxLayout.Y_AXIS));
        latoOperazioni.setBorder(BorderFactory.createEmptyBorder(20,10,20,20));
        latoOperazioni.setBackground(Common_constants.colore_primario);

        JButton aggiungi = bottonePersonalizzato("Aggiungi");
        JButton filtra = bottonePersonalizzato("Filtra");
        JButton ordina = bottonePersonalizzato("Ordina");

        latoOperazioni.add(Box.createVerticalGlue());
        latoOperazioni.add(aggiungi);
        latoOperazioni.add(Box.createRigidArea(new Dimension(0,15)));
        latoOperazioni.add(filtra);
        latoOperazioni.add(Box.createRigidArea(new Dimension(0,15)));
        latoOperazioni.add(ordina);
        latoOperazioni.add(Box.createVerticalGlue());

        getContentPane().add(latoOperazioni, BorderLayout.EAST);

        // Listener per il pulsante "Aggiungi" (aggiunge un libro mock e ricarica le categorie)
        aggiungi.addActionListener(e -> {
            Libro newLibro = new Libro.Builder()
                    .setTitolo("Libro Aggiunto " + (int)(Math.random() * 100))
                    .setAutore("Autore Casuale")
                    .setCodiceISBN("NEW-" + (int)(Math.random() * 10000))
                    .setGenereAppartenenza("Nuovo Genere")
                    .setValutazione((int)(Math.random() * 5) + 1)
                    .setStato(Stato.DA_LEGGERE) // Per test, aggiungi a una categoria specifica
                    .build();
            // Per ora, aggiungiamo il libro a una lista "fittizia" e poi ricarichiamo tutto
            // Quando ci sarà LibreriaManager, questo aggiungerà al DB e triggererà l'Observer.
            // Per il test visuale, aggiungiamo alla lista temporanea e ricarichiamo
            mockAllLibri.add(newLibro); // Aggiunge alla lista globale mock
            refreshLibriCategoriesMock(); // Ricostruisce tutte le categorie
            System.out.println("Aggiunto libro mock: " + newLibro.getTitolo());
        });

        filtra.addActionListener(e -> JOptionPane.showMessageDialog(this, "Azione Filtra (TEST)"));
        ordina.addActionListener(e -> JOptionPane.showMessageDialog(this, "Azione Ordina (TEST)"));
    }

    // Lista "mock" globale per simulare il database/LibreriaManager
    private List<Libro> mockAllLibri = new ArrayList<>();

    // Metodo per popolare la GUI con dati di prova divisi per categoria
    private void refreshLibriCategoriesMock() {
        mainContentPanel.removeAll(); // Rimuovi tutte le categorie esistenti

        // 1. Popola la lista mockAllLibri se è vuota (solo alla prima esecuzione)
        if (mockAllLibri.isEmpty()) {
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("Il Signore degli Anelli: La Compagnia dell'Anello")
                    .setAutore("J.R.R. Tolkien")
                    .setCodiceISBN("978-0123456789")
                    .setGenereAppartenenza("Fantasy")
                    .setValutazione(5)
                    .setStato(Stato.LETTO)
                    .build());
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("1984")
                    .setAutore("George Orwell")
                    .setCodiceISBN("978-9876543210")
                    .setGenereAppartenenza("Distopia")
                    .setValutazione(4)
                    .setStato(Stato.DA_LEGGERE)
                    .build());
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("Il Piccolo Principe")
                    .setAutore("Antoine de Saint-Exupéry")
                    .setCodiceISBN("978-1122334455")
                    .setGenereAppartenenza("Fiaba")
                    .setValutazione(5)
                    .setStato(Stato.IN_LETTURA)
                    .build());
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("Orgoglio e Pregiudizio")
                    .setAutore("Jane Austen")
                    .setCodiceISBN("978-5566778899")
                    .setGenereAppartenenza("Romanzo Rosa")
                    .setValutazione(4)
                    .setStato(Stato.LETTO)
                    .build());
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("Dune")
                    .setAutore("Frank Herbert")
                    .setCodiceISBN("978-9988776655")
                    .setGenereAppartenenza("Fantascienza")
                    .setValutazione(5)
                    .setStato(Stato.DA_LEGGERE)
                    .build());
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("Moby Dick")
                    .setAutore("Herman Melville")
                    .setCodiceISBN("978-1231231231")
                    .setGenereAppartenenza("Avventura")
                    .setValutazione(3)
                    .setStato(Stato.LETTO)
                    .build());
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("Cento Anni di Solitudine")
                    .setAutore("Gabriel García Márquez")
                    .setCodiceISBN("978-1029384756")
                    .setGenereAppartenenza("Realismo Magico")
                    .setValutazione(5)
                    .setStato(Stato.LETTO)
                    .build());
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("Guida Galattica per Autostoppisti")
                    .setAutore("Douglas Adams")
                    .setCodiceISBN("978-2938475610")
                    .setGenereAppartenenza("Fantascienza Umoristica")
                    .setValutazione(4)
                    .setStato(Stato.IN_LETTURA)
                    .build());
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("Il Nome della Rosa")
                    .setAutore("Umberto Eco")
                    .setCodiceISBN("978-3847561029")
                    .setGenereAppartenenza("Giallo Storico")
                    .setValutazione(5)
                    .setStato(Stato.LETTO)
                    .build());
            // Aggiungi altri libri per testare lo scorrimento orizzontale
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("Un Altro Libro Lungo Titolo")
                    .setAutore("Autore Esteso")
                    .setCodiceISBN("978-LONGAUTHOR")
                    .setGenereAppartenenza("Fantascienza")
                    .setValutazione(3)
                    .setStato(Stato.DA_LEGGERE)
                    .build());
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("Ancora Uno Lungo Titolo")
                    .setAutore("Ultimo Autore")
                    .setCodiceISBN("978-ULTIMOLIB")
                    .setGenereAppartenenza("Fantasy")
                    .setValutazione(2)
                    .setStato(Stato.IN_LETTURA)
                    .build());
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("Libro #13")
                    .setAutore("Autore 13")
                    .setCodiceISBN("978-1313131313")
                    .setGenereAppartenenza("Mistero")
                    .setValutazione(4)
                    .setStato(Stato.LETTO)
                    .build());
            mockAllLibri.add(new Libro.Builder()
                    .setTitolo("Libro #14")
                    .setAutore("Autore 14")
                    .setCodiceISBN("978-1414141414")
                    .setGenereAppartenenza("Avventura")
                    .setValutazione(5)
                    .setStato(Stato.DA_LEGGERE)
                    .build());
        }


        // 2. Filtra i libri per stato
        // (Aggiungi un metodo helper per ottenere il nome formattato dallo Stato enum)
        String tuttiLabel = "Tutti i Libri";
        String inLetturaLabel = "In Lettura";
        String daLeggereLabel = "Da Leggere";
        String lettiLabel = "Letti";
        String abbandonatiLabel = "Abbandonati"; // Se usi questo stato

        // Aggiungi la categoria "Tutti i Libri"
        mainContentPanel.add(createCategoryPanel(tuttiLabel, mockAllLibri));

        // Aggiungi le altre categorie filtrate
        mainContentPanel.add(createCategoryPanel(inLetturaLabel,
                mockAllLibri.stream()
                        .filter(libro -> libro.getStato() == Stato.IN_LETTURA)
                        .collect(Collectors.toList())
        ));
        mainContentPanel.add(createCategoryPanel(daLeggereLabel,
                mockAllLibri.stream()
                        .filter(libro -> libro.getStato() == Stato.DA_LEGGERE)
                        .collect(Collectors.toList())
        ));
        mainContentPanel.add(createCategoryPanel(lettiLabel,
                mockAllLibri.stream()
                        .filter(libro -> libro.getStato() == Stato.LETTO)
                        .collect(Collectors.toList())
        ));
        // Se hai lo stato ABBANDONATO, puoi aggiungerlo qui:
        mainContentPanel.add(createCategoryPanel(abbandonatiLabel,
                mockAllLibri.stream()
                        .filter(libro -> libro.getStato() == Stato.DA_LEGGERE)
                        .collect(Collectors.toList())
        ));


        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    /**
     * Crea un pannello per una singola categoria di libri con un titolo
     * e una riga di libri scrollabile orizzontalmente.
     * @param categoryTitle Il titolo della categoria (es. "Letti").
     * @param libriPerCategoria La lista di oggetti Libro da visualizzare in questa categoria.
     * @return Il JPanel rappresentante la categoria.
     */
    private JPanel createCategoryPanel(String categoryTitle, List<Libro> libriPerCategoria) {
        JPanel categoryContainer = new JPanel();
        // BoxLayout per impilare il titolo della categoria e il suo JScrollPane orizzontale
        categoryContainer.setLayout(new BoxLayout(categoryContainer, BoxLayout.Y_AXIS));
        categoryContainer.setOpaque(false); // Trasparente per mostrare lo sfondo del pannello genitore
        categoryContainer.setAlignmentX(Component.LEFT_ALIGNMENT); // Allinea l'intera categoria a sinistra

        // Titolo della categoria
        JLabel titleLabel = new JLabel(categoryTitle);
        titleLabel.setForeground(Common_constants.colore_font_titoli);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Allinea il titolo a sinistra
        categoryContainer.add(titleLabel);
        categoryContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Spazio sotto il titolo

        // Pannello che conterrà i PanelLibro, disposti orizzontalmente
        JPanel bookRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0)); // FlowLayout: allinea a sinistra, 20px spazio orizzontale tra libri, 0px verticale
        bookRowPanel.setOpaque(false);
        // Importantissimo: bookRowPanel non deve avere una preferredSize fissa.
        // Deve "espandersi" orizzontalmente in base al numero di libri.
        // FlowLayout gestisce questo bene: la sua preferredSize.width sarà la somma delle preferredSize.width dei suoi figli.

        for (Libro libro : libriPerCategoria) {
            bookRowPanel.add(new PanelLibro(libro));
        }

        // Scroll Pane specifico per questa riga di libri
        JScrollPane rowScrollPane = new JScrollPane(bookRowPanel);
        rowScrollPane.setOpaque(false);
        rowScrollPane.getViewport().setOpaque(false);
        rowScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); // Niente scroll verticale per la riga
        rowScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Scroll orizzontale per la riga
        rowScrollPane.setBorder(BorderFactory.createEmptyBorder()); // Rimuovi bordo predefinito dello scrollbar della riga

        // Imposta le dimensioni preferite e massime del JScrollPane della riga.
        // L'altezza è fissa (altezza del PanelLibro + padding per scrollbar se appare).
        // La larghezza preferita dovrebbe essere la larghezza della finestra,
        // ma la massima larghezza deve essere Integer.MAX_VALUE per permettere
        // al bookRowPanel di estendersi e al rowScrollPane di mostrarlo.
        int rowScrollHeight = PanelLibro.getAltezza() + 20; // Altezza del PanelLibro + un po' di spazio per la scrollbar se serve

        // Imposta le dimensioni preferite e massime per il JScrollPane della riga.
        // La larghezza sarà dinamica in base al viewport, ma l'altezza deve essere fissa.
        // Questo fa sì che il JScrollPane della riga abbia un'altezza fissa, ma si espanda in larghezza.
        // Quando bookRowPanel supera la larghezza visibile, la scrollbar orizzontale appare.
        rowScrollPane.setPreferredSize(new Dimension(800, rowScrollHeight)); // Iniziale larghezza di riferimento
        rowScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowScrollHeight)); // Consente espansione orizzontale infinita

        categoryContainer.add(rowScrollPane);
        categoryContainer.add(Box.createRigidArea(new Dimension(0, 30))); // Spazio tra le categorie
        return categoryContainer;
    }

    // Metodo per creare bottoni personalizzati
    private JButton bottonePersonalizzato(String testo){
        JButton bottone = new JButton(testo);
        bottone.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottone.setBackground(Common_constants.colore_secondario);
        bottone.setForeground(Common_constants.colore_font_titoli);
        bottone.setFont(new Font("Arial", Font.BOLD, 14));
        bottone.setPreferredSize(new Dimension(100, 40));
        bottone.setMaximumSize(new Dimension(150,40));
        bottone.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Common_constants.colore_secondario, 1),
                BorderFactory.createEmptyBorder(5,15,5,15)
        ));
        bottone.setFocusPainted(false);
        return bottone;
    }
}