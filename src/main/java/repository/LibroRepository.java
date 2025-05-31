package repository;

import builder.Libro;

import java.sql.SQLException;
import java.util.List;

public interface LibroRepository {

    //metto i metodi che mi servono per le operazioni
    //che vengono fatte sul dataBase

    //metodo per aggiungere
    void inserisciLibro(Libro libro) throws SQLException;
    //metodo per eliminare
    void eliminaLibro(Libro libro) throws SQLException;
    //metodo per modificare
    void modificaLibro(Libro libro) throws SQLException;
    //metodo per ottenere un libro dal suo isbn
    Libro LibroConIsbn(String isbn) throws SQLException;
    //nmetodo che restituisce tutti i libri
    List<Libro> tuttiLibri();

}
