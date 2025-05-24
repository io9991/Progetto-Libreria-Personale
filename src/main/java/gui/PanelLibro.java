package gui;

import constants.Common_constants;

import javax.swing.*;
import java.awt.*;

public class PanelLibro extends JPanel {

    public PanelLibro(String titolo, String autore, String isbn, String statoLettura) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(30, 30)); // box fisso

        // Bordo per distinguere
        setBorder(BorderFactory.createLineBorder(Common_constants.colore_font_titoli, 1));
        setBackground(Common_constants.colore_secondario);

        // Titolo (in alto)
        JLabel titoloLabel = new JLabel("<html><b>" + titolo + "</b></html>");
        titoloLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(titoloLabel, BorderLayout.NORTH);

        // Dettagli centrali
        JPanel dettagli = new JPanel(new GridLayout(2, 1));
        dettagli.setOpaque(false);
        dettagli.add(new JLabel("Autore: " + autore));
        dettagli.add(new JLabel("ISBN: " + isbn));
        add(dettagli, BorderLayout.CENTER);

        // Stato lettura (in basso)
        JLabel stato = new JLabel(statoLettura);
        stato.setHorizontalAlignment(JLabel.RIGHT);
        stato.setForeground(Common_constants.colore_font_titoli);
        stato.setFont(new Font("SansSerif", Font.BOLD, 12));
        add(stato, BorderLayout.SOUTH);
    }

}
