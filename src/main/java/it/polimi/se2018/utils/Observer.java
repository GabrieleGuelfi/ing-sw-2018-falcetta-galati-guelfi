package it.polimi.se2018.utils;

public interface Observer<T> {

    void update(T m);

    //void update(Message m);

}
