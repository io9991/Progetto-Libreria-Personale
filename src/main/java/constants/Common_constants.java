package constants;

import java.awt.*;

/*
    Ai fini di mantenere un maggiore ordine, questa classe
    contiene quelle costanti che appaiono con un'alta frequenza
    all'interno delle varie classi e metodi, in particolare
    quelle che appaiono nella GUI e nel DB
 */

public class Common_constants {

    //costanti della gui
    public static final Color colore_primario = Color.decode("#332D56");
    public static final Color colore_secondario = Color.decode("#4E6688");
    public static final Color colore_font_titoli = Color.decode("#ffffff");
    public static final Color colore_bottoni = Color.decode("#84aaff");
    public static final Color colore_menu_modifica = Color.decode("#dd863d");
    public static final Color colore_stella_piena = Color.decode("#ffc30f");
    public static final Color colore_croce = Color.decode("#b84141");

    //costanti del DB
//    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/library_schema";
//    public static final String DB_USERNAME = "java_user";
//    public static final String DB_PASSWORD = "1234";
//    public static final String DB_LIBRI_TABLE_NAME = "LIBRI";
    public static String DB_URL = "jdbc:h2:~/library_db;AUTO_SERVER=TRUE;MODE=MySQL";
    public static final String DB_USERNAME = "sa"; // Utente predefinito per H2 (system administrator)
    public static final String DB_PASSWORD = ""; // Password vuota per default in H2
    public static final String DB_LIBRI_TABLE_NAME = "LIBRI";



}
