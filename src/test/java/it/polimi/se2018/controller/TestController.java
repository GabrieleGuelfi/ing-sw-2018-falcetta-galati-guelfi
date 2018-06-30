package it.polimi.se2018.controller;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.events.messageforcontroller.MessageDoNothing;
import it.polimi.se2018.events.messageforcontroller.MessageMoveDie;
import it.polimi.se2018.events.messageforcontroller.MessageSetWP;
import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.HandleJSON;
import it.polimi.se2018.view.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TestController {

    private VirtualView virtualView;
    private Match match;

    @Before
    public void setUp() throws Exception {
        virtualView = new VirtualView(null);
        List<String> player = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            player.add("player" + i);

        Controller controller = new Controller(player, virtualView);

        Field field = null;
        try {
            field = controller.getClass().getDeclaredField("match");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if (field != null)
            field.setAccessible(true);
        else
            return;

        try {
            match = (Match) field.get(controller);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void visitEndGame() {
        virtualView.notifyObservers(new MessageDoNothing("player1"));
        assertEquals(0, match.getPlayers().get(1).getPoints());
    }

    @Test
    public void testUpdateNegative() {
        virtualView.notifyObservers(new MessageDoNothing("player1"));
        assertEquals("player0", match.getRound().getPlayerTurn().getNickname());
    }

    @Test
    public void testVisitDoNothing() {
        for (int i = 0; i < match.getPlayers().size(); i++) {
            assertEquals("player" + i, match.getRound().getPlayerTurn().getNickname());
            virtualView.notifyObservers(new MessageDoNothing("player" + i));
        }
        for (int i = match.getPlayers().size() - 1; i >= 0; i--) {
            assertEquals("player" + i, match.getRound().getPlayerTurn().getNickname());
            virtualView.notifyObservers(new MessageDoNothing("player" + i));
        }
        assertEquals("player1", match.getRound().getPlayerTurn().getNickname());

        virtualView.notifyObservers(new MessageDoNothing("player8"));
        assertEquals("player1", match.getRound().getPlayerTurn().getNickname());

    }

    @Test
    public void testSetWp() {
        Field field1 = null;
        try {
            field1 = HandleJSON.class.getDeclaredField("windowPattern");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if (field1 != null)
            field1.setAccessible(true);
        else
            return;
        Map<String, List<Integer>> windowPattern = new HashMap<>();
        try {
            windowPattern = (Map<String, List<Integer>>) field1.get(HandleJSON.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        windowPattern.clear();
        virtualView.notifyObservers(new MessageSetWP("player0", 0, 0));
        assertNull(match.getPlayers().get(0).getWindowPattern());

        List<Integer> chosen = new ArrayList<>();
        chosen.add(3);
        chosen.add(4);
        windowPattern.put("player0", chosen);
        virtualView.notifyObservers(new MessageSetWP("player0", 9, 1));
        assertNull(match.getPlayers().get(0).getWindowPattern());

        windowPattern.clear();
        int j=0;
        for (int i=0; i<match.getPlayers().size(); i++){
            List<Integer> choice = new ArrayList<>();
            choice.add(j);
            j++;
            choice.add(j);
            j++;
            windowPattern.put("player"+i, choice);
            virtualView.notifyObservers(new MessageSetWP("player"+i, j-1, i%2));
        }
        assertEquals("Fractal Drops", match.getPlayers().get(0).getWindowPattern().getName());
        assertEquals(3, match.getPlayers().get(0).getWindowPattern().getDifficulty());
        assertEquals(3, match.getPlayers().get(0).getFavorTokens());
        assertEquals(0, match.getPlayers().get(0).getWindowPattern().getBox(2, 1).getValueRestriction());
        assertEquals(Colour.WHITE, match.getPlayers().get(0).getWindowPattern().getBox(2, 1).getColourRestriction());
        assertEquals(4, match.getPlayers().get(0).getWindowPattern().getBox(0, 1).getValueRestriction());
        assertEquals(Colour.WHITE, match.getPlayers().get(0).getWindowPattern().getBox(0, 1).getColourRestriction());
        assertEquals(0, match.getPlayers().get(0).getWindowPattern().getBox(3, 1).getValueRestriction());
        assertEquals(Colour.YELLOW, match.getPlayers().get(0).getWindowPattern().getBox(3, 1).getColourRestriction());

        assertEquals("Water of Life", match.getPlayers().get(1).getWindowPattern().getName());
        assertEquals(6, match.getPlayers().get(1).getWindowPattern().getDifficulty());
        assertEquals(6, match.getPlayers().get(1).getFavorTokens());

        assertEquals("Aurorae Magnificus", match.getPlayers().get(2).getWindowPattern().getName());
        assertEquals(5, match.getPlayers().get(2).getWindowPattern().getDifficulty());
        assertEquals(5, match.getPlayers().get(2).getFavorTokens());

        assertEquals("Sun's Glory", match.getPlayers().get(3).getWindowPattern().getName());
        assertEquals(6, match.getPlayers().get(3).getWindowPattern().getDifficulty());
        assertEquals(6, match.getPlayers().get(3).getFavorTokens());

    }

    @Test
    public void testVisitMoveDie() {
        testSetWp();
        match.getRound().getDraftPool().getBag().clear();
        Die die = new Die(Colour.MAGENTA);
        die.setValue(3);
        match.getRound().getDraftPool().getBag().add(die);
        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 2, 3));
        if (match.getPlayers().get(0).getWindowPattern().getBox(2, 3).getDie()!=null)
            fail();

        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 0, 1));
        if (match.getPlayers().get(0).getWindowPattern().getBox(0, 1).getDie()!=null)
            fail();

        die = new Die(Colour.YELLOW);
        die.setValue(3);
        match.getRound().getDraftPool().getBag().add(die);
        virtualView.notifyObservers(new MessageMoveDie("player0", 1, 1, 0));
        if (match.getPlayers().get(0).getWindowPattern().getBox(1, 0).getDie()!=null)
            fail();

        die = match.getRound().getDraftPool().getBag().get(0);
        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 0, 0));
        assertEquals(die, match.getPlayers().get(0).getWindowPattern().getBox(0, 0).getDie());
        assertEquals(false, match.getRound().getDraftPool().getBag().contains(die));

        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 2, 3));
        if (match.getPlayers().get(0).getWindowPattern().getBox(1, 1).getDie()!=null)
            fail();

        virtualView.notifyObservers(new MessageDoNothing("player0"));
        virtualView.notifyObservers(new MessageDoNothing("player1"));
        virtualView.notifyObservers(new MessageDoNothing("player2"));
        virtualView.notifyObservers(new MessageDoNothing("player3"));
        virtualView.notifyObservers(new MessageDoNothing("player3"));
        virtualView.notifyObservers(new MessageDoNothing("player2"));
        virtualView.notifyObservers(new MessageDoNothing("player1"));

        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 2, 3));
        if (match.getPlayers().get(0).getWindowPattern().getBox(2, 3).getDie()!=null)
            fail();

        match.getRound().getPlayerTurn().setUsedTool(true);
        die = match.getRound().getDraftPool().getBag().get(0);
        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 1, 1));
        assertEquals (die, match.getPlayers().get(0).getWindowPattern().getBox(1, 1).getDie());

        assertEquals(2, match.getNumRound());
        assertEquals(1, match.getRound().getNumTurn());
        assertEquals("player1", match.getRound().getPlayerTurn().getNickname());
    }

    @Test
    public void testTool1() {
        match.getTools().add(Tool.factory(0));
        match.getTools().get(3).setVirtualView(virtualView);
        Player player = match.getPlayers().get(0);

        Method method = null;
        try {
            method = player.getClass().getDeclaredMethod("setFavorTokens", int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        try {
            method.invoke(player, 3);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        Die die = match.getRound().getDraftPool().getBag().get(0).copy();
        int ft = match.getPlayers().get(0).getFavorTokens();
        match.getTools().get(3).use(new MessageToolResponse("player0", 0, null, null, null, true), match, player);

        if (die.getValue() == 6)
            assertEquals(6, match.getRound().getDraftPool().getBag().get(0).getValue());
        else
            assertEquals(die.getValue() + 1, match.getRound().getDraftPool().getBag().get(0).getValue());

        assertEquals(ft - 1, player.getFavorTokens());

    }

    /*
    @Test
    public void visit1() {
    }
    */
}