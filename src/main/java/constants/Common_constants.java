package constants;

import java.awt.*;
//qui metterò le costanti comuni come i colori, font, e le caratteristiche del database

public class Common_constants {

    //parto dai colori scelti per la gui

    public static final Color colore_primario = Color.decode("#153448");
    public static final Color colore_secondario = Color.decode("#3C5B6F");
    public static final Color colore_font_titoli = Color.decode("#948979");
    public static final Color colore_font_dettagli = Color.decode("#DFD0B8");

    //costanti del DB
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/library_schema";
    public static final String DB_USERNAME = "java_user";
    public static final String DB_PASSWORD = "1234";
    public static final String DB_LIBRI_TABLE_NAME = "LIBRI";



}
