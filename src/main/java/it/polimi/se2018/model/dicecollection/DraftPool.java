package it.polimi.se2018.model.dicecollection;

import it.polimi.se2018.model.Die;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Random;

// Shouldn't the random part go in controller?

/**
 * This class represents the "Draft pool", aka the group of 5-7-9 dice extracted from the bag in the begin of a round.
 * @author Alessandro Falcetta
 */
public class DraftPool extends DiceCollection implements Serializable {

    /**
     * Basic constructor, used by copy()
     */
    public DraftPool() {
        super();
    }

    /**
     * Constructor for building a draft pool, starting from a bag
     * @param bag Bag to extract dice from
     * @param playersNumber number of players
     * @throws InvalidParameterException if: player number out of range, bag is null or too small
     */
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
            die.setRandomValue();
            addDie(die);
        }
    }

    /**
     * Copy method
     * @return Draftpool with a copy of all the dice of this, in a different object
     */
    @Override
    public DraftPool copy() {
        DraftPool d = new DraftPool();
        for(Die x : this.bag){

            d.bag.add(x.copy());
        }
        return d;
    }
}
