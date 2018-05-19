package it.polimi.se2018.model;

public class Box {

    private int valueRestriction;
    private Colour colourRestriction;
    private Die die;

    public Box(Colour c) {
        colourRestriction = c;
        valueRestriction = 0;
    }

    public Box(int n) {
        if(n<1 || n>6) throw new IllegalArgumentException("Invalid value restriction!");
        valueRestriction = n;
        colourRestriction = Colour.WHITE;
    }

    // Maybe this can be removed.
    public Box(int n, Colour c){

        if(n<0 || n>6) throw new IllegalArgumentException("Invalid value restriction!");
        valueRestriction = n;

        colourRestriction = c;

    }

    public int getValueRestriction(){ return valueRestriction;}

    public Colour getColourRestriction() { return colourRestriction;}

    public void setDie(Die d){
        if (die == null) //else throw exception? verify this here or in another class(tool can move die in pattern)?
            die = d;
    }

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
