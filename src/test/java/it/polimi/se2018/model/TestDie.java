package it.polimi.se2018.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class TestDie {

    @Test
    public void testConstructorPositive() {

        Die die;

        for(Colour c: Colour.values()) {
            die = new Die(c);

            assertEquals(c, die.getColour());

            assertEquals(-1, die.getValue());

        }


    }


    @Test
    public void testSetValuePositive() {

        Die die = new Die(Colour.BLUE);
        assertEquals(-1, die.getValue());

        int i;
        for(i=1; i<7; i++) {
            die.setValue(i);
            assertEquals(i, die.getValue());
        }

    }

    @Test
    public void testSetValueNegativeInf() {
        Die die = new Die(Colour.BLUE);

        try {
            die.setValue(0);
        }
        catch(IllegalArgumentException e) {
            return;
        }

        fail();
    }

    @Test
    public void testSetValueNegativeSup() {
        Die die = new Die(Colour.BLUE);

        try {
            die.setValue(7);
        }
        catch(IllegalArgumentException e) {
            return;
        }

        fail();
    }



    @Test
    public void testRandomValue() {
        Die die = new Die(Colour.BLUE);

        die.setRandomValue();

        if (die.getValue()<1 || die.getValue()>6) {
            fail();
        }
    }

    @Test
    public void testCopy(){
        Die die = new Die(Colour.BLUE);
        Die die2;

        die2=die.copy();

        assertEquals(die.getColour(), die2.getColour());

        assertEquals(die.getValue(), die2.getValue());

        assertNotEquals(die, die2);

    }

    @Test
    public void testIsPlacing() {
        Die die = new Die(Colour.BLUE);

        assertEquals(false, die.isPlacing());

        die.setPlacing(true);
        assertEquals(true, die.isPlacing());

        die.setPlacing(false);
        assertEquals(false, die.isPlacing());
    }
}