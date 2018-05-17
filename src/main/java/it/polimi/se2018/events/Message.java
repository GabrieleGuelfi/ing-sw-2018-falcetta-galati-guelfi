package it.polimi.se2018.events;

import it.polimi.se2018.model.Player;

import java.io.Serializable;
import java.util.Observable;

/**
 * @author Federico Galati
 *
 */
public class Message implements Serializable {

    private Player player;

    public Message(Player p){

        this.player = p;
    }

    public Player getPlayer(){ return this.player;    }


}
