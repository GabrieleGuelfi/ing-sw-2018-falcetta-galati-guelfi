package it.polimi.se2018;

public class Die {

    private Colour colour;
    private int value;

    public Die(int n, Colour c){
        colour = c;
        value = n;
    }

    public int getValue(){ return value;  }
    public Colour getColour(){ return colour;}
}
