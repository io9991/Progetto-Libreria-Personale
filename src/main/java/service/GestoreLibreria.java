package service;

import observer.Observer;
import observer.Subject;

//qui viene inserito il concreteSubject che quindi va a realizzare l'interfaccia subject
public class GestoreLibreria implements Subject{

    //piccola deviazione rispetto al diagra

    public GestoreLibreria(){

    }


    @Override
    public void attach(Observer o) {

    }

    @Override
    public void detach(Observer o) {

    }

    @Override
    public void notifyObserver() {

    }
}
