package it.polimi.se2018.model;

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
}