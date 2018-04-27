package it.polimi.se2018;

import java.util.*;

public class DiceCollection {

    private ArrayList<Die> bag;

    public DiceCollection() {
        this.bag = new ArrayList<Die>();
    }

    public void addDice(Die die) {
        this.bag.add(die);
    }

    public void removeDice(Die die) {
        int position = this.bag.indexOf(die);
        this.bag.remove(position);
    }

    /*public Die extractRandomDice() {
        // EXTRA-UML
    } */

}
