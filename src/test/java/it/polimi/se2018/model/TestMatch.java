package it.polimi.se2018.model;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.dicecollection.Bag;
import it.polimi.se2018.model.publicobjective.PublicObjective;
import it.polimi.se2018.view.VirtualView;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TestMatch {

    @Test
    public void testConstructorPositive() {
        Bag bag = new Bag();
        bag.populateBag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        VirtualView view = new VirtualView(null);

        objectives.add(PublicObjective.factory(2));
        objectives.add(PublicObjective.factory(3));

        ArrayList<Tool> tools = new ArrayList<>();

        Player player = new Player("foo");
        players.add(player);

        try {
            Match match = new Match(bag, players, objectives, tools, view);

            assertEquals(1, match.getNumRound());
            assertEquals(bag, match.getBag());
            assertEquals(players, match.getPlayers());
            assertEquals(tools, match.getTools());
            assertEquals(objectives, match.getPublicObjectives());
            Round testRound = new Round(match.getRound().getDraftPool(), player);
            assertEquals(testRound.getDraftPool(), match.getRound().getDraftPool());
            assertEquals(testRound.getPlayerTurn(), match.getRound().getPlayerTurn());
            assertEquals(testRound.getNumTurn(), match.getRound().getNumTurn());
            assertEquals(bag, match.getBag());
            assertEquals(player, match.getFirstPlayerRound());

            Map<Integer, List<Die>> roundTrack = match.getRoundTrack();

            assertTrue(roundTrack.isEmpty());
            assertEquals(1, match.getNumRound());
        }

        catch(NullPointerException e){
            fail();
            }
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
        Bag bag= new Bag();
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
//        assertTrue(match.getPlayers().isEmpty());

        match.deactivatePlayer(player);

//        assertTrue(match.getActivePlayers().isEmpty());
        assertTrue(match.getPlayers().contains(player));

    }

    @Test
    public void testDeactivatePlayerNegative() {
        Bag bag=new Bag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        Player firstPlayer = new Player("bar");
        players.add(firstPlayer);

        Player player = new Player("foo");

        Match match = new Match(bag, players, objectives, tools, null);

        assertFalse(match.getActivePlayers().contains(player));

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

        match.deactivatePlayer(player);

//        assertTrue(match.getActivePlayers().isEmpty());
        assertTrue(match.getPlayers().contains(player));

        match.activatePlayer(player);

        assertTrue(match.getActivePlayers().contains(player));
//        assertTrue(match.getPlayers().isEmpty());

    }

    @Test
    public void testActivatePlayerNegative() {
        Bag bag=new Bag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        Player firstPlayer = new Player("bar");
        players.add(firstPlayer);

        Player player = new Player("foo");

        Match match = new Match(bag, players, objectives, tools, null);

        assertFalse(match.getActivePlayers().contains(player));
//        assertTrue(match.getPlayers().isEmpty());

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

        Player player = new Player("foo");
        players.add(player);
        
        Match match = new Match(bag, players, objectives, tools, null);

        for(int i=0; i<9; i++) {
            match.setRound();
        }

        try {
            match.setRound();
        }
        catch(IllegalStateException e) {
            return;
        }
        fail();
    }

    @Test
    public void testSetRoundTrack() {
        Bag bag = new Bag();
        bag.populateBag();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<PublicObjective> objectives = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();

        Player firstPlayer = new Player("bar");
        players.add(firstPlayer);

        Match match = new Match(bag, players, objectives, tools, null);

        match.setRoundTrack();

        assertEquals(match.getRound().getDraftPool().getBag(), match.getRoundTrack().get(1));
    } 

}