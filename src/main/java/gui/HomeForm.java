
package gui;

import java.awt.*;
import constants.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import builder.Libro;
import builder.Stato;
import observer.Observer;
import repository.LibroRepositoryImpl;
import service.GestoreLibreria;



/*
    Questa rappresenta la pagina iniziale con cui l'utente si interfaccia
    oltre ad andare a lavorare sulla gui, qui si sfruttano le funzionalità
    del pattern OBSERVER in modo tale che ogni qual volta cambi qualcosa all'interno
    del db venga notificato il tutto e dunque avvenga facilmente una visualizzazione
    dei cambiamenti avvenuti.
 */


public class HomeForm extends Form implements Observer {

    private JPanel mainContentPanel;
    private GestoreLibreria gestoreLibreria;
    private String currentSearchTerm = "";  //campo per tenere traccia del termine di ricerca corrente
    private String currentFilterType = ""; // Tiene traccia del tipo di filtro attivo (es. "AUTORE")
    private String currentFilterValue = ""; // Tiene traccia del valore del filtro attivo (es. "Dante Alighieri")

    //variabili per l'ordinamento
    private String criterioCorrente = ""; //la colonna di ordinamento (autore, titolo, genere ... )
    private String direzioneCorrente = ""; //da a-z o da z-a

    public HomeForm(String titolo) throws SQLException {
        super(titolo);
        this.gestoreLibreria = GestoreLibreria.getInstance(new LibroRepositoryImpl());
        this.gestoreLibreria.attach(this);
        getContentPane().setLayout(new BorderLayout());
        addGuiComponent();
        refreshLibriCategories();
    }

    /*
        l'intenzione è quella di dividere la grafica in più parti, ovvero pannelli :
        SUPERIORE -> Titolo, Barra di Ricerca, Icona della casetta per ristabilire la vista
        CENTRALE ->  Visuale dei libri disposti per stato : TUTTI, DA_LEGGERE, IN LETTURA, LETTI
        DESTRA -> Controllo di Filtro e ordine tramite pulsanti (che poi saranno meno a tendina)
     */

    private void addGuiComponent() {
        // --- Pannello superiore: Titolo, Barra di Ricerca, vista iniziale ---
        JPanel superiore = new JPanel(new BorderLayout(10, 0));
        superiore.setBackground(Common_constants.colore_primario);
        superiore.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

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

        /*
         ActionListener per cercare
         */

        ricerca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(ricerca.getText());
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(ricerca);

        //bottone per tornare alla 'home'
        JButton homeButton = new JButton("⌂");
        homeButton.setPreferredSize(new Dimension(40, 35));
        homeButton.setBackground(Common_constants.colore_bottoni);
        homeButton.setForeground(Common_constants.colore_font_titoli);
        homeButton.setFont(new Font("Dialog", Font.BOLD, 18));
        //metto il cursore del bottone
        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        homeButton.setBorder(BorderFactory.createLineBorder(Common_constants.colore_secondario, 1));
        homeButton.setFocusPainted(false);
        searchPanel.add(homeButton);

        /*
         mi permette di tornare alla vista iniziale
         */

        homeButton.addActionListener(e -> {
            try {
              //  refreshLibriCategories();
                //devo andare a toglieree quelle che sono
                //le variabili di stato relativi a filtro/ordinamento e ricerca
                currentSearchTerm = "";  //campo per tenere traccia del termine di ricerca corrente
                currentFilterType = ""; // Tiene traccia del tipo di filtro attivo (es. "AUTORE")
                currentFilterValue = ""; // Tiene traccia del valore del filtro attivo (es. "Dante Alighieri")

                //variabili per l'ordinamento
                criterioCorrente = ""; //la colonna di ordinamento (autore, titolo, genere ... )
                direzioneCorrente = ""; //da a-z o da z-a

                //solo adesso dopo aver azzerato tutti i filtri posso tornare alla home
                refreshLibriCategories();

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        superiore.add(searchPanel, BorderLayout.EAST);
        getContentPane().add(superiore, BorderLayout.NORTH);


        // --- Pannello centrale contenitore per tutte le categorie (con scroll verticale) ---
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(Common_constants.colore_primario);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JScrollPane mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setOpaque(false);
        mainScrollPane.getViewport().setOpaque(false);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
        getContentPane().add(mainScrollPane, BorderLayout.CENTER);


        // --- Pannello a destra con bottoni ---
        JPanel latoOperazioni = new JPanel();
        latoOperazioni.setLayout(new BoxLayout(latoOperazioni, BoxLayout.Y_AXIS));
        latoOperazioni.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        latoOperazioni.setBackground(Common_constants.colore_primario);

        JButton aggiungi = bottonePersonalizzato("Aggiungi");
        JButton filtra = bottonePersonalizzato("Filtra");
        JButton ordina = bottonePersonalizzato("Ordina");

        latoOperazioni.add(Box.createVerticalGlue());
        latoOperazioni.add(aggiungi);
        latoOperazioni.add(Box.createRigidArea(new Dimension(0, 15)));
        latoOperazioni.add(filtra);
        latoOperazioni.add(Box.createRigidArea(new Dimension(0, 15)));
        latoOperazioni.add(ordina);
        latoOperazioni.add(Box.createVerticalGlue());

        getContentPane().add(latoOperazioni, BorderLayout.EAST);

        //mettiamo il cursore adeguato
        aggiungi.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        filtra.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ordina.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Listener per il pulsante "Aggiungi" che permette di aggiungere un libro
        aggiungi.addActionListener(e -> {
            AggiungiLibroForm addBookForm = new AggiungiLibroForm("Aggiungi un nuovo libro", null, this.gestoreLibreria);
            addBookForm.setModal(true);
            addBookForm.setVisible(true);
        });

        //bottoni per implementare il filtro e l'ordine

        filtra.addActionListener(e -> mostraPopupMenu(filtra));

        ordina.addActionListener(e -> mostraPopupMenuOrdine(ordina));

    }

    //*** FUNZIONI PER L'ORDINE ***//

    //popup menu di ordine
    private void mostraPopupMenuOrdine(Component invoker){
        JPopupMenu ordineMenu = new JPopupMenu("Ordine per :");

        //metto le opzioni possibili (stringa, criterio, direzione)
        //titolo alfabetico e inverso
        addOrdineItem(ordineMenu, "Titolo (A-Z)", "titolo", "ASC");
        addOrdineItem(ordineMenu, "Titolo (Z-A)", "titolo", "DESC");
        ordineMenu.addSeparator();
        //autore
        addOrdineItem(ordineMenu, "Autore (A-Z)", "autore", "ASC");
        addOrdineItem(ordineMenu, "Autore (Z-A)", "autore", "DESC");
        ordineMenu.addSeparator();
        //genere
        addOrdineItem(ordineMenu, "Genere (A-Z)", "genere", "ASC");
        addOrdineItem(ordineMenu, "Genere (Z-A)", "genere", "DESC");
        ordineMenu.addSeparator();
        //valutazione
        addOrdineItem(ordineMenu, "Valutazione (1-5)", "valutazione", "ASC");
        addOrdineItem(ordineMenu, "Valutazione (5-1)", "valutazione", "DESC");
        ordineMenu.addSeparator();

        JMenuItem annullaOrdineItem = new JMenuItem("Annulla Ordinamento / Ripristina Vista");
        annullaOrdineItem.addActionListener(e -> {
            try {
                // Resetta le variabili di stato dell'ordinamento
                criterioCorrente = "";
                direzioneCorrente = "";

                if (!currentFilterType.isEmpty()) {
                    // Se c'è un filtro attivo, riapplica il filtro
                    applyFilter(currentFilterType, currentFilterValue);
                } else if (!currentSearchTerm.isEmpty()) {
                    // Se c'è una ricerca attiva, riesegui la ricerca
                    performSearch(currentSearchTerm);
                } else {
                    // Altrimenti, torna alla visualizzazione "Home" predefinita
                    refreshLibriCategories();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        ordineMenu.add(annullaOrdineItem);

        ordineMenu.show(invoker, 0, invoker.getHeight());
    }

    /*
     Questo metodo permette di aggiungere un ordine, dunque segue un criterio e una direzione
        prende il menu che va a considerare
        prende il testo che appare
        prende il criterio(attributo del db)
        prende la direzione in cui vogliamo visualizzarlo(crescente, decrescente)
     */

    private void addOrdineItem(JPopupMenu menu, String testo, String criterio, String direzione){
        JMenuItem item = new JMenuItem(testo);
        item.addActionListener(e -> applyOrder(criterio, direzione));
        menu.add(item);
    }

    /*
        piccolo metodo solo per ricevere una scritta più intuitiva al posto di ASC e DESC scrivo crescente e decrescente
     */
    private String getDirezione(String direzione){
        if (direzione.equals("ASC")){
            return "Crescente";
        }else {
            return "Decrescente";
        }
    }


    /*
        permette di applicare l'ordine da noi scelto come prima detto l'ordine è caratterizzato dal
        criterio e dalla direzione, in particolare lavoro in modo da far coesistere filtro, ricerca
        e ordine, dunque questo sarà in un controllo
     */
    private void applyOrder(String criterio, String direzione){

        //settiamo il criterio e la direzione
        this.criterioCorrente = criterio;
        this.direzioneCorrente = direzione;

        try{
            //lista dei libri che vado a visualizzare
            List<Libro> libriDaVisualizzare;
            //titolo che appare
            String titoloDisplay = "Ordinamento: " + getDirezione(direzione) + " per " + criterio;

            // Se c'è un filtro attivo, recupera i libri filtrati e poi li ordina
            // Se c'è una ricerca attiva, recupera i libri cercati e poi li ordina
            // Altrimenti, recupera tutti i libri e li ordina.
            if (!currentFilterType.isEmpty()) {
                libriDaVisualizzare = gestoreLibreria.getLibriFiltrati(currentFilterType, currentFilterValue);
                libriDaVisualizzare = gestoreLibreria.getLibriOrdini(criterioCorrente, direzioneCorrente);
                libriDaVisualizzare = libriDaVisualizzare.stream()
                        .filter(libro -> {
                            try {
                                if (currentFilterType.equals("AUTORE")) return libro.getAutore().toLowerCase().contains(currentFilterValue.toLowerCase());
                                if (currentFilterType.equals("GENERE")) return libro.getGenere_appartenenza().toLowerCase().contains(currentFilterValue.toLowerCase());
                                if (currentFilterType.equals("VALUTAZIONE")) return libro.getValutazione() == Integer.parseInt(currentFilterValue);
                            } catch (Exception e) {
                                return false;
                            }
                            return true;
                        })
                        .collect(Collectors.toList());

                titoloDisplay = "Filtro: " + currentFilterType + " = " + currentFilterValue + ", Ordinamento: " + getDirezione(direzione) + " per " + criterio;

            } else if (!currentSearchTerm.isEmpty()) {
                libriDaVisualizzare = gestoreLibreria.cercaLibri(currentSearchTerm);
                libriDaVisualizzare = gestoreLibreria.getLibriOrdini(criterioCorrente, direzioneCorrente);
                libriDaVisualizzare = libriDaVisualizzare.stream()
                        .filter(libro ->
                                libro.getTitolo().toLowerCase().contains(currentSearchTerm.toLowerCase()) ||
                                        libro.getAutore().toLowerCase().contains(currentSearchTerm.toLowerCase()) ||
                                        libro.getCodice_ISBN().toLowerCase().contains(currentSearchTerm.toLowerCase()) ||
                                        libro.getGenere_appartenenza().toLowerCase().contains(currentSearchTerm.toLowerCase())
                        )
                        .collect(Collectors.toList());
                titoloDisplay = "Ricerca per \"" + currentSearchTerm + "\", Ordinamento: " + getDirezione(direzione) + " per " + criterio;

            } else {
                // Se non ci sono filtri o ricerche, ordina tutti i libri
                libriDaVisualizzare = gestoreLibreria.getLibriOrdini(criterio, direzione);
                titoloDisplay = "Ordinamento: " + getDirezione(direzione) + " per " + criterio;
            }

            displaySearchResults(libriDaVisualizzare, titoloDisplay);

        } catch (SQLException e){
            System.out.println("Errore nell'applicazione dell'ordinamento o del filtro/ricerca combinato.");
            e.printStackTrace();
            // Mostra un messaggio di errore all'utente
            JOptionPane.showMessageDialog(this,
                    "Errore durante l'applicazione dell'ordinamento: " + e.getMessage(),
                    "Errore di Ordinamento", JOptionPane.ERROR_MESSAGE);
        }
    }


    /*
        questo metodo permette di mostrare il menu dei filtri in particolare
        i criteri di filtraggio saranno : Autore, Genere, Valutazione
        data la presenza di diversi autori e generi si vanno a prendere dal database
        esistente e si mettono nel menu
     */

    private void mostraPopupMenu(Component invoker){
        JPopupMenu filterMenu = new JPopupMenu("Filtra per...");

        // Opzione per rimuovere tutti i filtri
        JMenuItem clearFilterItem = new JMenuItem("Mostra Tutti i Libri (Reset Filtro)");
        clearFilterItem.addActionListener(e -> {
            try {
                //per resettare il filtro metto filtroType e filtroValue nulli e refresh
                currentFilterType = "";
                currentFilterValue = "";
                refreshLibriCategories(); // Torna a mostrare tutti i libri
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        filterMenu.add(clearFilterItem);
        filterMenu.addSeparator(); // Linea di separazione

        // Sottomenu per Autore
        JMenu authorMenu = new JMenu("Autore");
        try {
            List<String> autori = gestoreLibreria.getAutoriDisponibili();
            if (autori.isEmpty()) {
                authorMenu.add(new JMenuItem("Nessun autore disponibile"));
                authorMenu.getItem(0).setEnabled(false); // Disabilita l'item
            } else {
                for (String autore : autori) {
                    JMenuItem item = new JMenuItem(autore);
                    item.addActionListener(e -> applyFilter("AUTORE", autore));
                    authorMenu.add(item);
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
            authorMenu.add(new JMenuItem("Errore caricamento autori"));
            authorMenu.getItem(0).setEnabled(false);
        }
        filterMenu.add(authorMenu);

        // Sottomenu per Genere
        JMenu genreMenu = new JMenu("Genere");
        try {
            List<String> generi = gestoreLibreria.getGeneriDisponibili();
            if (generi.isEmpty()) {
                genreMenu.add(new JMenuItem("Nessun genere disponibile"));
                genreMenu.getItem(0).setEnabled(false);
            } else {
                for (String genere : generi) {
                    JMenuItem item = new JMenuItem(genere);
                    item.addActionListener(e -> applyFilter("GENERE", genere));
                    genreMenu.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            genreMenu.add(new JMenuItem("Errore caricamento generi"));
            genreMenu.getItem(0).setEnabled(false);
        }
        filterMenu.add(genreMenu);

        // Sottomenu per Valutazione
        JMenu ratingMenu = new JMenu("Valutazione");
        for (int i = 5; i >= 1; i--) { // Da 5 stelle a 1 stella
            JMenuItem item = new JMenuItem(i + " Stella" + (i == 1 ? "" : "e"));
            int rating = i; // Per usarla nell'espressione lambda
            item.addActionListener(e -> applyFilter("VALUTAZIONE", String.valueOf(rating)));
            ratingMenu.add(item);
        }
        filterMenu.add(ratingMenu);

        // Mostra il popup menu nella posizione del bottone "Filtra"
        filterMenu.show(invoker, 0, invoker.getHeight());
    }

    /*
        analogalmente all'ordine, anche il filtro ha una funzione
        applica filtro che prende un tipo (Autore, Genere, Valutazione)
        e valore del tipo
     */

    private void applyFilter(String type, String value) {
        this.currentFilterType = type;
        this.currentFilterValue = value;
        this.currentSearchTerm = ""; // Resetta la ricerca quando applichi un filtro //todo lasciare la ricerca nel filtro

        try {
            List<Libro> filteredLibri = gestoreLibreria.getLibriFiltrati(type, value);
            String title = "Libri filtrati per " + type + value;
            displaySearchResults(filteredLibri, title); // Riutilizziamo displaySearchResults
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //metodo per la ricerca
    private void performSearch(String searchTerm){
        this.currentSearchTerm = searchTerm.trim(); // Salva il termine di ricerca corrente
        try {
            // Se il campo di ricerca è vuoto, mostra tutti i libri
            if (currentSearchTerm.isEmpty()) {
                refreshLibriCategories(); // Ricarica tutte le categorie
            } else {
                List<Libro> risultatiRicerca = gestoreLibreria.cercaLibri(currentSearchTerm);
                displaySearchResults(risultatiRicerca, currentSearchTerm); // Mostra solo i risultati della ricerca
            }
        } catch (SQLException e) {
            System.err.println("ERRORE GUI: Errore durante l'esecuzione della ricerca: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Errore durante la ricerca dei libri: " + e.getMessage(),
                    "Errore di Ricerca", JOptionPane.ERROR_MESSAGE);
        }
    }

        @Override
        public void update() {
                try {
                    System.out.println("HomeForm: Notifica di cambiamento ricevuta. Aggiorno la visualizzazione dei libri.");
                    if (!currentFilterType.isEmpty()) {
                        applyFilter(currentFilterType, currentFilterValue); // Riapplica il filtro corrente
                    } else if (!currentSearchTerm.isEmpty()) {
                        performSearch(currentSearchTerm); // Riesegue l'ultima ricerca
                    } else if (!criterioCorrente.isEmpty()) { // Riapplica l'ordinamento corrente
                        applyOrder(criterioCorrente, direzioneCorrente);
                    } else {
                        refreshLibriCategories(); // Altrimenti, ricarica tutto (stato "home")
                    }
                } catch (SQLException e) {
                    System.err.println("ERRORE GUI: Errore durante l'aggiornamento della HomeForm: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Errore durante l'aggiornamento della lista dei libri: " + e.getMessage(),
                            "Errore di Visualizzazione", JOptionPane.ERROR_MESSAGE);
                }
        }

    // Metodo per ricaricare i libri dalla fonte reale (GestoreLibreria)
    private void refreshLibriCategories() throws SQLException {
        mainContentPanel.removeAll(); // Rimuovi tutti i componenti esistenti

        System.out.println("HomeForm: Richiesta di tutti i libri al GestoreLibreria.");
        List<Libro> allLibri = gestoreLibreria.restituisciLibri(); // Ottiene i dati reali
        System.out.println("HomeForm: Ricevuti " + allLibri.size() + " libri dal GestoreLibreria.");


        if (allLibri.isEmpty()) {
            JLabel noBooksLabel = new JLabel("Nessun libro presente nella libreria. Aggiungine uno!");
            noBooksLabel.setForeground(Common_constants.colore_font_titoli);
            noBooksLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noBooksLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainContentPanel.add(Box.createVerticalGlue());
            mainContentPanel.add(noBooksLabel);
            mainContentPanel.add(Box.createVerticalGlue());
        } else {
            // --- Categoria "Tutti i Libri" ---
            mainContentPanel.add(createCategoryPanel("Tutti i Libri", allLibri));
            mainContentPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Spazio dopo la categoria "Tutti"


            // Raggruppa i libri per stato
            Map<String, List<Libro>> libriPerStato = allLibri.stream()
                    .collect(Collectors.groupingBy(libro -> libro.getStato().name()));

            // Ordina gli stati per un ordine specifico
            String[] statiOrdinati = {Stato.DA_LEGGERE.name(), Stato.IN_LETTURA.name(), Stato.LETTO.name()};
            List<String> chiaviOrdinate = new java.util.ArrayList<>();
            for (String statoName : statiOrdinati) {
                if (libriPerStato.containsKey(statoName)) {
                    chiaviOrdinate.add(statoName);
                }
            }
            // Aggiungi eventuali stati non previsti nell'ordine ma presenti nei dati
            libriPerStato.keySet().stream()
                    .filter(key -> !chiaviOrdinate.contains(key))
                    .sorted() // Ordina alfabeticamente gli stati rimanenti
                    .forEach(chiaviOrdinate::add);


            for (String categoria : chiaviOrdinate) {
                String categoryTitle;
                switch (Stato.valueOf(categoria)) { // Converte la stringa in enum per un confronto più robusto
                    case DA_LEGGERE:
                        categoryTitle = "Da Leggere";
                        break;
                    case IN_LETTURA:
                        categoryTitle = "In Lettura";
                        break;
                    case LETTO:
                        categoryTitle = "Letti";
                        break;
                    default:
                        categoryTitle = categoria.replace("_", " ");
                        categoryTitle = categoryTitle.substring(0, 1).toUpperCase() + categoryTitle.substring(1).toLowerCase();
                        break;
                }


                List<Libro> libriDiQuestaCategoria = libriPerStato.get(categoria);
                if (libriDiQuestaCategoria != null && !libriDiQuestaCategoria.isEmpty()) {
                    mainContentPanel.add(createCategoryPanel(categoryTitle, libriDiQuestaCategoria));
                }
            }
        }
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    //per visualizzare solo i libri che soddisfano la ricerca
    private void displaySearchResults(List<Libro> risultati, String searchTerm) {
        mainContentPanel.removeAll(); // Rimuovi tutti i componenti esistenti

        if (risultati.isEmpty()) {
            JLabel noResultsLabel = new JLabel("Nessun risultato trovato per: '" + searchTerm + "'");
            noResultsLabel.setForeground(Common_constants.colore_font_titoli);
            noResultsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainContentPanel.add(Box.createVerticalGlue());
            mainContentPanel.add(noResultsLabel);
            mainContentPanel.add(Box.createVerticalGlue());
        } else {
            mainContentPanel.add(createCategoryPanel("Risultati per \"" + searchTerm + "\"", risultati), searchTerm);
        }
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }



    // Metodo per creare un pannello di categoria con un titolo e una riga di libri scrollabile
    private JPanel createCategoryPanel(String categoryTitle, List<Libro> libriPerCategoria) {
        JPanel categoryContainer = new JPanel();
        categoryContainer.setLayout(new BoxLayout(categoryContainer, BoxLayout.Y_AXIS));
        categoryContainer.setOpaque(false);
        categoryContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(categoryTitle);
        titleLabel.setForeground(Common_constants.colore_bottoni);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryContainer.add(titleLabel);
        categoryContainer.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel bookRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        bookRowPanel.setOpaque(false);

        for (Libro libro : libriPerCategoria) {
            bookRowPanel.add(new PanelLibro(libro, this.gestoreLibreria));
        }

        JScrollPane rowScrollPane = new JScrollPane(bookRowPanel);
        rowScrollPane.setOpaque(false);
        rowScrollPane.getViewport().setOpaque(false);
        rowScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        rowScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rowScrollPane.setBorder(BorderFactory.createEmptyBorder());

        int rowScrollHeight = PanelLibro.getAltezza() + 20; // Altezza del PanelLibro + spazio per scrollbar

        rowScrollPane.setPreferredSize(new Dimension(getWidth() - 200, rowScrollHeight));
        rowScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowScrollHeight));

        categoryContainer.add(rowScrollPane);
        categoryContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        return categoryContainer;
    }

    // Metodo per creare bottoni personalizzati
    private JButton bottonePersonalizzato(String testo) {
        JButton bottone = new JButton(testo);
        bottone.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottone.setBackground(Common_constants.colore_secondario);
        bottone.setForeground(Common_constants.colore_font_titoli);
        bottone.setFont(new Font("Arial", Font.BOLD, 14));
        bottone.setPreferredSize(new Dimension(100, 40));
        bottone.setMaximumSize(new Dimension(150, 40));
        bottone.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Common_constants.colore_secondario, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        bottone.setFocusPainted(false);
        return bottone;
    }
}