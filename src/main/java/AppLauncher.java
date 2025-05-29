
//importo le dovute librerie
import builder.Libro;
import builder.Stato;
import gui.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

public class AppLauncher {

    static Libro l = new Libro.Builder().setTitolo("Ciao").setAutore("Hey").setCodiceISBN("1234").setStato(Stato.LETTO).setGenereAppartenenza("").setValutazione(3).build();
    //main
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            //metodo run
            @Override
            public void run() {
                FlatLightLaf.setup();
                //istanziamo un form di login e rendiamolo visibile
                new HomeForm("Libreria").setVisible(true);
               // new PanelLibro(l);
            }
        });


    }


}
