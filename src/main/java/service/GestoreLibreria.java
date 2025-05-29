package service;


import builder.Libro;
import db.MyJavaDBC;
import observer.Observer;
import observer.Subject;
import java.util.*;

//qui viene inserito il concreteSubject che quindi va a realizzare l'interfaccia subject
public class GestoreLibreria implements Subject{

    //piccola deviazione rispetto al diagramma di classe generale

    //miaIstanza
    private static GestoreLibreria instance;
    //lista di osservatori
    private List<Observer> osservatori;
    //riferimento alla connessione
    private MyJavaDBC dataBaseConnection;

    public GestoreLibreria(){
        osservatori = new ArrayList<>();
        //connessione al db
        dataBaseConnection = MyJavaDBC.getInstance();
    }

    public static synchronized GestoreLibreria getInstance(){
        if(instance == null ){
            instance = new GestoreLibreria();
        }
        return instance;
    }


    @Override
    public void attach(Observer o) {
        //se non contiene l'osservatore lo aggiungiamo
        if(!osservatori.contains(o)){
            osservatori.add(o);
        }
    }

    @Override
    public void detach(Observer o) {
        //rimozione dell'osservatore
        if(osservatori.contains(o)){
            osservatori.remove(o);
        }
    }

    @Override
    public void notifyObserver() {
        //creo una copia per evitare ConcurrentModificationException
        List<Observer> osservatoriCopia = new ArrayList<>(this.osservatori);
        for (Observer o : osservatoriCopia){
            //a tutti gli osservatori andiamo a fare l'update
            o.update();
        }
    }


    public void aggiungiLibro(Libro libro){
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void rimuoviLibro(Libro libro){

    }

    public List<Libro> restituisciLibri(){

    }

    //todo modifica libro

    //update db
    public void aggiornaLibro(Libro libro){
        try {

        }catch(Exception e){

        }
    }



}


