
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

    private JPanel mainContentPanel;
    private GestoreLibreria gestoreLibreria;

    public HomeForm(String titolo) throws SQLException {
        super(titolo);
        this.gestoreLibreria = GestoreLibreria.getInstance();
        this.gestoreLibreria.attach(this);
        getContentPane().setLayout(new BorderLayout());

        addGuiComponent();

        refreshLibriCategories();
    }

    private void addGuiComponent() {
        // --- Pannello superiore: Titolo e Barra di Ricerca ---
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

        // Listener per il pulsante "Aggiungi"
        aggiungi.addActionListener(e -> {
            AggiungiLibroForm addBookForm = new AggiungiLibroForm("Aggiungi un nuovo libro", null);
            addBookForm.setModal(true);
            addBookForm.setVisible(true);
        });

        // Listener per i pulsanti "Filtra" e "Ordina" (ancora da implementare)
        filtra.addActionListener(e -> JOptionPane.showMessageDialog(this, "Azione Filtra (TEST)"));
        ordina.addActionListener(e -> JOptionPane.showMessageDialog(this, "Azione Ordina (TEST)"));
    }

    @Override
    public void update() {
        try {
            System.out.println("HomeForm: Notifica di cambiamento ricevuta. Aggiorno la visualizzazione dei libri.");
            refreshLibriCategories();
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
            bookRowPanel.add(new PanelLibro(libro));
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