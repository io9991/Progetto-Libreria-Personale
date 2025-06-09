package gui;




import builder.Libro;
import builder.Libro;
import builder.Stato;
import constants.Common_constants;
import mediator.AggiungiLibroFormMediator;
import service.GestoreLibreria;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;


public class AggiungiLibroForm extends JDialog {

    private JTextField titoloField;
    private JTextField autoreField;
    private JTextField isbnField;
    private JTextField genereField;
    private JComboBox<Integer> valutazioneComboBox;
    private JComboBox<Stato> statoComboBox;
    private JButton saveButton;

    private GestoreLibreria gestoreLibreria;
    private AggiungiLibroFormMediator mediator;
    private Libro libroToModify;


    public AggiungiLibroForm(String titolo, Libro libroDaModificare) {
        super((JFrame) null, titolo, true);
        this.gestoreLibreria = GestoreLibreria.getInstance();
        this.mediator = new AggiungiLibroFormMediator();
        this.libroToModify = libroDaModificare; // Assegna qui il libro da modificare

        if (this.libroToModify != null) {
            System.out.println("AggiungiLibroForm: Aperto in modalità MODIFICA per libro: " + this.libroToModify.getTitolo() + " (ISBN: '" + this.libroToModify.getCodice_ISBN() + "')");
        } else {
            System.out.println("AggiungiLibroForm: Aperto in modalità AGGIUNGI NUOVO LIBRO.");
        }

        initComponents();
        setupMediator();
        setupListeners();

        setSize(400, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Common_constants.colore_primario);

        // DEBUG: Stampa lo stato di libroToModify all'apertura del form
        if (this.libroToModify != null) {
            System.out.println("AggiungiLibroForm: Aperto in modalità MODIFICA per libro: " + this.libroToModify.getTitolo() + " (ISBN: '" + this.libroToModify.getCodice_ISBN() + "')");
        } else {
            System.out.println("AggiungiLibroForm: Aperto in modalità AGGIUNGI NUOVO LIBRO.");
        }

        mediator.widgetCambiato(null); // Inizializza lo stato del bottone
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Color labelColor = Common_constants.colore_font_titoli;

        Dimension fieldSize = new Dimension(200, 30);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Color fieldBackground = Common_constants.colore_bottoni;
        Color fieldForeground = Common_constants.colore_font_titoli;

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

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
        JLabel genereLabel = new JLabel("Genere");
        genereLabel.setForeground(Common_constants.colore_font_titoli);
        genereLabel.setFont(labelFont);
        add(genereLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        genereField = new JTextField();
        genereField.setPreferredSize(fieldSize);
        genereField.setFont(fieldFont);
        genereField.setBackground(fieldBackground);
        genereField.setForeground(fieldForeground);
        add(genereField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST;
        JLabel valutazioneLabel = new JLabel("Valutazione");
        valutazioneLabel.setForeground(Common_constants.colore_font_titoli);
        valutazioneLabel.setFont(labelFont);
        add(valutazioneLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        Integer[] ratings = {0, 1, 2, 3, 4, 5};
        valutazioneComboBox = new JComboBox<>(ratings);
        valutazioneComboBox.setPreferredSize(fieldSize);
        valutazioneComboBox.setFont(fieldFont);
        valutazioneComboBox.setBackground(fieldBackground);
        valutazioneComboBox.setForeground(fieldForeground);
        add(valutazioneComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.WEST;
        JLabel statoLabel = new JLabel("Stato");
        statoLabel.setForeground(Common_constants.colore_font_titoli);
        statoLabel.setFont(labelFont);
        add(statoLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 5; gbc.fill = GridBagConstraints.HORIZONTAL;
        statoComboBox = new JComboBox<>(Stato.values());
        statoComboBox.setPreferredSize(fieldSize);
        statoComboBox.setFont(fieldFont);
        statoComboBox.setBackground(fieldBackground);
        statoComboBox.setForeground(fieldForeground);
        add(statoComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);
        saveButton = new JButton("Salva");
        saveButton.setPreferredSize(new Dimension(150, 45));
        saveButton.setBackground(Common_constants.colore_secondario);
        saveButton.setForeground(Common_constants.colore_font_titoli);
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setFocusPainted(false);
        saveButton.setEnabled(false);
        add(saveButton, gbc);

        // Pre-popola i campi se stiamo modificando un libro
        if (this.libroToModify != null) {
            populateFields(this.libroToModify);
            saveButton.setText("Salva Modifiche");
            isbnField.setEditable(false); // L'ISBN non è modificabile in modalità modifica
            isbnField.setBackground(Common_constants.colore_primario); // Un colore per indicare che non è modificabile
        }
    }

    private void setupMediator() {
        mediator.setTitoloField(titoloField);
        mediator.setAutoreField(autoreField);
        mediator.setIsbnField(isbnField);
        mediator.setSaveButton(saveButton);
    }

    private void setupListeners() {
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) { updateMediator(); }
            @Override
            public void insertUpdate(DocumentEvent e) { updateMediator(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateMediator(); }

            private void updateMediator() {
                mediator.widgetCambiato(null);
            }
        };

        titoloField.getDocument().addDocumentListener(documentListener);
        autoreField.getDocument().addDocumentListener(documentListener);
        isbnField.getDocument().addDocumentListener(documentListener);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSaveAction();
            }
        });
    }

    private void handleSaveAction() {
        String titolo = titoloField.getText().trim();
        String autore = autoreField.getText().trim();
        String isbn = isbnField.getText().trim(); // Ottieni l'ISBN dal campo
        String genere = genereField.getText().trim();
        int valutazione = (Integer) valutazioneComboBox.getSelectedItem();
        Stato stato = (Stato) statoComboBox.getSelectedItem();

        if (titolo.isEmpty() || autore.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(AggiungiLibroForm.this,
                    "Titolo, Autore e Codice ISBN sono campi obbligatori.",
                    "Campi Mancanti", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (libroToModify == null) {
                // Modalità Aggiungi Nuovo Libro
                Libro nuovoLibro = new Libro.Builder()
                        .setTitolo(titolo)
                        .setAutore(autore)
                        .setCodiceISBN(isbn)
                        .setGenereAppartenenza(genere.isEmpty() ? null : genere)
                        .setValutazione(valutazione)
                        .setStato(stato)
                        .build();

                System.out.println("AggiungiLibroForm: Tentativo di AGGIUNGERE libro con ISBN: '" + nuovoLibro.getCodice_ISBN() + "'");
                gestoreLibreria.aggiungiLibro(nuovoLibro);
                JOptionPane.showMessageDialog(AggiungiLibroForm.this,
                        "Libro aggiunto con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Modalità Modifica Libro Esistente
                // Aggiorna le proprietà dell'oggetto 'libroToModify' esistente
                libroToModify.setTitolo(titolo);
                libroToModify.setAutore(autore);
                // L'ISBN di libroToModify è già quello originale, non lo modifichiamo qui.
                libroToModify.setGenere_appartenenza(genere.isEmpty() ? null : genere);
                libroToModify.setValutazione(valutazione);
                libroToModify.setStato(stato);

                System.out.println("AggiungiLibroForm: Tentativo di MODIFICARE libro con ISBN originale: '" + libroToModify.getCodice_ISBN() + "'");
                gestoreLibreria.aggiornaLibro(libroToModify);
                JOptionPane.showMessageDialog(AggiungiLibroForm.this,
                        "Libro aggiornato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (SQLException ex) {
            System.err.println("ERRORE SQL in AggiungiLibroForm: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(AggiungiLibroForm.this,
                    "Errore durante l'operazione sul database: " + ex.getMessage() + "\nControlla la console per i dettagli.",
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            System.err.println("ERRORE DI VALIDAZIONE/COSTRUZIONE LIBRO in AggiungiLibroForm: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(AggiungiLibroForm.this,
                    "Errore nei dati del libro: " + ex.getMessage(),
                    "Errore Dati", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            System.err.println("ERRORE INATTESO in AggiungiLibroForm: " + ex.getClass().getName() + " - " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(AggiungiLibroForm.this,
                    "Si è verificato un errore inatteso: " + ex.getMessage() + "\nControlla la console per i dettagli.",
                    "Errore Grave", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFields(Libro libro) {
        titoloField.setText(libro.getTitolo());
        autoreField.setText(libro.getAutore());
        isbnField.setText(libro.getCodice_ISBN());
        genereField.setText(libro.getGenere_appartenenza() == null ? "" : libro.getGenere_appartenenza());
        valutazioneComboBox.setSelectedItem(libro.getValutazione());
        statoComboBox.setSelectedItem(libro.getStato());
        // L'ISBN è già impostato come non modificabile nel initComponents
    }

    // Metodo di debug temporaneo per AggiungiLibroForm
    public Libro getLibroToModifyDebug() {
        return libroToModify;
    }


}