package it.polimi.se2018.model;

/**
 * Class Die
 * @author Federico Galati
 */


import java.util.Random;



public class Die {

    private Colour colour;
    private int value;
    private Random generator;

    /**
     * Costructor
     * @param c Colour of the new die
     */

    public Die(Colour c){
        colour = c;
        value = -1;
        generator = new Random();
    }

    /**
     * Returns the current value, if the die has been rolled
     * @return value
     */

    public int getValue(){ return value;  }

    /**
     * Returns the colour of the die
      * @return colour
     */
    public Colour getColour(){ return colour;}

    /**
     * Sets a given value to the die
     * @throws IllegalArgumentException if the value is not possible for a die
     * @param n value
     */

    public void setValue(int n) {
        if ((n < 1) || (n > 6)) {
            throw new IllegalArgumentException("Value not allowed!");
        }
        value = n;
        }

    /**
     * Rolls the die
     * Sets a random value between 1 and 6
    */

    public void setRandomValue() { value = generator.nextInt(6) + 1;}

    public Die copy(){
        Die d = new Die(this.colour);
        d.value = this.value;
        return d;
    }

}
