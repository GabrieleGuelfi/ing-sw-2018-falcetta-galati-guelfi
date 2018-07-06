package it.polimi.se2018.controller;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.events.messageforcontroller.*;
import it.polimi.se2018.events.messageforview.MessageEndMatch;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class TestController {

    private VirtualView virtualView;
    private Match match;
    private Controller controller;

    @Before
    public void setUp() throws Exception {
        virtualView = new VirtualView(null);
        List<String> player = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            player.add("player" + i);

        controller = new Controller(player, virtualView);

        Field field = null;
        try {
            field = controller.getClass().getDeclaredField("match");
        } catch (NoSuchFieldException e) {
            final Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, e.getMessage());
        }

        if (field != null)
            field.setAccessible(true);
        else
            return;

        try {
            match = (Match) field.get(controller);
        } catch (IllegalAccessException e) {
            final Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    @Test
    public void testEndGameOnePlayer() {
        testSetWp();
        virtualView.notifyObservers(new MessageClientDisconnected("player2", true));
        assertEquals(0, match.getPlayers().get(2).getPoints());
    }

    @Test
    public void testEndGameMorePlayer() {
        testSetWp();

        Method method = null;
        try {
            method = controller.getClass().getDeclaredMethod("endMatch");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (method != null) {
            method.setAccessible(true);
        }
        else
            return;
        try {
            method.invoke(controller);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        for (Player p : match.getPlayers()) {
            assertEquals(0, p.getPoints());
        }

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
            final Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, e.getMessage());
        }

        if (field1 != null)
            field1.setAccessible(true);
        else
            return;
        Map<String, List<Integer>> windowPattern = new HashMap<>();
        try {
            windowPattern = (Map<String, List<Integer>>) field1.get(HandleJSON.class);
        } catch (IllegalAccessException e) {
            final Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, e.getMessage());
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
        //isNearDie
        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 2, 3));
        if (match.getPlayers().get(0).getWindowPattern().getBox(2, 3).getDie()!=null)
            fail();
        //ValueRestriction
        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 0, 1));
        if (match.getPlayers().get(0).getWindowPattern().getBox(0, 1).getDie()!=null)
            fail();

        die = new Die(Colour.YELLOW);
        die.setValue(3);
        match.getRound().getDraftPool().getBag().add(die);
        //ColourRestriction
        virtualView.notifyObservers(new MessageMoveDie("player0", 1, 1, 0));
        if (match.getPlayers().get(0).getWindowPattern().getBox(1, 0).getDie()!=null)
            fail();
        //nDieDraftpool
        virtualView.notifyObservers(new MessageMoveDie("player0", 5, 1, 0));
        if (match.getPlayers().get(0).getWindowPattern().getBox(1, 0).getDie()!=null)
            fail();

        die = match.getRound().getDraftPool().getBag().get(0);
        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 0, 0));
        assertEquals(die, match.getPlayers().get(0).getWindowPattern().getBox(0, 0).getDie());
        assertEquals(false, match.getRound().getDraftPool().getBag().contains(die));
        //isPlacedDie
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
        //boxFull
        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 0, 0));

        match.getRound().getPlayerTurn().setUsedTool(true);
        die = match.getRound().getDraftPool().getBag().get(0);
        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 1, 1));
        assertEquals (die, match.getPlayers().get(0).getWindowPattern().getBox(1, 1).getDie());

        assertEquals(2, match.getNumRound());
        assertEquals(1, match.getRound().getNumTurn());
        assertEquals("player1", match.getRound().getPlayerTurn().getNickname());
    }

    @Test
    public void testRequestType() {
        testSetWp();
        for (RequestType type:RequestType.values()) {
            virtualView.notifyObservers(new MessageRequest("player0", type));
            assertEquals("player0", match.getRound().getPlayerTurn().getNickname());
            assertFalse(match.getPlayers().get(0).isPlacedDie());
            assertFalse(match.getPlayers().get(0).isUsedTool());
        }
    }

    @Test
    public void testTool1() {
        testSetWp();
        match.getTools().clear();
        match.getTools().add(Tool.factory(0));
        match.getTools().get(0).setVirtualView(virtualView);

        Die die = match.getRound().getDraftPool().getBag().get(0).copy();
        int ft = match.getPlayers().get(0).getFavorTokens();
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, null, null, null, true));

        if (die.getValue() == 6) {
            assertEquals(6, match.getRound().getDraftPool().getBag().get(0).getValue());
            assertEquals(ft, match.getPlayers().get(0).getFavorTokens());
        }
        else {
            assertEquals(die.getValue() + 1, match.getRound().getDraftPool().getBag().get(0).getValue());
            assertEquals(ft - 1, match.getPlayers().get(0).getFavorTokens());
        }

    }

    @Test
    public void testTool2() {
        testSetWp();
        match.getRound().getDraftPool().getBag().clear();
        Die die = new Die(Colour.MAGENTA);
        die.setValue(3);
        match.getRound().getDraftPool().getBag().add(die);
        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 0, 0));
        match.getTools().clear();
        match.getTools().add(Tool.factory(4));
        match.getTools().get(0).setVirtualView(virtualView);
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));

        List<Integer[]> diceFromWp = new ArrayList<>();
        Integer[] positions = { 1, 0 };
        diceFromWp.add(positions);

        List<Integer[]> newPositionsWP = new ArrayList<>();
        Integer[] positionsWP = { 0, 1 };
        newPositionsWP.add(positionsWP);

        die = match.getPlayers().get(0).getWindowPattern().getBox(0,0).getDie();
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, diceFromWp, null, newPositionsWP, false));
        assertEquals(die, match.getPlayers().get(0).getWindowPattern().getBox(0,0).getDie());

        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        positionsWP[0] = 1;
        positionsWP[1] = 0;
        newPositionsWP.clear();
        newPositionsWP.add(positionsWP);
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, diceFromWp, null, newPositionsWP, false));

        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        positions[0] = 0;
        diceFromWp.clear();
        diceFromWp.add(positions);
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, diceFromWp, null, newPositionsWP, false));
        if (match.getPlayers().get(0).getWindowPattern().getBox(0,0).getDie()!=null)
            fail();
        assertEquals(die.getValue(), match.getPlayers().get(0).getWindowPattern().getBox(1,0).getDie().getValue());
        assertEquals(die.getColour(), match.getPlayers().get(0).getWindowPattern().getBox(1,0).getDie().getColour());
    }

    @Test
    public void testTool3() {
        testSetWp();
        match.getRound().getDraftPool().getBag().clear();
        Die die = new Die(Colour.MAGENTA);
        die.setValue(3);
        match.getRound().getDraftPool().getBag().add(die);
        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 0, 0));
        match.getTools().clear();
        match.getTools().add(Tool.factory(5));
        match.getTools().get(0).setVirtualView(virtualView);
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        List<Integer[]> diceFromWp = new ArrayList<>();
        Integer[] positions = { 0, 0 };
        diceFromWp.add(positions);
        List<Integer[]> newPositionsWP = new ArrayList<>();
        Integer[] positionsWP = { 0, 3 };
        newPositionsWP.add(positionsWP);
        die = match.getPlayers().get(0).getWindowPattern().getBox(0,0).getDie();
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, diceFromWp, null, newPositionsWP, false));
        assertEquals(die, match.getPlayers().get(0).getWindowPattern().getBox(0,0).getDie());

        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        positionsWP[1] = 4;
        newPositionsWP.clear();
        newPositionsWP.add(positionsWP);
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, diceFromWp, null, newPositionsWP, false));
        if (match.getPlayers().get(0).getWindowPattern().getBox(0,0).getDie()!=null)
            fail();
        assertEquals(die.getValue(), match.getPlayers().get(0).getWindowPattern().getBox(0,4).getDie().getValue());
        assertEquals(die.getColour(), match.getPlayers().get(0).getWindowPattern().getBox(0,4).getDie().getColour());
    }

    @Test
    public void testTool4() {
        testSetWp();
        match.getRound().getDraftPool().getBag().clear();
        Die die = new Die(Colour.MAGENTA);
        die.setValue(3);
        match.getRound().getDraftPool().getBag().add(die);
        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 0, 0));
        match.getTools().clear();
        match.getTools().add(Tool.factory(6));
        match.getTools().get(0).setVirtualView(virtualView);

        for (int i=0; i<7; i++) {
            if (i < 4)
                virtualView.notifyObservers(new MessageDoNothing("player" + i));
            else
                virtualView.notifyObservers(new MessageDoNothing("player" + (7-i)));
        }

        die = new Die(Colour.YELLOW);
        die.setValue(4);
        match.getRound().getDraftPool().getBag().add(die);
        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 0, 1));
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));

        List<Integer[]> diceFromWp = new ArrayList<>();
        Integer[] positions = { 0, 0 };
        diceFromWp.add(positions);
        Integer[] positions1 = { 0, 1 };
        diceFromWp.add(positions1);
        List<Integer[]> newPositionsWP = new ArrayList<>();
        Integer[] positionsWP = { 3, 4 };
        newPositionsWP.add(positionsWP);
        Integer[] positionsWP1 = { 3, 3 };
        newPositionsWP.add(positionsWP1);

        Die die1 = match.getPlayers().get(0).getWindowPattern().getBox(0,0).getDie();
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, diceFromWp, null, newPositionsWP, false));

        if (match.getPlayers().get(0).getWindowPattern().getBox(0,0).getDie()!=null)
            fail();

        if (match.getPlayers().get(0).getWindowPattern().getBox(0,1).getDie()!=null)
            fail();

        assertEquals(die1.getValue(), match.getPlayers().get(0).getWindowPattern().getBox(3,4).getDie().getValue());
        assertEquals(die1.getColour(), match.getPlayers().get(0).getWindowPattern().getBox(3,4).getDie().getColour());
        assertEquals(die.getValue(), match.getPlayers().get(0).getWindowPattern().getBox(3,3).getDie().getValue());
        assertEquals(die.getColour(), match.getPlayers().get(0).getWindowPattern().getBox(3,3).getDie().getColour());
    }

    @Test
    public void testTool5() {
        testSetWp();
        match.getTools().clear();
        match.getTools().add(Tool.factory(11));
        match.getTools().get(0).setVirtualView(virtualView);

        Die dieRT = new Die(Colour.MAGENTA);
        dieRT.setValue(3);

        match.getRound().getDraftPool().getBag().clear();
        Die dieDP = new Die(Colour.YELLOW);
        dieDP.setValue(4);
        match.getRound().getDraftPool().addDie(dieDP);
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));

        List<Integer> dieRound = new ArrayList<>();
        dieRound.add(3);
        dieRound.add(0);
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, null, dieRound, null, false));

        assertEquals(dieDP, match.getRound().getDraftPool().getBag().get(0));

        for (int i=0; i<8; i++) {
            if (i < 4)
                virtualView.notifyObservers(new MessageDoNothing("player" + i));
            else
                virtualView.notifyObservers(new MessageDoNothing("player" + (7-i)));
        }

        match.getRound().getDraftPool().getBag().clear();
        match.getRound().getDraftPool().addDie(dieRT);
        dieRound.clear();
        dieRound.add(1);
        dieRound.add(0);

        virtualView.notifyObservers(new MessageRequestUseOfTool("player1", 0));
        virtualView.notifyObservers(new MessageToolResponse("player1", 0, null, dieRound, null, false));

        assertEquals(dieRT, match.getRoundTrack().get(1).get(0));
        assertEquals(dieDP, match.getRound().getDraftPool().getBag().get(0));
    }

    @Test
    public void testTool7() {
        testSetWp();
        match.getTools().clear();
        match.getTools().add(Tool.factory(1));
        match.getTools().get(0).setVirtualView(virtualView);

        match.getRound().getDraftPool().getBag().clear();
        for(int i=0; i<6; i++) {
            match.getRound().getDraftPool().getBag().add(new Die(Colour.RED));
            match.getRound().getDraftPool().getBag().get(i).setValue(i+1);
        }

        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        for(int i=0; i<6; i++) {
            assertEquals(i+1, match.getRound().getDraftPool().getBag().get(i).getValue());
        }
        virtualView.notifyObservers(new MessageDoNothing("player0"));
        virtualView.notifyObservers(new MessageDoNothing("player1"));
        virtualView.notifyObservers(new MessageDoNothing("player2"));
        virtualView.notifyObservers(new MessageDoNothing("player3"));

        virtualView.notifyObservers(new MessageMoveDie("player3", 0, 0, 0));
        for(int i=0; i<5; i++) {
            assertEquals(i+2, match.getRound().getDraftPool().getBag().get(i).getValue());
        }
        virtualView.notifyObservers(new MessageRequestUseOfTool("player3", 0));
        for(int i=0; i<5; i++) {
            assertEquals(i+2, match.getRound().getDraftPool().getBag().get(i).getValue());
        }

        virtualView.notifyObservers(new MessageDoNothing("player3"));
        virtualView.notifyObservers(new MessageDoNothing("player2"));
        virtualView.notifyObservers(new MessageDoNothing("player1"));

        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));

    }

    @Test
    public void testTool8() {
        testSetWp();
        match.getTools().clear();
        match.getTools().add(Tool.factory(8));
        match.getTools().get(0).setVirtualView(virtualView);

        Die die1 = new Die(Colour.MAGENTA);
        die1.setValue(3);
        match.getRound().getDraftPool().getBag().clear();
        Die die2 = new Die(Colour.YELLOW);
        die2.setValue(2);
        match.getRound().getDraftPool().addDie(die1);
        match.getRound().getDraftPool().addDie(die2);

        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 0, 0));

        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        virtualView.notifyObservers(new MessageToolResponse("player0"));

        //verifyNear
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        List<Integer[]> newPositionsWP = new ArrayList<>();
        Integer[] positionsWP = { 3, 4 };
        newPositionsWP.add(positionsWP);
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, null, null, newPositionsWP, false));
        assertNull(match.getPlayers().get(0).getWindowPattern().getBox(3,4).getDie());
        assertEquals(die2, match.getRound().getDraftPool().getBag().get(0));

        //verifyNumber
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        positionsWP[0] = 0;
        positionsWP[1] = 1;
        newPositionsWP.clear();
        newPositionsWP.add(positionsWP);
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, null, null, newPositionsWP, false));
        assertNull(match.getPlayers().get(0).getWindowPattern().getBox(0,1).getDie());
        assertEquals(die2, match.getRound().getDraftPool().getBag().get(0));

        //verifyColor
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        positionsWP[0] = 1;
        positionsWP[1] = 0;
        newPositionsWP.clear();
        newPositionsWP.add(positionsWP);
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, null, null, newPositionsWP, false));
        assertNull(match.getPlayers().get(0).getWindowPattern().getBox(1,0).getDie());
        assertEquals(die2, match.getRound().getDraftPool().getBag().get(0));

        //correctUse
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        positionsWP[0] = 1;
        positionsWP[1] = 1;
        newPositionsWP.clear();
        newPositionsWP.add(positionsWP);
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, null, null, newPositionsWP, false));
        assertEquals(die2, match.getPlayers().get(0).getWindowPattern().getBox(1,1).getDie());
        assertEquals(0, match.getRound().getDraftPool().getBag().size());

        for (int i=1; i<6; i++) {
            if (i < 4)
                virtualView.notifyObservers(new MessageDoNothing("player" + i));
            else
                virtualView.notifyObservers(new MessageDoNothing("player" + (7-i)));
        }
        virtualView.notifyObservers(new MessageRequestUseOfTool("player1", 0));
        assertEquals(false, match.getTools().get(0).isBeingUsed());

        virtualView.notifyObservers(new MessageDoNothing("player1"));
        assertEquals(2, match.getNumRound());
        assertEquals("player1", match.getRound().getPlayerTurn().getNickname());
    }

    @Test
    public void testTool9() {
        testSetWp();
        match.getTools().clear();
        match.getTools().add(Tool.factory(9));
        match.getTools().get(0).setVirtualView(virtualView);

        Die die1 = new Die(Colour.MAGENTA);
        die1.setValue(3);
        match.getRound().getDraftPool().getBag().clear();
        Die die2 = new Die(Colour.YELLOW);
        die2.setValue(4);
        match.getRound().getDraftPool().addDie(die1);
        match.getRound().getDraftPool().addDie(die2);

        virtualView.notifyObservers(new MessageMoveDie("player0", 0, 0, 0));

        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        assertEquals(false, match.getTools().get(0).isBeingUsed());

        for (int i=0; i<7; i++) {
            if (i < 4)
                virtualView.notifyObservers(new MessageDoNothing("player" + i));
            else
                virtualView.notifyObservers(new MessageDoNothing("player" + (7-i)));
        }

        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));

        //verifyNear
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        List<Integer[]> newPositionsWP = new ArrayList<>();
        Integer[] positionsWP = { 0, 1 };
        newPositionsWP.add(positionsWP);
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, null, null, newPositionsWP, false));
        assertNull(match.getPlayers().get(0).getWindowPattern().getBox(0,1).getDie());
        assertEquals(die2, match.getRound().getDraftPool().getBag().get(0));

        //verifyNumber
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        positionsWP[0] = 0;
        positionsWP[1] = 4;
        newPositionsWP.clear();
        newPositionsWP.add(positionsWP);
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, null, null, newPositionsWP, false));
        assertNull(match.getPlayers().get(0).getWindowPattern().getBox(0,4).getDie());
        assertEquals(die2, match.getRound().getDraftPool().getBag().get(0));

        //verifyColor
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        positionsWP[0] = 2;
        positionsWP[1] = 2;
        newPositionsWP.clear();
        newPositionsWP.add(positionsWP);
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, null, null, newPositionsWP, false));
        assertNull(match.getPlayers().get(0).getWindowPattern().getBox(2,2).getDie());
        assertEquals(die2, match.getRound().getDraftPool().getBag().get(0));

        //correctUse
        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        positionsWP[0] = 3;
        positionsWP[1] = 2;
        newPositionsWP.clear();
        newPositionsWP.add(positionsWP);
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, null, null, newPositionsWP, false));
        assertEquals(die2, match.getPlayers().get(0).getWindowPattern().getBox(3,2).getDie());
        assertEquals(9, match.getRound().getDraftPool().getBag().size());
    }

    @Test
    public void testTool10() {
        testSetWp();
        match.getTools().clear();
        match.getTools().add(Tool.factory(2));
        match.getTools().get(0).setVirtualView(virtualView);

        match.getRound().getDraftPool().getBag().clear();
        for(int i=0; i<6; i++) {
            match.getRound().getDraftPool().getBag().add(new Die(Colour.RED));
            match.getRound().getDraftPool().getBag().get(i).setValue(i+1);
        }

        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        virtualView.notifyObservers(new MessageToolResponse("player0", 0, null, null, null, false));
        assertEquals(6, match.getRound().getDraftPool().getBag().get(0).getValue());
        virtualView.notifyObservers(new MessageDoNothing("player0"));

        virtualView.notifyObservers(new MessageRequestUseOfTool("player1", 0));
        virtualView.notifyObservers(new MessageToolResponse("player1", 1, null, null, null, false));
        assertEquals(5, match.getRound().getDraftPool().getBag().get(1).getValue());
        virtualView.notifyObservers(new MessageDoNothing("player1"));

        virtualView.notifyObservers(new MessageRequestUseOfTool("player2", 0));
        virtualView.notifyObservers(new MessageToolResponse("player2", 2, null, null, null, false));
        assertEquals(4, match.getRound().getDraftPool().getBag().get(2).getValue());
        virtualView.notifyObservers(new MessageDoNothing("player2"));

        virtualView.notifyObservers(new MessageRequestUseOfTool("player3", 0));
        virtualView.notifyObservers(new MessageToolResponse("player3", 3, null, null, null, false));
        assertEquals(3, match.getRound().getDraftPool().getBag().get(3).getValue());
        virtualView.notifyObservers(new MessageDoNothing("player3"));

        virtualView.notifyObservers(new MessageRequestUseOfTool("player3", 0));
        virtualView.notifyObservers(new MessageToolResponse("player3", 4, null, null, null, false));
        assertEquals(2, match.getRound().getDraftPool().getBag().get(4).getValue());
        virtualView.notifyObservers(new MessageDoNothing("player3"));

        virtualView.notifyObservers(new MessageRequestUseOfTool("player2", 0));
        virtualView.notifyObservers(new MessageToolResponse("player2", 5, null, null, null, false));
        assertEquals(1, match.getRound().getDraftPool().getBag().get(5).getValue());
        virtualView.notifyObservers(new MessageDoNothing("player2"));

    }

    @Test
    public void testTool11() {
        testSetWp();
        match.getTools().clear();
        match.getTools().add(Tool.factory(7));
        match.getTools().get(0).setVirtualView(virtualView);

        match.getRound().getDraftPool().getBag().clear();
        Die die1 = new Die(Colour.MAGENTA);
        die1.setValue(3);
        Die die2 = new Die(Colour.MAGENTA);
        die2.setValue(2);
        Die die3 = new Die(Colour.MAGENTA);
        die3.setValue(2);
        match.getRound().getDraftPool().addDie(die1);
        match.getRound().getDraftPool().addDie(die2);
        match.getRound().getDraftPool().addDie(die3);

        virtualView.notifyObservers(new MessageRequestUseOfTool("player0", 0));
        assertEquals(false, match.getTools().get(0).isBeingUsed());

        for (int i=0; i<8; i++) {
            if (i==1) {
                virtualView.notifyObservers(new MessageMoveDie("player1", 0, 0, 2));
            }
            if (i==6) {
                virtualView.notifyObservers(new MessageMoveDie("player1", 0, 1, 3));
            }
            if (i < 4)
                virtualView.notifyObservers(new MessageDoNothing("player" + i));
            else
                virtualView.notifyObservers(new MessageDoNothing("player" + (7-i)));
        }

        match.getRound().getDraftPool().getBag().clear();
        Die die4 = new Die(Colour.BLUE);
        die3.setValue(2);
        match.getRound().getDraftPool().addDie(die4);
        virtualView.notifyObservers(new MessageMoveDie("player1", 0, 2, 3));

        //noDiePosition
        virtualView.notifyObservers(new MessageRequestUseOfTool("player1", 0));
        List<Integer[]> diceFromWp = new ArrayList<>();
        Integer[] positions = { 0, 0 };
        Integer[] positions1 = { 1, 3 };
        diceFromWp.add(positions);
        diceFromWp.add(positions1);
        List<Integer[]> newPositionsWP = new ArrayList<>();
        Integer[] positionsWP = { 3, 3 };
        Integer[] positionsWP1 = { 2, 4 };
        newPositionsWP.add(positionsWP);
        newPositionsWP.add(positionsWP1);
        virtualView.notifyObservers(new MessageToolResponse("player1", 0, diceFromWp, null, newPositionsWP, false));
        assertEquals(die1, match.getPlayers().get(1).getWindowPattern().getBox(0, 2).getDie());
        assertEquals(die2, match.getPlayers().get(1).getWindowPattern().getBox(1, 3).getDie());

        //noDieRoundTrack
        virtualView.notifyObservers(new MessageRequestUseOfTool("player1", 0));
        diceFromWp.clear();
        positions[0] = 2;
        positions[1] = 3;
        diceFromWp.add(positions);
        diceFromWp.add(positions1);
        newPositionsWP.add(positionsWP);
        newPositionsWP.add(positionsWP1);
        virtualView.notifyObservers(new MessageToolResponse("player1", 0, diceFromWp, null, newPositionsWP, false));
        assertEquals(die1, match.getPlayers().get(1).getWindowPattern().getBox(0, 2).getDie());
        assertEquals(die2, match.getPlayers().get(1).getWindowPattern().getBox(1, 3).getDie());

        //correctUse
        virtualView.notifyObservers(new MessageRequestUseOfTool("player1", 0));
        diceFromWp.clear();
        positions[0] = 0;
        positions[1] = 2;
        diceFromWp.add(positions);
        diceFromWp.add(positions1);
        newPositionsWP.add(positionsWP);
        newPositionsWP.add(positionsWP1);
        virtualView.notifyObservers(new MessageToolResponse("player1", 0, diceFromWp, null, newPositionsWP, false));

        if (match.getPlayers().get(1).getWindowPattern().getBox(0, 2).getDie()!=null || match.getPlayers().get(1).getWindowPattern().getBox(1, 3).getDie()!=null)
            fail();

        assertEquals(die1.getColour(), match.getPlayers().get(1).getWindowPattern().getBox(3, 3).getDie().getColour());
        assertEquals(die1.getValue(), match.getPlayers().get(1).getWindowPattern().getBox(3, 3).getDie().getValue());
        assertEquals(die2.getColour(), match.getPlayers().get(1).getWindowPattern().getBox(2, 4).getDie().getColour());
        assertEquals(die2.getValue(), match.getPlayers().get(1).getWindowPattern().getBox(2, 4).getDie().getValue());
    }

    /*
    @Test
    public void visit1() {
    }
    */
}