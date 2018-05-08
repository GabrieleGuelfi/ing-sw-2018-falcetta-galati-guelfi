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

    public Die removeDice(int position) {
        return this.bag.remove(position);
    }

    public int size() {
        return bag.size();
    }

}
