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
        tools.add(() -> new DieChanger("Glazing Hammer", false, false, true));
        tools.add(() -> new DieChanger("Grinding Stone", true, false, false));
        tools.add(() -> new DieChanger("Flux Brush", false, false, false));
        tools.add(() -> new DieMover("Eglomise Brush", 1, false, true ));
        tools.add(() -> new DieMover("Copper Foil Burnisher", 1, true, false));
        tools.add(() -> new DieMover("Lathekin", 2, true, true));
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
            virtualView.send(new MessageErrorMove(player.getNickname(), "Not enough favor tokens"));
            virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie()));
            return false;
        }
        return true;
    }

    void finishToolMove(Player player) {

        if(this.used) player.removeFavorTokens(2);
        else {
            this.used = true;
            player.removeFavorTokens(1);
        }

        player.setUsedTool(true);

        virtualView.send(new MessageConfirmMove(player.getNickname(), !player.isPlacedDie()));

    }

    boolean verifyDiceInDraftpool(int positionInDraftpool, int draftpoolSize) {
        if(positionInDraftpool<0 || positionInDraftpool>(draftpoolSize-1)) return false;
        else return true;
    }

    public void setBeingUsed(boolean beingUsed) {
        isBeingUsed = beingUsed;
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

    public abstract boolean use(MessageToolResponse message, Match match, Player player, Controller controller);

    public abstract void requestOrders(Player player, Match match);

    }

