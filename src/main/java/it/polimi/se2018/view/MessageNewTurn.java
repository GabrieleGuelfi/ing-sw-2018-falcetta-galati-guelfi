package it.polimi.se2018.view;

import it.polimi.se2018.model.Player;

import java.util.Observable;

public class MessageNewTurn extends Observable{

    public MessageNewTurn(Player p, View v){

        this.addObserver(v);
        //IS CORRECT WRITTEN LIKE THIS?
        this.notifyObservers(p);
    }
}
