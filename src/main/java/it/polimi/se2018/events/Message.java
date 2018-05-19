package it.polimi.se2018.events;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.SagradaVisitor;
import it.polimi.se2018.view.VirtualView;

import java.io.Serializable;

/**
 * @author Federico Galati
 *
 */
public class Message implements Serializable  {
    String s;
    Player player;

    public Message(String string){

        s = string;
    }

    public Message(Player p){
        this.player = p;
    }

    public Message(String string, Player p){
        this.s = string;
        this.player = p;
    }

    public String getString() {
        return s;
    }

    public void notifyThis(VirtualView v) {
        v.notifyObservers(this);
    }

    public Player getPlayer(){return this.player;}

    public void accept(SagradaVisitor visitor){
        visitor.visit(this);
    }

}
