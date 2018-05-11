package it.polimi.se2018.model;

import java.util.Random;

// Shouldn't the random part go in controller?

public class DraftPool extends DiceCollection {

    public DraftPool(Bag bag, int n) {
        super();

        Random generator = new Random();
        for(int i =0; i<(2*n+1); i++) {
            Die die = bag.removeDice(generator.nextInt(bag.size()));
            addDice(die);
        }
    }
}
