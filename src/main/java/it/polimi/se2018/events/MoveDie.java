package it.polimi.se2018.events;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Player;

public class MoveDie {

    private final Player player;

    private final int row;

    private final int column;

    private Die die;


      public MoveDie(Player player, int row, int column, Die die) {
        this.player = player;
        this.row = row;
        this.column = column;
        this.die = die;
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
