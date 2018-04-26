package it.polimi.se2018;

import java.util.*;

public class DiceCollection {

    private ArrayList<Dice> bag;

    public DiceCollection() {
        this.bag = new ArrayList<Dice>;
    }

    public void addDice(Dice dice) {
        this.bag.add(Dice);
    }

    public void removeDice(Dice dice) {
        int position = this.bag.indexOf(Dice);
        this.bag.remove(position);
    }

    public Dice extractRandomDice() {
        // EXTRA-UML
    }

}
