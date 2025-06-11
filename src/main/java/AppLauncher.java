
//importo le dovute librerie
import builder.Libro;
import builder.Stato;
//import com.formdev.flatlaf.FlatDarculaLaf;
import db.MyJavaDBC;
import gui.*;
import com.formdev.flatlaf.*;
import repository.LibroRepositoryImpl;
import service.GestoreLibreria;

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
                    GestoreLibreria gestoreLibreria = GestoreLibreria.getInstance(new LibroRepositoryImpl());

                    HomeForm homeForm = new HomeForm("Libreria Personale");
                    homeForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // SOLO QUI
                    homeForm.setVisible(true);
                    //chiudo la connessione al db quando viene chiusa la schermata
                    homeForm.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                            MyJavaDBC.getInstance().closeConnection();
                            System.out.println("Applicazione chiusa. Connessione DB H2 disconnessa.");
                        }
                    });
                    homeForm.setVisible(true);
                } catch (SQLException e) {
                    System.err.println("Errore critico all'avvio dell'applicazione: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Errore all'avvio dell'applicazione. Controlla il database.",
                            "Errore Fatale", JOptionPane.ERROR_MESSAGE);
                    System.exit(1); // Esci dall'applicazione
                }
                // new PanelLibro(l);
            }
        });


    }


}
