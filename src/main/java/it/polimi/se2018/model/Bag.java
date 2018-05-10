package it.polimi.se2018.model;

public class Bag extends DiceCollection {

    final int sameColourDices = 18;

    public Bag() {

        super();

        int i;

        for (i=0; i<sameColourDices; i++) {
            addDiceOfColour(Colour.RED);
            addDiceOfColour(Colour.BLUE);
            addDiceOfColour(Colour.GREEN);
            addDiceOfColour(Colour.YELLOW);
            addDiceOfColour(Colour.PURPLE);
        }
    }

    private void addDiceOfColour(Colour colour) { // EXTRA-UML
        this.addDice(new Die(colour));
    }

}
