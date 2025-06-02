
package gui;

import java.awt.*;
import constants.*;
import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors; // Per il filtraggio

import builder.Libro; // Assicurati che l'importazione sia corretta per la classe Libro
import builder.Stato; // Assicurati che l'importazione sia corretta per l'enum Stato
import observer.Observer;
import service.GestoreLibreria;

// Per ora, non implementiamo LibreriaObserver e LibreriaManager
// observer.LibreriaObserver;
// service.LibreriaManager;


public class HomeForm extends Form implements Observer {

    private JPanel mainContentPanel; // Pannello principale che conterrà le categorie scrollabili
    //riferimento a gestione libreria
    private GestoreLibreria gestoreLibreria;

    public HomeForm(String titolo) throws SQLException {
        super(titolo);
        //istanza di gestione
        this.gestoreLibreria = GestoreLibreria.getInstance();
        //home form sarà un osservatore
        //quindi richiamo il metodo attach
        this.gestoreLibreria.attach(this);
        getContentPane().setLayout(new BorderLayout());
        addGuiComponent();

        // questo va ad utilizzare i dati reali
        refreshLibriCategories();
    }

    private void addGuiComponent(){

        // --- Pannello superiore: Titolo e Barra di Ricerca ---
        JPanel superiore = new JPanel(new BorderLayout(10, 0));
        superiore.setBackground(Common_constants.colore_primario);
        superiore.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));

        JLabel titoloLabel = new JLabel("Private Library");
        titoloLabel.setForeground(Common_constants.colore_font_titoli);
        titoloLabel.setFont(new Font("Dialog", Font.BOLD, 40));
        superiore.add(titoloLabel, BorderLayout.WEST);

        JTextField ricerca = new JTextField(20);
        ricerca.setPreferredSize(new Dimension(300, 35));
        ricerca.setBackground(Common_constants.colore_bottoni);
        ricerca.setForeground(Common_constants.colore_font_titoli);
        ricerca.setFont(new Font("Dialog", Font.BOLD, 15));
        ricerca.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(ricerca);
        JButton searchButton = new JButton("🔍");
        searchButton.setPreferredSize(new Dimension(40, 35));
        searchButton.setBackground(Common_constants.colore_bottoni);
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
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
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


        //todo controlla
        aggiungi.addActionListener(e -> {
            // Apre AggiungiLibroForm
            AggiungiLibroForm addBookForm = new AggiungiLibroForm("Aggiungi un nuovo libro");
            addBookForm.setModal(true); // Rendi la finestra modale (blocca la HomeForm finché non chiusa)
            addBookForm.setVisible(true);

        });

        //todo questi ancora vanno visti
        filtra.addActionListener(e -> JOptionPane.showMessageDialog(this, "Azione Filtra (TEST)"));
        ordina.addActionListener(e -> JOptionPane.showMessageDialog(this, "Azione Ordina (TEST)"));
    }

    /*
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



        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

     */

    @Override
    public void update() throws SQLException {
        System.out.println("HomeForm: Notifica di cambiamento ricevuta. Aggiorno la visualizzazione dei libri.");
        refreshLibriCategories(); // Richiama il metodo per aggiornare la GUI con i dati reali
    }

    // Metodo per ricaricare i libri dalla fonte reale (LibreriaManager)
    private void refreshLibriCategories() throws SQLException {
        mainContentPanel.removeAll(); // Rimuovi tutti i componenti esistenti

        List<Libro> allLibri = gestoreLibreria.restituisciLibri(); // <-- Ottiene i dati reali dal Manager

        if (allLibri.isEmpty()) {
            mainContentPanel.add(new JLabel("Nessun libro presente nella libreria. Aggiungine uno!"));
        } else {
            Map<String, List<Libro>> libriPerGenere = allLibri.stream()
                    .collect(Collectors.groupingBy(Libro::getGenere_appartenenza));

            // Per ogni genere, crea un pannello categoria e aggiungi i libri
            libriPerGenere.forEach((genere, libriDiQuestoGenere) -> {
                JLabel categoryLabel = new JLabel("Genere: " + genere);
                categoryLabel.setForeground(Common_constants.colore_bottoni);
                categoryLabel.setFont(new Font("Arial", Font.BOLD, 20));
                categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                categoryLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
                mainContentPanel.add(categoryLabel);

                JPanel categoryRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0)); // FlowLayout per la riga
                categoryRowPanel.setOpaque(false);

                for (Libro libro : libriDiQuestoGenere) {
                    categoryRowPanel.add(new PanelLibro(libro)); // Assicurati che PanelLibro sia aggiornato
                }

                JScrollPane rowScrollPane = new JScrollPane(categoryRowPanel);
                rowScrollPane.setOpaque(false);
                rowScrollPane.getViewport().setOpaque(false);
                rowScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                rowScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                rowScrollPane.setBorder(BorderFactory.createEmptyBorder());

                int rowScrollHeight = PanelLibro.getAltezza() + 20; // Altezza del PanelLibro + un po' di spazio
                rowScrollPane.setPreferredSize(new Dimension(getWidth() - 200, rowScrollHeight)); // Adatta la larghezza
                rowScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowScrollHeight));

                mainContentPanel.add(rowScrollPane);
                mainContentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            });
        }
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
        titleLabel.setForeground(Common_constants.colore_bottoni);
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