package it.polimi.se2018.model;

import it.polimi.se2018.model.dicecollection.Bag;
import it.polimi.se2018.model.dicecollection.DraftPool;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        assertEquals(1, round.getNumTurn());
        assertEquals(draftPool, round.getDraftPool());
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

    @Test
    public void testNextTurn() {
        Bag bag = new Bag();
        bag.populateBag();
        DraftPool draftPool = new DraftPool(bag, 4);
        List<Player> players = new ArrayList<>();
        players.add(new Player("foo"));
        players.add(new Player("bar"));
        players.add(new Player("wat"));
        players.add(new Player("loc"));
        Round round = new Round(draftPool, players.get(2));
        int i;
        for (i=0; i<4; i++) {
            assertEquals(players.get((2+i)%4).getNickname(), round.getPlayerTurn().getNickname());
            round.nextTurn(players);
        }
        for (i=4; i>1; i--) {
            assertEquals(players.get((2+(i-1))%4).getNickname(), round.getPlayerTurn().getNickname());
            round.nextTurn(players);
        }
        assertEquals(players.get((2+(i-1))%4).getNickname(), round.getPlayerTurn().getNickname());
        try {
            round.nextTurn(players);
        } catch (IllegalStateException e) {
            return;
        }
        fail();
    }
}