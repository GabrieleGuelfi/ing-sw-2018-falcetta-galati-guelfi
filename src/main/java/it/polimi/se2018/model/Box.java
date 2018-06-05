package it.polimi.se2018.model;

import java.io.Serializable;

/**
 * is a Boc of a window pattern, with colour or value restriction
 */
public class Box implements Serializable {

    private int valueRestriction;
    private Colour colourRestriction;
    private Die die;

    /**
     * create a Box with colour restriction c and no value restriction
     * @param c is the colour restriction
     */
    public Box(Colour c) {
        colourRestriction = c;
        valueRestriction = 0;
    }

    /**
     * create a Box with value restriction n and no colour restriction
     * @param n is the value restriction
     */
    public Box(int n) {
        if(n<0 || n>6) throw new IllegalArgumentException("Invalid value restriction!");
        valueRestriction = n;
        colourRestriction = Colour.WHITE;
    }

    // Maybe this can be removed.
    public Box(int n, Colour c){
        if(n<0 || n>6) throw new IllegalArgumentException("Invalid value restriction!");
        valueRestriction = n;
        colourRestriction = c;
        die=null;
    }

    /**
     * @return the value restriction of this box
     */
    public int getValueRestriction(){ return valueRestriction;}

    /**
     * @return the colour restriction of this box
     */
    public Colour getColourRestriction() { return colourRestriction;}

    public boolean hasAValueRestriction() {
        return (this.colourRestriction==Colour.WHITE);
    }

    /**
     * place a die in this box
     * @param d is the die to place in this box
     */
    public void setDie(Die d){
        if (die == null) //else throw exception? verify this here or in another class(tool can move die in pattern)?
            die = d;
    }


    /**
     * @return the die placed on this box
     */
    public Die getDie(){ return die;}

    /**
     *
     * @return Copy of this Object.
     */
    public Box copy(){
        Box b = new Box(this.valueRestriction, this.colourRestriction);
        if(this.die!=null) b.die = this.die.copy();
        return b;
    }

}
