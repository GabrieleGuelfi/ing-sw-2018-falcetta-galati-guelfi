package it.polimi.se2018.model;

public class Bag extends DiceCollection {

    private static final int SAME_COLOUR_DICE = 18;

    public Bag() {

        super();

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

}
