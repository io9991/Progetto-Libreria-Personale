package constants;

import java.awt.*;
//qui metterò le costanti comuni come i colori, font, e le caratteristiche del database

public class Common_constants {

    //parto dai colori scelti per la gui  #e5e3ce
    //#251d47
    //#152443
    public static final Color colore_primario = Color.decode("#251d47");
    public static final Color colore_secondario = Color.decode("#e59a5b");
    public static final Color colore_font_titoli = Color.decode("#ffffff");
    public static final Color colore_bottoni = Color.decode("#84aaff");
    public static final Color colore_menu_modifica = Color.decode("#dd863d");
    public static final Color colore_stella_piena = Color.decode("#ffc30f");
    public static final Color colore_croce = Color.decode("#b84141");

    //costanti del DB
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/library_schema";
    public static final String DB_USERNAME = "java_user";
    public static final String DB_PASSWORD = "1234";
    public static final String DB_LIBRI_TABLE_NAME = "LIBRI";



}
