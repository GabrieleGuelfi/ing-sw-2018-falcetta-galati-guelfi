package it.polimi.se2018.events;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.view.View;

import java.io.Serializable;

/**
 * @author Federico Galati
 *
 */
public class Message implements Serializable  {
    String s;

    public Message(String string){

        s = string;
    }

    public String getString() {
        return s;
    }

    public void notifyThis(View v) {
        v.notifyObservers(this);
    }

}
