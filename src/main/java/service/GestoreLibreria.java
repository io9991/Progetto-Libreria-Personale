package service;


import builder.Libro;
import db.MyJavaDBC;
import observer.Observer;
import observer.Subject;
import repository.LibroRepository;
import repository.LibroRepositoryImpl;

import java.sql.SQLException;
import java.util.*;


public class GestoreLibreria implements Subject{

    private static GestoreLibreria instance;
    private List<Observer> osservatori;
    private LibroRepository libroRepository;

    private GestoreLibreria(){
        osservatori = new ArrayList<>();
        this.libroRepository = new LibroRepositoryImpl();
    }

    public static synchronized GestoreLibreria getInstance(){
        if(instance == null ){
            instance = new GestoreLibreria();
        }
        return instance;
    }

    @Override
    public void attach(Observer o) {
        if(!osservatori.contains(o)){
            osservatori.add(o);
        }
    }

    @Override
    public void detach(Observer o) {
        if(osservatori.contains(o)){
            osservatori.remove(o);
        }
    }

    @Override
    public void notifyObserver() throws SQLException {
        List<Observer> osservatoriCopia = new ArrayList<>(this.osservatori);
        for (Observer o : osservatoriCopia){
            o.update();
        }
    }

    public void aggiungiLibro(Libro libro) throws SQLException{
        // Rimuovo il try-catch per propagare l'errore alla GUI
        libroRepository.inserisciLibro(libro); // Mantengo il nome del metodo
        notifyObserver(); // Notifica solo se l'inserimento è riuscito
    }

    public void rimuoviLibro(Libro libro) throws SQLException {
        libroRepository.eliminaLibro(libro); // Mantengo il nome del metodo
        notifyObserver();
    }

    public List<Libro> restituisciLibri() throws SQLException{
        return libroRepository.tuttiLibri(); // Mantengo il nome del metodo
    }

    public void aggiornaLibro(Libro libro) throws SQLException{
        // Rimuovo il try-catch per propagare l'errore alla GUI
        libroRepository.modificaLibro(libro); // Mantengo il nome del metodo
        notifyObserver();
    }

}