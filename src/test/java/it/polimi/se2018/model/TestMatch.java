package it.polimi.se2018.model;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.publicobjective.PublicObjective;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestMatch {

    @Test
    public void testConstructorPositive() {
        Bag bag = new Bag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        Match match = new Match(bag, players, objectives, tools);

        assertEquals(bag, match.getBag());
        assertEquals(players, match.getActivePlayers());
        assertEquals(tools, match.getTools());
        assertEquals(objectives, match.getPublicObjectives());

        ArrayList<Die> roundTrack = match.getRoundTrack();

        assertTrue(roundTrack.isEmpty());
        assertEquals(1, match.getRound());
    }

    @Test
    public void testConstructorNegativeBag() {
        Bag bag=null;
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        try {
            Match match = new Match(bag, players, objectives, tools);
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
            Match match = new Match(bag, players, objectives, tools);
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
            Match match = new Match(bag, players, objectives, tools);
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
            Match match = new Match(bag, players, objectives, tools);
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

        Match match = new Match(bag, players, objectives, tools);

        assertTrue(match.getActivePlayers().contains(player));
        assertTrue(match.getPlayers().isEmpty());

        match.deactivatePlayer(player);

        assertTrue(match.getActivePlayers().isEmpty());
        assertTrue(match.getPlayers().contains(player));

    }

    @Test
    public void testActivatePlayer() {

        Bag bag=new Bag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();
        Player player = new Player("foo");
        players.add(player);
        Match match = new Match(bag, players, objectives, tools);

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
    public void testNextRound() {

        Bag bag=new Bag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        Match match = new Match(bag, players, objectives, tools);

        for(int i=0; i<9; i++) {
            match.nextRound();
        }

        try {
            match.nextRound();
        }
        catch(IllegalStateException e) {
            return;
        }

        fail();
    }
}