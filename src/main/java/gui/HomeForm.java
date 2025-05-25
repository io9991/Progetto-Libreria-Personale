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
        griglia.add(new PanelLibro("Titolo", "Autore", "codice", "Stato", 4));

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
