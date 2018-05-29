package it.polimi.se2018.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestColour {

    @Test
    public void testGetFirstLetter() {

        assertEquals('W', Colour.getFirstLetter(Colour.WHITE));
        assertEquals('B', Colour.getFirstLetter(Colour.BLUE));
        assertEquals('P', Colour.getFirstLetter(Colour.PURPLE));
        assertEquals('Y', Colour.getFirstLetter(Colour.YELLOW));
        assertEquals('R', Colour.getFirstLetter(Colour.RED));
        assertEquals('G', Colour.getFirstLetter(Colour.GREEN));

    }
}