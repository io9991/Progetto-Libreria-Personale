

// PanelLibro.java
package gui;

import constants.Common_constants;
import builder.Libro; // Assicurati che Libro sia importato correttamente
import builder.Stato; // Assicurati che Stato sia importato correttamente
import service.GestoreLibreria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Objects;



public class PanelLibro extends JPanel {

    // Riduci le dimensioni dei riquadri
    private static final int larghezza = 150; // Era 200
    private static final int altezza = 200;  // Era 250

    private Libro libro;

    // Metodi statici per accedere alle dimensioni
    public static int getLarghezza() {
        return larghezza;
    }

    public static int getAltezza() {
        return altezza;
    }

    public PanelLibro(Libro libro) {
        this.libro = Objects.requireNonNull(libro, "L'oggetto Libro non può essere null.");

        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(larghezza, altezza));
        setMinimumSize(new Dimension(larghezza, altezza));
        setMaximumSize(new Dimension(larghezza, altezza));
        setBorder(BorderFactory.createLineBorder(Common_constants.colore_font_titoli, 2));
        setBackground(Common_constants.colore_secondario);

        // MouseListener per rendere l'intero pannello cliccabile
        //se clicco il libro mi apre un jdialog che mi permette la
        //modifica
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AggiungiLibroForm modificaForm = new AggiungiLibroForm("Modifica il tuo libro", libro);
                System.out.println("DEBUG: PanelLibro - Cliccato per modificare. Libro passato: " +
                        (PanelLibro.this.libro != null ?
                                PanelLibro.this.libro.getTitolo() + " (ISBN: " + PanelLibro.this.libro.getCodice_ISBN() + ")" :
                                "NULL (Problema qui!)"));

                modificaForm.setVisible(true);
             //   modificaForm.setModal(true);
              //  JOptionPane.showMessageDialog(PanelLibro.this,
              //          "Hai cliccato sul libro: " + libro.getTitolo() + "\nISBN: " + libro.getCodice_ISBN(),
              //          "Dettagli Libro (TEST)",
              //          JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createLineBorder(Common_constants.colore_menu_modifica, 3));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                setBorder(BorderFactory.createLineBorder(Common_constants.colore_font_titoli, 2));
            }
        });

        // ----------------- Pannello informazioni principali (CENTER) -----------------
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JLabel titoloLabel = new JLabel("<html><b>" + libro.getTitolo() + "</b></html>");
        titoloLabel.setForeground(Common_constants.colore_font_titoli);
        titoloLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titoloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel autoreLabel = new JLabel("Autore: " + libro.getAutore());
        autoreLabel.setForeground(Common_constants.colore_font_titoli);
        autoreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        autoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel isbnLabel = new JLabel("ISBN: " + libro.getCodice_ISBN());
        isbnLabel.setForeground(Common_constants.colore_font_titoli);
        isbnLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        isbnLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(titoloLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(autoreLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(isbnLabel);
        infoPanel.add(Box.createVerticalGlue()); // Spinge i componenti verso l'alto

        add(infoPanel, BorderLayout.CENTER);

        // ----------------- Parte inferiore del pannello (SOUTH) -----------------
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 5));



        JPanel ratingAndDeletePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        ratingAndDeletePanel.setOpaque(false);

        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        starPanel.setOpaque(false);
        for (int i = 1; i <= 5; i++) {
            JLabel star = new JLabel("★");
            star.setForeground(i <= libro.getValutazione() ? Common_constants.colore_stella_piena : Common_constants.colore_secondario);
            star.setFont(new Font("Dialog", Font.BOLD, 18));
            starPanel.add(star);
        }
        ratingAndDeletePanel.add(starPanel);

        JButton deleteButton = new JButton("X");
        deleteButton.setBackground(Common_constants.colore_secondario);
        deleteButton.setForeground(Common_constants.colore_croce);
        deleteButton.setPreferredSize(new Dimension(30, 30));
        deleteButton.setBorder(BorderFactory.createEmptyBorder());
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Vuoi eliminare il libro: " + libro.getTitolo() + " (ISBN: " + libro.getCodice_ISBN() + ")?",
                    "Conferma Eliminazione",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Eliminazione confermata per libro: " + libro.getTitolo());
                //logica cancellazione seguendo l'observer
                try {
                    //richiamiamo la funzione che permette l'eleminazione del libro
                    GestoreLibreria.getInstance().rimuoviLibro(libro);
                    JOptionPane.showMessageDialog(this, "Libro eliminato!");
                    //il gestore libreria notifica l'homeform della modifica avvenuta
                }catch(SQLException sqe){
                    System.out.println("Errore nell'eliminazione");
                    sqe.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Errore nell'eliminazione!");

                }
            }else{
                System.out.println("Eliminazione annullata");
                JOptionPane.showMessageDialog(this, "Hai scelto di non eliminare il libro");
            }
        });
        ratingAndDeletePanel.add(deleteButton);

        bottomPanel.add(ratingAndDeletePanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private String formatStato(Stato stato) {
        return stato.name().replace("_", " ").toLowerCase().chars()
                .mapToObj(c -> (char) c)
                .collect(StringBuilder::new, (sb, c) -> {
                    if (sb.isEmpty() || Character.isWhitespace(sb.charAt(sb.length() - 1))) {
                        sb.append(Character.toUpperCase(c));
                    } else {
                        sb.append(c);
                    }
                }, StringBuilder::append)
                .toString();
    }
}
