package it.polimi.se2018.controller;

import it.polimi.se2018.events.messageforcontroller.MessageDoNothing;
import it.polimi.se2018.view.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestController {

    private VirtualView virtualView;
    private Controller controller;

    @Before
    public void setUp() throws Exception {
        virtualView = new VirtualView(null);
        List<String> player = new ArrayList<>();
        for (int i=0; i<4; i++)
            player.add("player"+i);

        controller = new Controller(player, virtualView);

    }

    @Test
    public void visitEndGame() {
        virtualView.notifyObservers(new MessageDoNothing("player1"));
    }

    /*
    @Test
    public void visit1() {
    }

    @Test
    public void visit2() {
    }

    @Test
    public void visit3() {
    }

    @Test
    public void visit4() {
    }

    @Test
    public void visit5() {
    }

    @Test
    public void visit6() {
    }

    @Test
    public void visit7() {
    }
    */
}