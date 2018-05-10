package it.polimi.se2018.model;


import java.util.Random;



public class Die {

    private Colour colour;
    private int value;
    private Random generator;

    public Die(Colour c){
        if ((c!=Colour.BLUE) && (c!=Colour.PURPLE) && (c!=Colour.GREEN) && (c!=Colour.RED) && (c!=Colour.YELLOW )) {
           throw new IllegalArgumentException("Colour not supported!");
        }
        colour = c;
        value = -1;
        generator = new Random();
    }

    public int getValue(){ return value;  }
    public Colour getColour(){ return colour;}

    public void setValue(int n) {
        if ((n < 1) || (n > 6)) {
            throw new IllegalArgumentException("Value not allowed!");
        }
        value = n;
        }

    public void setRandomValue() { value = generator.nextInt(6) + 1;}

}
