package it.polimi.se2018.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestWindowPattern {

    @Test
    public void testConstructorPositive() {
        WindowPattern w = new WindowPattern("foo", 5);
        assertEquals("foo", w.getName());
        assertEquals(5, w.getDifficulty());
        assertEquals(20, w.getEmptyBox());
    }

    @Test
    public void testDecreaseEmptyBox() {
        WindowPattern w = new WindowPattern("foo", 5);
        w.putDice(new Die(Colour.WHITE), 0, 1);
        w.putDice(new Die(Colour.RED), 0, 1);
        w.putDice(new Die(Colour.GREEN), 0, 1);
        assertEquals(17, w.getEmptyBox());

        w.addEmptyBox();
        assertEquals(18, w.getEmptyBox());
    }

    @Test
    public void testGetBox() {
        WindowPattern w = new WindowPattern("foo", 5);
        Box b = new Box(Colour.WHITE);
        w.setBox(b, 3, 2);
        assertEquals(b, w.getBox(3, 2));
        b = new Box(3);
        w.setBox(b, 1, 3);
        assertEquals(b, w.getBox(1, 3));
    }

    @Test
    public void testCopy() {
        WindowPattern w = new WindowPattern("foo", 5);
        WindowPattern w1 = w.copy();
        assertEquals(w.getName(), w1.getName());
        assertEquals(w.getDifficulty(), w1.getDifficulty());
        assertEquals(w.getEmptyBox(), w.getEmptyBox());
    }
}
