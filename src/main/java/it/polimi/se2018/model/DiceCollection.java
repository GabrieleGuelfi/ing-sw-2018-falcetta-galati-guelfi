package it.polimi.se2018.model;

import java.util.*;

public class DiceCollection {

    private ArrayList<Die> bag;

    public DiceCollection() {
        this.bag = new ArrayList<>();
    }

    public void addDie(Die die) {
        this.bag.add(die);
    }

    public Die removeDie(int position) {
        return this.bag.remove(position);
    }

    public int size() {
        return bag.size();
    }

    public DiceCollection copy(){
        DiceCollection d = new DiceCollection();
        for(Die x : this.bag){

            d.bag.add(x.copy());
        }
        return d;

    }

}
