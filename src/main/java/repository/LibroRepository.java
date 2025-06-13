package repository;

import builder.Libro;

import java.sql.SQLException;
import java.util.List;

/*
    interfaccia che definisce i metodi necessari per la comunicazione
    tra database e applicazione
 */

public interface LibroRepository {

    void inserisciLibro(Libro libro) throws SQLException;
    void eliminaLibro(Libro libro) throws SQLException;
    void modificaLibro(Libro libro) throws SQLException;
    Libro LibroConIsbn(String isbn) throws SQLException;
    List<Libro> tuttiLibri() throws SQLException;
    //per la ricerca
    List<Libro> cercaLibri(String query) throws SQLException;

    //per i filtri
    List<String> getAutoriDistinti() throws SQLException;
    List<String> getGeneriDistinti() throws SQLException;
    List<Libro> cercaLibriByAutore(String autore) throws SQLException;
    List<Libro> cercaLibriByGenere(String genere) throws SQLException;
    List<Libro> cercaLibriByValutazione(int valutazione) throws SQLException;

    //per l'ordinamento
    List<Libro> libriOrdinati(String criterio, String direzione ) throws SQLException;


}