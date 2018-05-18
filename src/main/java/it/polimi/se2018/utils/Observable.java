package it.polimi.se2018.utils;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageDie;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {

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

    public void notifyObservers(MessageDie message){
        synchronized (observers) {
            for(Observer observer : observers){
                observer.update(message);
            }
        }
    }
}
