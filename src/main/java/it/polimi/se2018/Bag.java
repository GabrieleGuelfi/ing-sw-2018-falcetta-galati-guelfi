package it.polimi.se2018;

public class Bag extends DiceCollection {

    final int sameColourDices = 18;

    public void Bag() {

        int i;

        for (i=0; i<sameColourDices; i++) {
            addDiceOfColour(Colour.RED);
            addDiceOfColour(Colour.BLUE);
            addDiceOfColour(Colour.GREEN);
            addDiceOfColour(Colour.YELLOW);
            addDiceOfColour(Colour.PURPLE);
        }
    }

    private void addDiceOfColour(Colour color) { // EXTRA-UML
        this.addDice(new Die(color));
    }

}
