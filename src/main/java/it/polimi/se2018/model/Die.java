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

    public void setValue(int n) {
        if ((n < 1) || (n > 6)) {
            throw new IllegalArgumentException("Value not allowed!");
        }
        value = n;
        }

    public void setRandomValue() { value = generator.nextInt(6) + 1;}

    public Die copy(){
        Die d = new Die(this.colour);
        d.value = this.value;
        return d;
    }

}
