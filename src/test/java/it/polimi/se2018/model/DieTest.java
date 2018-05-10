package it.polimi.se2018.model;

import static it.polimi.se2018.model.Colour.BLUE;

import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DieTest {

    @Test
    public void testConstructorPositive() {

        Die die = null;
        try {
            die = new Die(Colour.BLUE); }
        catch(IllegalArgumentException e) {
            fail("Illegal argument");
        }

        assertEquals(Colour.BLUE, die.getColour());

        assertEquals(-1, die.getValue());

        }
    @Test
    public void testSetValuePositive() {

        Die die = new Die(Colour.BLUE);
        assertEquals(-1, die.getValue());

        die.setValue(1);
        assertEquals(1, die.getValue());

        die.setValue(2);
        assertEquals(2, die.getValue());

        die.setValue(3);
        assertEquals(3, die.getValue());

        die.setValue(4);
        assertEquals(4, die.getValue());

        die.setValue(5);
        assertEquals(5, die.getValue());

        die.setValue(6);
        assertEquals(6, die.getValue());

    }

    @Test
    public void testSetValueNegative() {
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
    public void testRandomValue() {
        Die die = new Die(Colour.BLUE);

        die.setRandomValue();

        if (die.getValue()<1 || die.getValue()>6) {
            fail();
        }
    }
}