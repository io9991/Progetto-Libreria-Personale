package gui;

//importo il package relativo alle costanti che ho creato in precedenza
import constants.*;
//importo la libreria che mi permetterà di poter utilizzare il pacchetto grafico
import javax.swing.*;

//estendiamo JFrame
public class Form extends JFrame{

    //costruttore
    public Form(String titolo){
        //settiamo il titolo rifacendoci alla classe super
        super(titolo);

        //settiamo le dimensioni
        setSize(1200, 807);

        //configuro la GUI in modo tale che metta fine al processo quando chiuso
       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //in questo modo possiamo posizionare i component dove vogliamo
        setLayout(null);

        //facciamo aprire la finestra al centro dello schermo
        setLocationRelativeTo(null);

        //se non vogliamo permettere di far cambiare la dimensione del frame
        setResizable(false);

        //setto il background
        getContentPane().setBackground(Common_constants.colore_primario);
    }

}
