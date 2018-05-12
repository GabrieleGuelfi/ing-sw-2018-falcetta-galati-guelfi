package it.polimi.se2018.model.dicecollection;

import it.polimi.se2018.model.Die;

import java.security.InvalidParameterException;
import java.util.Random;

// Shouldn't the random part go in controller?

public class DraftPool extends DiceCollection {

    public DraftPool() {
        super();
    }

    public DraftPool(Bag bag, int playersNumber) {

        super();

        if (playersNumber<1 || playersNumber>4) {
            throw new InvalidParameterException("Invalid number of players!");
        }

        if (bag==null) {
            throw new InvalidParameterException("Invalid bag!");
        }

        if (bag.size()<2*playersNumber+1) {
            throw new InvalidParameterException("Bag too small!");
        }

        Random generator = new Random();
        for(int i =0; i<(2*playersNumber+1); i++) {
            Die die = bag.removeDie(generator.nextInt(bag.size()));
            addDie(die);
        }
    }

    public DraftPool copy() {
        DraftPool d = new DraftPool();
        for(Die x : this.bag){

            d.bag.add(x.copy());
        }
        return d;
    }
}
