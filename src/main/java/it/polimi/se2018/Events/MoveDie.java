package it.polimi.se2018.Events;

import it.polimi.se2018.Die;
import it.polimi.se2018.Player;

public class MoveDie {

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
