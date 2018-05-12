package it.polimi.se2018.model;

public class Box {

    private int valueRestriction;
    private Colour colourRestriction;
    private Die die;

    public Box(int n, Colour c){

        if(n<1 || n>6) throw new IllegalArgumentException("Invalid value restriction!");
        valueRestriction = n;

        colourRestriction = c;

    }

    public int getValueRestriction(){ return valueRestriction;}

    public Colour getColourRestriction() { return colourRestriction;}

    public void setDie(Die d){
        die = d;
    }

    public Die getDie(){ return die;}

    /**
     *
     * @return Copy of this Object.
     */
    public Box copy(){

        Box b = new Box(this.valueRestriction, this.colourRestriction);
        b.die = this.die.copy();
        return b;
    }

}
