package it.polimi.se2018.events;

import it.polimi.se2018.model.Player;

import java.util.Observable;

/**
 * @author Federico Galati
 *
 */
public class Message {
    private TypeMessage typeMessage;
    private Player player;

    public Message(TypeMessage type, Player p){
        this.typeMessage = type;
        this.player = p;
    }

    public Player getPlayer(){ return this.player;    }

    public TypeMessage getTypeMessage() {
        return typeMessage;
    }

}
