package it.polimi.se2018.controller.tool;

import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.events.messageforview.MessageAskMove;
import it.polimi.se2018.events.messageforview.MessageConfirmMove;
import it.polimi.se2018.events.messageforview.MessageErrorMove;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.HandleJSON;
import it.polimi.se2018.utils.StringJSON;
import it.polimi.se2018.view.VirtualView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.out;

public abstract class Tool {

    VirtualView virtualView;
    private String name;
    private String description;
    boolean used;
    boolean isBeingUsed;

    public Tool(List<String> name) {
        this.name = name.get(0);
        this.description = name.get(1);
        this.used = false;
    }

    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    private static final List<Command> TOOLS;

    static {

        final List<Command> tools = new ArrayList<>();

        tools.add(() -> new DieChanger(HandleJSON.createTool("1"), false, true, false));
        tools.add(() -> new DieChanger(HandleJSON.createTool("7"), false, false, true));
        tools.add(() -> new DieChanger(HandleJSON.createTool("10"), true, false, false));
        tools.add(() -> new DieChanger(HandleJSON.createTool("6"), false, false, false));
        tools.add(() -> new DieMover(HandleJSON.createTool("2"), 1, false, true, false ));
        tools.add(() -> new DieMover(HandleJSON.createTool("3"), 1, true, false, false));
        tools.add(() -> new DieMover(HandleJSON.createTool("4"), 2, true, true, false));
        tools.add(() -> new DieMover(HandleJSON.createTool("12"), 2, true, true, true));
        tools.add(() -> new DiePlacer(HandleJSON.createTool("8"), false, false ));
        tools.add(() -> new DiePlacer(HandleJSON.createTool("9"), true, false));
        tools.add(() -> new DiePlacer(HandleJSON.createTool("11"), false, true));
        tools.add(() -> new DieSwapper(HandleJSON.createTool("5")));

        TOOLS = Collections.unmodifiableList(tools);
    }

    public static Tool factory(int n) {

        try {
            Command command = TOOLS.get(n);
            return command.create();
        }
        catch (IndexOutOfBoundsException e) {
            out.println(StringJSON.printStrings("errorTool","noTool"));
            return null;
        }
    }


    boolean canUseTool(Player player) {
        if((this.used && player.getFavorTokens()<2) || (player.getFavorTokens()<1)) {
            virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorTool","favorTokens")));
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

        this.isBeingUsed = false;

        virtualView.send(new MessageConfirmMove(player.getNickname(), !player.isPlacedDie()));

    }

    boolean verifyDiceInDraftpool(int positionInDraftpool, int draftpoolSize) {
        return (positionInDraftpool>=0 && positionInDraftpool<=(draftpoolSize-1));
    }

    public void setBeingUsed(boolean beingUsed) {
        isBeingUsed = beingUsed;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUsed() {
        return used;
    }

    public boolean isBeingUsed() {
        return isBeingUsed;
    }

    public abstract boolean use(MessageToolResponse message, Match match, Player player);

    public abstract void requestOrders(Player player, Match match);

    }

