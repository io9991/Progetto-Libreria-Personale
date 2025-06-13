package service;


import builder.Libro;
import db.MyJavaDBC;
import observer.Observer;
import observer.Subject;
import repository.LibroRepository;
import repository.LibroRepositoryImpl;

import java.sql.SQLException;
import java.util.*;

/*
    gestoreLibreria è quello che nel contesto del pattern OBSERVER
    rapprensenta la concreteSubject, che contiene lo stato
    a cui gli oggetti che osservano, quindi osservatori sono interessati
    quando modifica lo stato invia una notifica
 */

public class GestoreLibreria implements Subject{

    private static GestoreLibreria instance;
    private List<Observer> osservatori;
    private LibroRepository libroRepository;

    private GestoreLibreria(LibroRepository repository){
        osservatori = new ArrayList<>();
        this.libroRepository = repository;
    }

    public static synchronized GestoreLibreria getInstance(LibroRepository repository){
        if(instance == null ){
            instance = new GestoreLibreria(repository);
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
        libroRepository.inserisciLibro(libro);
        notifyObserver(); // Notifica solo se l'inserimento è riuscito
    }

    public void rimuoviLibro(Libro libro) throws SQLException {
        libroRepository.eliminaLibro(libro);
        notifyObserver();
    }

    public List<Libro> restituisciLibri() throws SQLException{
        return libroRepository.tuttiLibri();
    }

    public void aggiornaLibro(Libro libro) throws SQLException{
        libroRepository.modificaLibro(libro);
        notifyObserver();
    }


    //metodo per la ricerca
    public List<Libro> cercaLibri(String terminaRicerca) throws SQLException{
        if (terminaRicerca == null || terminaRicerca.trim().isEmpty()){
            return libroRepository.tuttiLibri();
        }
        return libroRepository.cercaLibri(terminaRicerca);
    }

    public List<String> getAutoriDisponibili() throws SQLException{
        return libroRepository.getAutoriDistinti();
    }

    public List<String> getGeneriDisponibili() throws SQLException{
        return libroRepository.getGeneriDistinti();
    }

    // Metodo per applicare un filtro (da chiamare dal listener del JMenuItem)
    public List<Libro> getLibriFiltrati(String filterType, String filterValue) throws SQLException {

        switch (filterType) {
            case "AUTORE":
                return libroRepository.cercaLibriByAutore(filterValue);
            case "GENERE":
                return libroRepository.cercaLibriByGenere(filterValue);
            case "VALUTAZIONE":

                return libroRepository.cercaLibriByValutazione(Integer.parseInt(filterValue));
            default:
                return libroRepository.tuttiLibri();
        }
    }

    //per l'ordinamento
    public List<Libro> getLibriOrdini(String criterio, String direzione) throws SQLException {
        return libroRepository.libriOrdinati(criterio, direzione);
    }

}