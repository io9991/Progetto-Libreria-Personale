
//importo le dovute librerie
import builder.Libro;
import builder.Stato;
//import com.formdev.flatlaf.FlatDarculaLaf;
import gui.*;
import com.formdev.flatlaf.*;
import javax.swing.*;
import java.sql.SQLException;

public class AppLauncher {

    //static Libro l = new Libro.Builder().setTitolo("Ciao").setAutore("Hey").setCodiceISBN("1234").setStato(Stato.LETTO).setGenereAppartenenza("").setValutazione(3).build();
    //main
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            //metodo run
            @Override
            public void run() {
                //FlatLightLaf.setup();
                //per il theme
                FlatDarculaLaf.setup();
                //istanziamo un form di login e rendiamolo visibile
                try {
                    HomeForm homeForm = new HomeForm("Libreria Personale");
                    homeForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // SOLO QUI
                    homeForm.setVisible(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                // new PanelLibro(l);
            }
        });


    }


}
