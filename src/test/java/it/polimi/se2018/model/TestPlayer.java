package it.polimi.se2018.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestPlayer {

    @Test
    public void testConstructor() {
        Player player = new Player("foo");
        assertEquals("foo", player.getNickname());
        assertEquals(0, player.getPoints());
    }

    @Test
    public void testIsUsedTool() {
        Player player = new Player("foo");
        assertFalse(player.isUsedTool());
        player.setUsedTool(true);
        assertTrue(player.isUsedTool());
    }

    @Test
    public void testIsPlacedDie() {
        Player player = new Player("foo");
        assertFalse(player.isPlacedDie());
        player.setPlacedDie(true);
        assertTrue(player.isPlacedDie());
    }

    @Test
    public void testSetWindowPatternPositive() {
        Player player = new Player("foo");
        WindowPattern windowPattern = new WindowPattern(10);
        assertNull(player.getWindowPattern());
        player.setWindowPattern(windowPattern);
        assertEquals(windowPattern, player.getWindowPattern());
        assertEquals(10, player.getWindowPattern().getDifficulty());
    }

    @Test
    public void testSetWindowPatternNegative() {
        Player player = new Player("foo");
        WindowPattern windowPattern = null;
        try {
            player.setWindowPattern(windowPattern);
        }
        catch(IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void testRemoveFavorTokensPositive() {
        Player player = new Player("foo");
        WindowPattern windowPattern = new WindowPattern(10);
        player.setWindowPattern(windowPattern);
        assertEquals(10, player.getFavorTokens());
        player.removeFavorTokens(5);
        assertEquals(5, player.getFavorTokens());
    }

    @Test
    public void testRemoveFavorTokensNegative() {
        Player player = new Player("foo");
        WindowPattern windowPattern = new WindowPattern(10);
        player.setWindowPattern(windowPattern);
        assertEquals(10, player.getFavorTokens());
        try {
            player.removeFavorTokens(11);
        }
        catch(IllegalStateException e) {
            return;
        }
        fail();
    }

    @Test
    public void testPoints() {
        Player player = new Player("foo");
        assertEquals(0, player.getPoints());
        player.addPoints(5);
        assertEquals(5, player.getPoints());
    }

    @Test
    public void testGetPrivateObjective() {
        Player player = new Player("foo");
        PrivateObjective po = new PrivateObjective("foo", Colour.BLUE);
        player.setPrivateObjective(po);
        assertEquals(po, player.getPrivateObjective());
    }

    // Miss copy of private objective: to be implemented.
    @Test
    public void testCopy() {
        Player player = new Player("foo");

        PrivateObjective po = new PrivateObjective("foo", Colour.BLUE);
        WindowPattern wp = new WindowPattern(10);

        player.setPrivateObjective(po);
        player.setWindowPattern(wp);
        player.addPoints(5);
        player.setPlacedDie(true);
        player.setUsedTool(true);

        Player player1 = player.copy();

        assertNotEquals(player, player1);
        assertEquals(player.isPlacedDie(), player1.isPlacedDie());
        assertEquals(player.isUsedTool(), player1.isUsedTool());
        assertEquals(player.getFavorTokens(), player1.getFavorTokens());
        assertEquals(player.getPoints(), player1.getPoints());
        assertEquals(player.getNickname(), player1.getNickname());
//        assertEquals(player.getPrivateObjective().getDescription(), player1.getPrivateObjective().getDescription());
//        assertEquals(player.getPrivateObjective().getShade(), player1.getPrivateObjective().getShade());
        assertNotEquals(player.getPrivateObjective(), player1.getPrivateObjective());
        assertEquals(player.getWindowPattern().getDifficulty(), player1.getWindowPattern().getDifficulty());
        assertNotEquals(player.getWindowPattern(), player1.getWindowPattern());
            }
}