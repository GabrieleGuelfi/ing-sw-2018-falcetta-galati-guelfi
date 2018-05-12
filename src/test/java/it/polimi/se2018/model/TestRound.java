package it.polimi.se2018.model;

import it.polimi.se2018.model.dicecollection.Bag;
import it.polimi.se2018.model.dicecollection.DraftPool;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestRound {
    @Test
    public void testConstructorPositive() {
        Bag bag = new Bag();
        bag.populateBag();
        DraftPool draftPool = new DraftPool(bag, 4);
        Player player = new Player("foo");


        Round round = new Round(draftPool, player);

        assertEquals(player, round.getPlayerTurn());
    }

    @Test
    public void testConstructorNegativeDraftPool() {

        DraftPool draftPool = null;
        Player player = new Player("foo");

        try {
            Round round = new Round(draftPool, player);
        }
        catch(IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void testConstructorNegativePlayer() {

        Bag bag = new Bag();
        bag.populateBag();
        DraftPool draftPool = new DraftPool(bag, 4);
        Player player = null;

        try {
            Round round = new Round(draftPool, player);
        }
        catch(IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void testSetPlayerTurn() {
        Bag bag = new Bag();
        bag.populateBag();
        DraftPool draftPool = new DraftPool(bag, 4);
        Player player = new Player("foo");

        Round round = new Round(draftPool, player);

        assertEquals(player, round.getPlayerTurn());

        Player player1 = new Player("bar");

        round.setPlayerTurn(player1);

        assertEquals(player1, round.getPlayerTurn());
    }
}