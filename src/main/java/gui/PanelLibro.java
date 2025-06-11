package gui;

import constants.Common_constants;

import javax.swing.*;
import java.awt.*;

public class PanelLibro extends JPanel {

    private static final int larghezza = 200;
    private static final int altezza = 250;

    private int valutazione;

    public PanelLibro(String titolo, String autore, String isbn, String statoLettura, int valutazione) {

        //salvo valutazione
        this.valutazione = valutazione;

        setLayout(new BorderLayout(5,5));
        setPreferredSize(new Dimension(larghezza, altezza)); // box fisso
        setMinimumSize(new Dimension(larghezza, altezza));
        setMaximumSize(new Dimension(larghezza, altezza));
        // Bordo per distinguere
        setBorder(BorderFactory.createLineBorder(Common_constants.colore_font_titoli, 2));
        setBackground(Common_constants.colore_secondario);

        //pannello informazioni principali
        //utilizzo un box
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        // Titolo (in alto)
        JLabel titoloLabel = new JLabel("<html><b>" + titolo + "</b></html>");
        titoloLabel.setForeground(Common_constants.colore_font_titoli);
        titoloLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titoloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel autoreLabel = new JLabel("Autore" + autore );
        autoreLabel.setForeground(Common_constants.colore_font_titoli);
        autoreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        autoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        //add(titoloLabel, BorderLayout.NORTH);

        JLabel isbnLabel = new JLabel("ISBN" + isbn );
        autoreLabel.setForeground(Common_constants.colore_font_titoli);
        autoreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        autoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(titoloLabel);
        info.add(Box.createVerticalStrut(5));
        info.add(autoreLabel);
        info.add(Box.createVerticalStrut(5));
        info.add(isbnLabel);
        //spingo i componenti verso l'alto
        info.add(Box.createVerticalGlue());

        add(info, BorderLayout.CENTER);

        //parte inferiore del pannello  todo


    }

}
