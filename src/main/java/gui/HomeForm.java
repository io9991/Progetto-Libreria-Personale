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
        JPanel superiore = new JPanel(new BorderLayout());
        superiore.setBackground(Common_constants.colore_primario);
        //il titolo lo metto in una label
        JLabel titolo = new JLabel("Personal Library");
        //camnio colore del font e tipologia
        titolo.setForeground(Common_constants.colore_font_titoli);
        titolo.setFont(new Font("Dialog", Font.BOLD, 30));

        superiore.add(titolo, BorderLayout.WEST);
        //aggiungo la barra di ricerca
        JTextField ricerca = new JTextField();
        ricerca.setPreferredSize(new Dimension(300, 30));
        ricerca.setBackground(Common_constants.colore_secondario);
        ricerca.setForeground(Common_constants.colore_font_titoli);
        ricerca.setFont(new Font("Dialog", Font.PLAIN, 15));

        ricerca.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

        superiore.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        superiore.add(ricerca, BorderLayout.EAST);

        getContentPane().add(superiore, BorderLayout.NORTH);


        //passiamo alla parte centrale dove mettiamo la griglia per i libri
        JPanel griglia = new JPanel(new GridLayout(6,6,10,10));
        griglia.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        griglia.setBackground(Common_constants.colore_primario);
        griglia.add(new PanelLibro("Titolo", "Autore", "codice", "Stato"));
        griglia.add(new PanelLibro("Titolo2", "Autore2", "codice2", "Stato2"));
        griglia.add(new PanelLibro("Titolo2", "Autore2", "codice2", "Stato2"));

        JScrollPane centrale = new JScrollPane(griglia);
        getContentPane().add(centrale, BorderLayout.CENTER);

    }



}
