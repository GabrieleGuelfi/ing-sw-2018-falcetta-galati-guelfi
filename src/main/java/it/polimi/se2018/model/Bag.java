package it.polimi.se2018.model;

public class Bag extends DiceCollection {

    final int sameColourDices = 18;

    public Bag() {

        super();

        int i;

        for (i=0; i<sameColourDices; i++) {
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
