package it.polimi.se2018.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestBox {

    @Test
    public void testConstructorPositive() {
        Box box = new Box(3, Colour.BLUE);
        assertEquals(3, box.getValueRestriction());
        assertEquals(Colour.BLUE, box.getColourRestriction());
        assertEquals(false, box.hasNoValueRestriction());
    }

    @Test
    public void testConstructorNumberPositive() {
        Box box = new Box(5);
        assertEquals(5, box.getValueRestriction());
        assertEquals(Colour.WHITE, box.getColourRestriction());
        assertEquals(true, box.hasNoValueRestriction());
    }

    @Test
    public void testConstructorColourPositive() {
        Box box = new Box(Colour.YELLOW);
        assertEquals(0, box.getValueRestriction());
        assertEquals(Colour.YELLOW, box.getColourRestriction());
    }

    @Test
    public void testConstructorNumberNegativeValueInf() {
        try {
            Box box = new Box(-3);
        }
        catch(IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void testConstructorNumberNegativeValueSup() {
        try {
            Box box = new Box(11);
        }
        catch(IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void testConstructorNegativeValueInf() {
        try {
            Box box = new Box(-1, Colour.BLUE);
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

    @Test
    public void testCopy() {
        Box box = new Box(4, Colour.BLUE);
        Box box1=null;

        Die die = new Die(Colour.BLUE);
        die.setRandomValue();

        box.setDie(die);
        box1 = box.copy();

        assertNotEquals(box, box1);

        assertEquals(box.getColourRestriction(), box1.getColourRestriction());
        assertEquals(box.getValueRestriction(), box1.getValueRestriction());

        assertEquals(box.getDie().getValue(), box1.getDie().getValue());
        assertEquals(box.getDie().getColour(), box1.getDie().getColour());




    }
}