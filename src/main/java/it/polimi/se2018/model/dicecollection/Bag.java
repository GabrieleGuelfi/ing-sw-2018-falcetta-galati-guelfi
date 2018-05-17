package it.polimi.se2018.model.dicecollection;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;

/**
 * It implements the "Bag" of a game, aka the group of 90 dice (at the beginning).
 * @author Alessandro Falcetta
 */

public class Bag extends DiceCollection {

    private static final int SAME_COLOUR_DICE = 18;

    /**
     * Constructor; it doesn't populate the bag at the creation because it's much easier to write "copy()".
     */
    public Bag() {

        super();

    }

    /**
     * Method which fill the bag with 90 dice, divided in 5 colours (from model.Colour).
     * Constant SAME_COLOUR_DICE is set to 18.
     */
    public void populateBag() {

        int i;

        for (i=0; i<SAME_COLOUR_DICE; i++) {
            addDieOfColour(Colour.RED);
            addDieOfColour(Colour.BLUE);
            addDieOfColour(Colour.GREEN);
            addDieOfColour(Colour.YELLOW);
            addDieOfColour(Colour.PURPLE);
        }
    }

    /**
     * Creates a die with the given colour and adds it to the bag
     * @param colour of the new die
     */
    private void addDieOfColour(Colour colour) { // EXTRA-UML
        this.addDie(new Die(colour));
    }

    /**
     * Copy method for Bag
     * @return a bag with a copy of all the dice in this, but in a new object
     */
    @Override
    public Bag copy(){
        Bag b = new Bag();
        for(Die x : this.bag){

            b.bag.add(x.copy());
        }
        return b;

    }
}
