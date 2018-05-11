package it.polimi.se2018.model;

public class Box {

    private int valueRestriction;
    private Colour colourRestriction;
    private Die die;

    public Box(int n, Colour c){

        valueRestriction = n; //exceptions??
        colourRestriction = c; //exceptions??

    }

    public int getValueRestriction(){ return valueRestriction;}

    public Colour getColourRestriction() { return colourRestriction;}

    public void setDie(Die d){
        die = d;
    }

    public Die getDie(){ return die;} //exceptions??

}
