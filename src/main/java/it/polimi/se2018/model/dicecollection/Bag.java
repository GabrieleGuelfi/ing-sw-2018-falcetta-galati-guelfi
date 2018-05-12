package it.polimi.se2018.model.dicecollection;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;

public class Bag extends DiceCollection {

    private static final int SAME_COLOUR_DICE = 18;

    public Bag() {

        super();

    }

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

    private void addDieOfColour(Colour colour) { // EXTRA-UML
        this.addDie(new Die(colour));
    }

    @Override
    public Bag copy(){
        Bag b = new Bag();
        for(Die x : this.bag){

            b.bag.add(x.copy());
        }
        return b;

    }
}
