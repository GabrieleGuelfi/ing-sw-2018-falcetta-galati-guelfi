package it.polimi.se2018.events;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Player;

import java.io.Serializable;
import java.util.Observable;

public class MessageDie implements Serializable {

    // THIS IS NOT WORKING THROUGH SOCKETS! Die has to implement serializable.

    static final long serialVersionUID = 42L;

    private Die die;

    public MessageDie(Die die) {


        this.die = die;
    }

    public Die getDie() { return die; }
}
