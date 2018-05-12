package it.polimi.se2018.model;

import it.polimi.se2018.model.dicecollection.DiceCollection;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDiceCollection {

    @Test
    public void testConstructor() {
        DiceCollection dice = new DiceCollection();
        assertEquals(0, dice.size());
    }

    @Test
    public void testAddDie() {
        DiceCollection dice = new DiceCollection();

        assertEquals(0, dice.size());

        Die die = new Die(Colour.BLUE);
        dice.addDie(die);

        assertEquals(1, dice.size());

    }

    @Test
    public void testRemoveDie() {
        DiceCollection dice = new DiceCollection();

        assertEquals(0, dice.size());

        Die die = new Die(Colour.BLUE);
        dice.addDie(die);

        assertEquals(1, dice.size());

        Die die2 = dice.removeDie(0);

        assertEquals(die, die2);
    }

    @Test
    public void testSize() {
        DiceCollection dice = new DiceCollection();
        int i;
        Die die;

        for(i=1; i<100; i++){
            die = new Die(Colour.BLUE);
            dice.addDie(die);
            assertEquals(i, dice.size());
        }

        for(i=99; i>0; i--) {
            assertEquals(i, dice.size());
            dice.removeDie(0);
        }
    }

    @Test
    public void testCopy() {
        DiceCollection dc = new DiceCollection();
        DiceCollection dc1 = null;

        Die die1 = new Die(Colour.BLUE);
        Die die2 = new Die(Colour.GREEN);
        Die die3 = new Die(Colour.PURPLE);

        die1.setRandomValue();
        die2.setRandomValue();
        die3.setRandomValue();

        dc.addDie(die1);
        dc.addDie(die2);
        dc.addDie(die3);

        dc1 = dc.copy();

        assertNotEquals(dc, dc1);
        assertEquals(dc.size(), dc1.size());

        for(int i=0; i<3; i++) {
            Die temp = dc.removeDie(0);
            Die temp2 = dc1.removeDie(0);
            assertEquals(temp.getColour(), temp2.getColour());
            assertEquals(temp.getValue(), temp2.getValue());
            assertNotEquals(temp, temp2);
        }

        assertEquals(dc.size(), dc1.size());

    }
}