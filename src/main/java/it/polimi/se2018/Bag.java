package it.polimi.se2018;

public class Bag extends DiceCollection {

    final int sameColorDices = 18;

    public void Bag() {

        int i;

        for (i=0; i<sameColorDices; i++) {
            addDiceOfColor('Red');
            addDiceOfColor('Blue');
            addDiceOfColor('Green');
            addDiceOfColor('Yellow');
            addDiceOfColor('Purple');
        }
    }

    private void addDiceOfColor(Color color) { // EXTRA-UML
        this.bag.add(new Dice(color));
    }

}
