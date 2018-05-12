package it.polimi.se2018.events;

import it.polimi.se2018.model.*;

import java.util.Observable;

public class MoveDie extends Observable{

    private final Player player;

    private final int row;

    private final int column;

    private Die die;


      public MoveDie(Player player, int row, int column) {
        this.player = player;
        this.row = row;
        this.column = column;
     }


    public Player getPlayer() {
        return player;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Die getDie() {
        return die;
    }
}
