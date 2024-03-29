package it.polimi.se2018.utils;

import it.polimi.se2018.events.Message;

import java.util.ArrayList;
import java.util.List;

public class Observable{

    private final List<Observer> observers = new ArrayList<>();

    public void register(Observer observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void deregister(Observer observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    public void notifyObservers(Message m){
        synchronized (observers) {
            for(Observer observer : observers){
                observer.update(m);
            }
        }
    }

}
