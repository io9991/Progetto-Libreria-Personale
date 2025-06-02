package gui;

//questo form servirà per l'aggiunta di un nuovo libro, richiederà quindi i vari campi, tenendo conto di campi obbligatori quali
//il titolo, il codice e l'autore
//gli altri potranno essere anche tralasciati
//per fare quello che ho detto nelle righe precedenti, mi avvalgo dell'utilizzo di un mediator

import builder.Libro;
import builder.Stato;
import constants.Common_constants; // Assicurati di avere le tue costanti di colore
import mediator.AggiungiLibroFormMediator;
import service.GestoreLibreria; // Importa il gestore per l'aggiunta del libro
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Objects; // Per Objects.requireNonNull



public class AggiungiLibroForm extends JDialog {

    private JTextField titoloField;
    private JTextField autoreField;
    private JTextField isbnField;
    private JTextField genereField;
    private JComboBox<Integer> valutazioneComboBox;
    private JComboBox<Stato> statoComboBox;
    private JButton saveButton;

    private GestoreLibreria gestoreLibreria; // Riferimento al GestoreLibreria
    private AggiungiLibroFormMediator mediator; // Riferimento al Mediator

    // Costruttore per la modalità "Aggiungi" (senza libro preesistente)
    public AggiungiLibroForm(String titolo) {
        super((JFrame) null, titolo, true); // Parent frame a null, modale true
        this.gestoreLibreria = GestoreLibreria.getInstance(); // Ottieni l'istanza del GestoreLibreria

        // Inizializza il Mediator prima di inizializzare i componenti GUI
        this.mediator = new AggiungiLibroFormMediator();

        initComponents(); // Inizializza i componenti della GUI
        setupMediator(); // Collega i componenti al Mediator
        setupListeners(); // Aggiungi i listener per i pulsanti

        // Configurazione della finestra
        setSize(400, 500); // Dimensioni della finestra (puoi aggiustare)
        setResizable(false);
        setLocationRelativeTo(null); // Centra la finestra
        getContentPane().setBackground(Common_constants.colore_primario);

        //stato iniziale del bottone disattivato
        mediator.widgetCambiato(titoloField);

    }

    // Costruttore per la modalità "Modifica" (con un libro preesistente)

    public AggiungiLibroForm(String titolo, Libro libroDaModificare) {
        this(titolo); // Chiama il costruttore "aggiungi" per l'inizializzazione base
        // Pre-popola i campi con i dati del libro da modificare
        populateFields(libroDaModificare);
        // Cambia il testo del pulsante in "Modifica" o "Salva Modifiche"
        saveButton.setText("Aggiungi");
        saveButton.setEnabled(false);

    }

    private void initComponents() {
        setLayout(new GridBagLayout()); // Usa GridBagLayout per un layout più flessibile
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margini interni

        // Impostazioni comuni per le etichette (Label)
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Color labelColor = Common_constants.colore_font_titoli; // O un altro colore che indichi "obbligatorio" per Titolo, Autore, ISBN

        // Impostazioni comuni per i campi di testo (TextField)
        Dimension fieldSize = new Dimension(200, 30);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Color fieldBackground = Common_constants.colore_bottoni;
        Color fieldForeground = Common_constants.colore_font_titoli;

        // Titolo
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel titoloLabel = new JLabel("Titolo");
        titoloLabel.setForeground(labelColor);
        titoloLabel.setFont(labelFont);
        add(titoloLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        titoloField = new JTextField();
        titoloField.setPreferredSize(fieldSize);
        titoloField.setFont(fieldFont);
        titoloField.setBackground(fieldBackground);
        titoloField.setForeground(fieldForeground);
        add(titoloField, gbc);

        // Autore
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        JLabel autoreLabel = new JLabel("Autore");
        autoreLabel.setForeground(labelColor);
        autoreLabel.setFont(labelFont);
        add(autoreLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        autoreField = new JTextField();
        autoreField.setPreferredSize(fieldSize);
        autoreField.setFont(fieldFont);
        autoreField.setBackground(fieldBackground);
        autoreField.setForeground(fieldForeground);
        add(autoreField, gbc);

        // Codice ISBN
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        JLabel isbnLabel = new JLabel("Codice ISBN");
        isbnLabel.setForeground(labelColor);
        isbnLabel.setFont(labelFont);
        add(isbnLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        isbnField = new JTextField();
        isbnField.setPreferredSize(fieldSize);
        isbnField.setFont(fieldFont);
        isbnField.setBackground(fieldBackground);
        isbnField.setForeground(fieldForeground);
        add(isbnField, gbc);

        // Genere
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
        JLabel genereLabel = new JLabel("Genere");
        genereLabel.setForeground(Common_constants.colore_font_titoli); // Meno enfatico se non obbligatorio
        genereLabel.setFont(labelFont);
        add(genereLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        genereField = new JTextField();
        genereField.setPreferredSize(fieldSize);
        genereField.setFont(fieldFont);
        genereField.setBackground(fieldBackground);
        genereField.setForeground(fieldForeground);
        add(genereField, gbc);

        // Valutazione
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST;
        JLabel valutazioneLabel = new JLabel("Valutazione");
        valutazioneLabel.setForeground(Common_constants.colore_font_titoli);
        valutazioneLabel.setFont(labelFont);
        add(valutazioneLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        Integer[] ratings = {0, 1, 2, 3, 4, 5}; // 0 per "non valutato" o "scegli"
        valutazioneComboBox = new JComboBox<>(ratings);
        valutazioneComboBox.setPreferredSize(fieldSize);
        valutazioneComboBox.setFont(fieldFont);
        valutazioneComboBox.setBackground(fieldBackground);
        valutazioneComboBox.setForeground(fieldForeground);
        add(valutazioneComboBox, gbc);

        // Stato
        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.WEST;
        JLabel statoLabel = new JLabel("Stato");
        statoLabel.setForeground(Common_constants.colore_font_titoli);
        statoLabel.setFont(labelFont);
        add(statoLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 5; gbc.fill = GridBagConstraints.HORIZONTAL;
        statoComboBox = new JComboBox<>(Stato.values()); // Popola con i valori dell'enum Stato
        statoComboBox.setPreferredSize(fieldSize);
        statoComboBox.setFont(fieldFont);
        statoComboBox.setBackground(fieldBackground);
        statoComboBox.setForeground(fieldForeground);
        add(statoComboBox, gbc);

        // Pulsante Save
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; // Occupa 2 colonne
        gbc.fill = GridBagConstraints.NONE; // Non si espande
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10); // Margini più ampi per il bottone
        saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(150, 45));
        saveButton.setBackground(Common_constants.colore_secondario);
        saveButton.setForeground(Common_constants.colore_font_titoli);
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setFocusPainted(false);
        saveButton.setEnabled(false); // Inizialmente disabilitato per il Mediator
        add(saveButton, gbc);
    }

    // Metodo per collegare i componenti al Mediator
    private void setupMediator() {
        mediator.setTitoloField(titoloField);
        mediator.setAutoreField(autoreField);
        mediator.setIsbnField(isbnField);
        mediator.setSaveButton(saveButton);
    }

    private void setupListeners() {
        // Aggiungi DocumentListener a tutti i campi di testo obbligatori
        DocumentListener documentListener = new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                //non usato
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                mediator.widgetCambiato((JComponent) e.getDocument().getProperty("owner"));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mediator.widgetCambiato((JComponent) e.getDocument().getProperty("owner"));
            }


        };

        titoloField.getDocument().putProperty("owner", titoloField); // Associa il componente al documento
        titoloField.getDocument().addDocumentListener(documentListener);

        autoreField.getDocument().putProperty("owner", autoreField);
        autoreField.getDocument().addDocumentListener(documentListener);

        isbnField.getDocument().putProperty("owner", isbnField);
        isbnField.getDocument().addDocumentListener(documentListener);

        // Listener per il pulsante Save
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Preleva i dati dai campi
                String titolo = titoloField.getText().trim();
                String autore = autoreField.getText().trim();
                String isbn = isbnField.getText().trim();
                String genere = genereField.getText().trim();
                int valutazione = (Integer) valutazioneComboBox.getSelectedItem();
                Stato stato = (Stato) statoComboBox.getSelectedItem();

                // Crea l'oggetto Libro usando il Builder Pattern
                Libro libro = new Libro.Builder()
                        .setTitolo(titolo)
                        .setAutore(autore)
                        .setCodiceISBN(isbn)
                        .setGenereAppartenenza(genere.isEmpty() ? null : genere)
                        .setValutazione(valutazione)
                        .setStato(stato)
                        .build();

                try {
                    // Qui dovrai implementare la logica per distinguere tra aggiunta e modifica.
                    // Ad esempio, se la form è stata aperta con un libro esistente (modalità modifica),
                    // dovresti chiamare un metodo di modifica nel GestoreLibreria.
                    // Per ora, assumiamo sia sempre un'aggiunta come nel tuo esempio iniziale.
                    gestoreLibreria.aggiungiLibro(libro);

                    JOptionPane.showMessageDialog(AggiungiLibroForm.this,
                            "Libro salvato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(AggiungiLibroForm.this,
                            "Errore durante il salvataggio del libro: " + ex.getMessage(),
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(AggiungiLibroForm.this,
                            ex.getMessage(),
                            "Errore di Validazione", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void populateFields(Libro libro) {
        Objects.requireNonNull(libro, "Il libro da modificare non può essere null.");
        titoloField.setText(libro.getTitolo());
        autoreField.setText(libro.getAutore());
        isbnField.setText(libro.getCodice_ISBN());
        genereField.setText(libro.getGenere_appartenenza() == null ? "" : libro.getGenere_appartenenza()); // Gestisci null
        valutazioneComboBox.setSelectedItem(libro.getValutazione());
        statoComboBox.setSelectedItem(libro.getStato());
        isbnField.setEditable(false); // L'ISBN non è modificabile
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AggiungiLibroForm form = new AggiungiLibroForm("Aggiungi il tuo libro");
            form.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            form.setVisible(true);
        });
    }
}