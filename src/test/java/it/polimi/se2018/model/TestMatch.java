package it.polimi.se2018.model;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.dicecollection.Bag;
import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.model.publicobjective.PublicObjective;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestMatch {

    @Test
    public void testConstructorPositive() {
        Bag bag = new Bag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        Match match = new Match(bag, players, objectives, tools, null);

        assertEquals(1, match.getNumRound());
        assertEquals(bag, match.getBag());
        assertEquals(players, match.getActivePlayers());
        assertEquals(tools, match.getTools());
        assertEquals(objectives, match.getPublicObjectives());

        List<Die> roundTrack = match.getRoundTrack();

        assertTrue(roundTrack.isEmpty());
        assertEquals(1, match.getNumRound());
    }

    @Test
    public void testConstructorNegativeBag() {
        Bag bag=null;
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        try {
            Match match = new Match(bag, players, objectives, tools, null);
        }
        catch(IllegalArgumentException e)  {
            return;
        }

        fail();

    }

    @Test
    public void testConstructorNegativePlayers() {
        Bag bag= new Bag();
        ArrayList<Player> players = null;
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        try {
            Match match = new Match(bag, players, objectives, tools, null);
        }
        catch(IllegalArgumentException e)  {
            return;
        }

        fail();

    }

    @Test
    public void testConstructorNegativeObjectives() {
        Bag bag= new Bag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = null;
        ArrayList<Tool> tools = new ArrayList<>();

        try {
            Match match = new Match(bag, players, objectives, tools, null);
        }
        catch(IllegalArgumentException e)  {
            return;
        }

        fail();

    }

    @Test
    public void testConstructorNegativeTools() {
        Bag bag=null;
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = null;

        try {
            Match match = new Match(bag, players, objectives, tools, null);
        }
        catch(IllegalArgumentException e)  {
            return;
        }

        fail();

    }

    @Test
    public void testDeactivatePlayer() {
        Bag bag=new Bag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        Player player = new Player("foo");
        players.add(player);

        Match match = new Match(bag, players, objectives, tools, null);

        assertTrue(match.getActivePlayers().contains(player));
        assertTrue(match.getPlayers().isEmpty());

        match.deactivatePlayer(player);

        assertTrue(match.getActivePlayers().isEmpty());
        assertTrue(match.getPlayers().contains(player));

    }

    @Test
    public void testDeactivatePlayerNegative() {
        Bag bag=new Bag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        Player player = new Player("foo");

        Match match = new Match(bag, players, objectives, tools, null);

        assertFalse(match.getActivePlayers().contains(player));
        assertTrue(match.getPlayers().isEmpty());

        try {
            match.deactivatePlayer(player);
        }
        catch(IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void testActivatePlayer() {

        Bag bag=new Bag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();
        Player player = new Player("foo");
        players.add(player);
        Match match = new Match(bag, players, objectives, tools, null);

        assertTrue(match.getActivePlayers().contains(player));
        assertTrue(match.getPlayers().isEmpty());

        match.deactivatePlayer(player);

        assertTrue(match.getActivePlayers().isEmpty());
        assertTrue(match.getPlayers().contains(player));

        match.activatePlayer(player);

        assertTrue(match.getActivePlayers().contains(player));
        assertTrue(match.getPlayers().isEmpty());

    }

    @Test
    public void testActivatePlayerNegative() {
        Bag bag=new Bag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        Player player = new Player("foo");

        Match match = new Match(bag, players, objectives, tools, null);

        assertFalse(match.getActivePlayers().contains(player));
        assertTrue(match.getPlayers().isEmpty());

        try {
            match.activatePlayer(player);
        }
        catch(IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void testNextRound() {

        Bag bag=new Bag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        Match match = new Match(bag, players, objectives, tools, null);

        for(int i=0; i<9; i++) {
            match.nextNumRound();
        }

        try {
            match.nextNumRound();
        }
        catch(IllegalStateException e) {
            return;
        }

        fail();
    }

    @Test
    public void testGetSetRound() {
        Bag bag = new Bag();
        bag.populateBag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        Match match = new Match(bag, players, objectives, tools, null);

        Player p = new Player("foo");

        DraftPool dp = new DraftPool(bag, 4);

        Round round = new Round(dp, p);

        match.setRound(round);

        assertEquals(round, match.getRound());


    }

}