package it.polimi.se2018;

import java.util.Random;

// Shouldn't the random part go in Controller?

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
