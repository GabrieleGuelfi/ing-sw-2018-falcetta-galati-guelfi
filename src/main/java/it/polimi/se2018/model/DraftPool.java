package it.polimi.se2018.model;

import java.security.InvalidParameterException;
import java.util.Random;

// Shouldn't the random part go in controller?

public class DraftPool extends DiceCollection {

    public DraftPool(Bag bag, int playersNumber) {

        super();

        if (playersNumber<1 || playersNumber>4) {
            throw new InvalidParameterException("Invalid number of players!");
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
}
