package it.polimi.se2018.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestBag {

    @Test
    public void testConstructor() {

        final int sameColourDices = 18;

        int i;

        Bag bag;
        bag = new Bag();
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
}