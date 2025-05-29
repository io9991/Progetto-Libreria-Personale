package gui;

//questo form servirà per l'aggiunta di un nuovo libro, richiederà quindi i vari campi, tenendo conto di campi obbligatori quali
//il titolo, il codice e l'autore
//gli altri potranno essere anche tralasciati
//per fare quello che ho detto nelle righe precedenti, mi avvalgo dell'utilizzo di un mediator

import builder.*;

import javax.swing.*;

public class AggiungiLibroForm extends JDialog {


    //i vari campi saranno
    //titolo, autore, isbn, genere, valutazione, stato
    private JTextField campoTesto;
    private JTextField campoAutore;
    private JTextField campoCodice;
    private JTextField campoGenere;
    private ComboBoxModel<Stato> statoBox;
    private ComboBoxModel<Integer> valBox;
    private JButton bottoneAggiungi;




}
