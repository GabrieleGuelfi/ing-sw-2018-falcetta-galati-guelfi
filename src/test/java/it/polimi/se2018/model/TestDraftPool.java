package it.polimi.se2018.model;

import org.junit.Test;

import java.security.InvalidParameterException;

import static org.junit.Assert.*;

public class TestDraftPool {

    private final int PLAYERS_NUMBER=4;

    @Test
    public void testConstructor() {
        final int SAME_COLOUR_DICE=18;
        int i;
        int blueDice=0;
        int greenDice=0;
        int redDice=0;
        int purpleDice=0;
        int yellowDice=0;

        Bag bag = new Bag();
        assertEquals(90, bag.size());
        Die die;

        DraftPool draftPool = new DraftPool(bag, PLAYERS_NUMBER);

        assertEquals(81, bag.size());
        assertEquals(9, draftPool.size());

        for(i=0; i<9; i++) {
            die = draftPool.removeDie(0);
            if (die.getColour()==Colour.BLUE) blueDice++;
            if (die.getColour()==Colour.GREEN) greenDice++;
            if (die.getColour()==Colour.YELLOW) yellowDice++;
            if (die.getColour()==Colour.RED) redDice++;
            if (die.getColour()==Colour.PURPLE) purpleDice++;
        }

        assertEquals(0, draftPool.size());

        for(i=0; i<81; i++) {
            die = bag.removeDie(0);
            if (die.getColour()==Colour.BLUE) blueDice++;
            if (die.getColour()==Colour.GREEN) greenDice++;
            if (die.getColour()==Colour.YELLOW) yellowDice++;
            if (die.getColour()==Colour.RED) redDice++;
            if (die.getColour()==Colour.PURPLE) purpleDice++;
        }

        assertEquals(0, bag.size());

        assertEquals(SAME_COLOUR_DICE, blueDice);
        assertEquals(SAME_COLOUR_DICE, yellowDice);
        assertEquals(SAME_COLOUR_DICE, greenDice);
        assertEquals(SAME_COLOUR_DICE, redDice);
        assertEquals(SAME_COLOUR_DICE, purpleDice);
    }

    @Test
    public void testConstructorNegativeBag() {
        Bag bag = new Bag();
        DraftPool draftPool;
        int i;

        for(i=0; i<90; i++) {
            bag.removeDie(0);
        }

        try {
            draftPool = new DraftPool(bag, PLAYERS_NUMBER);
        }
        catch(InvalidParameterException e) {
            return;
        }

        fail();
    }

    @Test public void testConstructorNullBag() {
        Bag bag=null;
        DraftPool draftPool;

        try {
            draftPool = new DraftPool(bag, PLAYERS_NUMBER);
        }
        catch(InvalidParameterException e) {
            return;
        }

        fail();
    }

    @Test
    public void testConstructorNegativePlayersNumberInf() {
        Bag bag = new Bag();
        DraftPool draftPool;

        try {
            draftPool = new DraftPool(bag, 0);
        }
        catch(InvalidParameterException e) {
            return;
        }

        fail();
    }

    @Test
    public void testConstructorNegativePlayersNumberSup() {
        Bag bag = new Bag();
        DraftPool draftPool;

        try {
            draftPool = new DraftPool(bag, 5);
        }
        catch(InvalidParameterException e) {
            return;
        }

        fail();
    }
}