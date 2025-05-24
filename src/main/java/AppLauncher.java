
//importo le dovute librerie
import gui.*;

import javax.swing.*;

public class AppLauncher {

    //main
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            //metodo run
            @Override
            public void run() {
                //istanziamo un form di login e rendiamolo visibile
                new HomeForm("Libreria").setVisible(true);

            }
        });


    }


}
