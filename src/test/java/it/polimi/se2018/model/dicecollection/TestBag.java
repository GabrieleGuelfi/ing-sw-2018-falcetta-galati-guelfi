package it.polimi.se2018.model.dicecollection;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestBag {

    @Test
    public void testConstructor() {
        Bag bag = new Bag();
        assertEquals(0, bag.size());
    }

    @Test
    public void testPopulateBag() {

        final int sameColourDices = 18;

        int i;

        Bag bag;
        bag = new Bag();
        bag.populateBag();
        Die die;

        int redDice = 0;
        int blueDice = 0;
        int greenDice = 0;
        int purpleDice = 0;
        int yellowDice = 0;

        assertEquals(90, bag.size());

        for (i = 0; i < 90; i++) {
            die = bag.removeDie(0);
            if (die.getColour() == Colour.BLUE) blueDice++;
            if (die.getColour() == Colour.GREEN) greenDice++;
            if (die.getColour() == Colour.RED) redDice++;
            if (die.getColour() == Colour.YELLOW) yellowDice++;
            if (die.getColour() == Colour.PURPLE) purpleDice++;
        }

        assertEquals(sameColourDices, greenDice);
        assertEquals(sameColourDices, blueDice);
        assertEquals(sameColourDices, redDice);
        assertEquals(sameColourDices, yellowDice);
        assertEquals(sameColourDices, purpleDice);

        assertEquals(0, bag.size());

    }

    @Test
    public void testCopy() {
        int i;

        Bag bag = new Bag();
        bag.populateBag();

        Bag bag1 = bag.copy();

        assertNotEquals(bag, bag1);
        assertEquals(bag.size(), bag1.size());

        for(i=0; i<89; i++) {
            Die tempDie = bag.removeDie(0);
            Die tempDie2 = bag1.removeDie(0);
            assertEquals(tempDie.getColour(), tempDie2.getColour());
            assertEquals(tempDie.getValue(), tempDie2.getValue());
            assertNotEquals(tempDie, tempDie2);
        }

        assertEquals(bag.size(), bag1.size());

    }
}