package it.polimi.se2018.View;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.view.View;

import java.util.Observable;

public class MessageNewTurn extends Observable{

    private Player player;

    public MessageNewTurn(Player p, View v){

        player = p;
        this.addObserver(v);
        //IS CORRECT WRITTEN LIKE THIS?
        this.notifyObservers(player);
    }
}
