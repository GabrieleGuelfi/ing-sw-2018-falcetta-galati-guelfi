package it.polimi.se2018.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoxTest {

    @Test
    public void testConstructorPositive() {
        Box box = new Box(3, Colour.BLUE);
        assertEquals(3, box.getValueRestriction());
        assertEquals(Colour.BLUE, box.getColourRestriction());
    }

    @Test
    public void testConstructorNegativeValueInf() {
        try {
            Box box = new Box(0, Colour.BLUE);
        }
        catch(IllegalArgumentException e) {
            return;
        }
        fail();
    }
    @Test
    public void testConstructorNegativeValueSup() {
        try {
            Box box = new Box(7, Colour.BLUE);
        }
        catch(IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void testsetDie() {
        Box box = new Box(3, Colour.BLUE);
        Die die = new Die(Colour.BLUE);
        box.setDie(die);
        assertEquals(die, box.getDie());
    }

}