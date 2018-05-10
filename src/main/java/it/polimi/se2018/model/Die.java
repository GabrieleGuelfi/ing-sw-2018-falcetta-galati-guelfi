package it.polimi.se2018.model;


import java.util.Random;



public class Die {

    private Colour colour;
    private int value;
    private Random generator;

    public Die(Colour c){
        colour = c;
        value = -1;
        generator = new Random();
    }

    public int getValue(){ return value;  }
    public Colour getColour(){ return colour;}
    public void setValue(int n) {value = n;}
    public void setRandomValue() { value = generator.nextInt(6) + 1;}

}
