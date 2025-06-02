package repository;

import builder.Libro;

import java.sql.SQLException;
import java.util.List;



public interface LibroRepository {

    void inserisciLibro(Libro libro) throws SQLException; // Mantengo inserisciLibro
    void eliminaLibro(Libro libro) throws SQLException; // Mantengo eliminaLibro (anche se un ISBN sarebbe più pulito)
    void modificaLibro(Libro libro) throws SQLException; // Mantengo modificaLibro
    Libro LibroConIsbn(String isbn) throws SQLException; // Mantengo LibroConIsbn
    List<Libro> tuttiLibri() throws SQLException; // Mantengo tuttiLibri e aggiungo throws SQLException
}