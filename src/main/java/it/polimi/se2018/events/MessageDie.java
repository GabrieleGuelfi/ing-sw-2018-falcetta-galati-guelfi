package it.polimi.se2018.events;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.view.View;

import java.io.Serializable;
import java.util.Observable;

public class MessageDie extends Message implements Serializable {

    static final long serialVersionUID = 42L;

    private Die die;

    public MessageDie(Die die) {
        super("Messaggio die");
        this.die = die;
    }

    public Die getDie() { return die; }

    public void notifyThis(View v) {
        v.notifyObservers(this);
    }
}
