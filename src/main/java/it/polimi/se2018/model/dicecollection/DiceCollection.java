package it.polimi.se2018.model.dicecollection;

import it.polimi.se2018.model.Die;

import java.util.*;

/**
 * @author Alessandro Falcetta
 *
 * Superclass for all the collections of dice.
 *
 */

public class DiceCollection {

    protected ArrayList<Die> bag;

    /**
     * Constructor
     */
    public DiceCollection() {
        this.bag = new ArrayList<>();
    }

    /**
     * Adds a die
     * @param die Die to be added
     */
    public void addDie(Die die) {
        this.bag.add(die);
    }

    /**
     * Removes a die, given the position in the arraylist
     * @param position Position to be removed
     * @return The die in that position
     */
    public Die removeDie(int position) {
        return this.bag.remove(position);
    }

    /**
     * Getter for size of the bag
     * @return size of the bag
     */

    public int size() {
        return bag.size();
    }

    /**
     * Copy method
     * @return a DiceCollection with all the dice contained in this, but in a different object.
     */
    public DiceCollection copy(){
        DiceCollection d = new DiceCollection();
        for(Die x : this.bag){

            d.bag.add(x.copy());
        }
        return d;

    }

}
