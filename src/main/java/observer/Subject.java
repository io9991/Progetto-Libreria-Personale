package observer;

//questa è l'interfaccia Subject alla quale si attengono gli oggetti osservati

import java.sql.SQLException;

public interface Subject {

    //i metodi sono quelli di attach e detach dell'obsercer, difatti mantiene un riferimento a questo
    //attach(:Observer)
    //detach(:Observer)
    //e poi serve il metodo notify che serve per notificare dei cambiamenti

    void attach(Observer o);

    void detach(Observer o);

    void notifyObserver() throws SQLException;


}
