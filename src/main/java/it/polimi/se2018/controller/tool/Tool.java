package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.events.messageforview.MessageAskMove;
import it.polimi.se2018.events.messageforview.MessageConfirmMove;
import it.polimi.se2018.events.messageforview.MessageErrorMove;
import it.polimi.se2018.model.*;
import it.polimi.se2018.view.VirtualView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.out;

public abstract class Tool {

    VirtualView virtualView;
    private String name;
    boolean used;
    boolean isBeingUsed;

    public Tool(String name) {
        this.name = name;
        this.used = false;
    }

    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    private static final List<Command> TOOLS;

    static {

        final List<Command> tools = new ArrayList<>();

        tools.add(() -> new DieChanger("Grozing Pliers", false, true, false));
        tools.add(() -> new DieChanger("Flux Brush", false, false, false));
        tools.add(() -> new DieChanger("Grinding Stone", true, false, false));
        tools.add(() -> new DieChanger("Glazing Hammer", true, false, true));
        tools.add(() -> new DieMover("Eglomise Brush", 1, false, true, false));
        tools.add(() -> new DieMover("Copper Foil Burnisher", 1, true, false, false));
        tools.add(() -> new DieMover("Lathekin", 2, true, true, false));
        tools.add(() -> new DiePlacer("Running Pliers", false, false ));
        tools.add(() -> new DiePlacer("Cork-backed Straightedge", true, false));
        tools.add(() -> new DiePlacer("Flux Remover", false, true));
        tools.add(() -> new DieSwapper("Lens Cutter"));


        TOOLS = Collections.unmodifiableList(tools);
    }

    public static Tool factory(int n) {

        try {
            Command command = TOOLS.get(n);
            return command.create();
        }
        catch (IndexOutOfBoundsException e) {
            out.println("Tool not existing");
            return null;
        }
    }


    boolean canUseTool(Player player) {
        if((this.used && player.getFavorTokens()<2) || (player.getFavorTokens()<1)) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "Not enough favor tokens", player.isPlacedDie(), player.isUsedTool()));
            return false;
        }
        return true;
    }

    void finishToolMove(Player player, Controller controller, Match match) {
        this.isBeingUsed = false;
        if(this.used) player.removeFavorTokens(2);
        else {
            this.used = true;
            player.removeFavorTokens(1);
        }

        player.setUsedTool(true);
        boolean isThereAnotherMove = false;

        if(!player.isPlacedDie()) isThereAnotherMove = true;
        else {
            player.setPlacedDie(false);
            player.setUsedTool(false);
        }

        virtualView.send(new MessageConfirmMove(player.getNickname(), isThereAnotherMove));
        if(!isThereAnotherMove) controller.nextTurn();
        else virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), player.getWindowPattern(), match.getRound().getDraftPool()));

    }

    public String getName() {
        return name;
    }

    public boolean isUsed() {
        return used;
    }

    public boolean isBeingUsed() {
        return isBeingUsed;
    }

    public abstract void use(MessageToolResponse message, Match match, Player player, Controller controller);

    public abstract void requestOrders(Player player);

    }

