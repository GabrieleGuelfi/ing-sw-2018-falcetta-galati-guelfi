package it.polimi.se2018.events;

import it.polimi.se2018.model.Player;

import java.util.Observable;

/**
 * @Author Federico Galati
 *
 */
public class Message extends Observable {
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
