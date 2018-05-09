package it.polimi.se2018;

public class Box {

    private int valueRestriction;
    private Colour colourRestriction;
    private Die die;

    public Box(int n, Colour c){

        valueRestriction = n; //Exception??
        colourRestriction = c; //Exception??

    }

    public int getValueRestriction(){ return valueRestriction;}

    public Colour getColourRestriction() { return colourRestriction;}

    public void setDie(Die d){
        die = d;
    }

    public Die getDie(){ return die;} //Exception??

}
