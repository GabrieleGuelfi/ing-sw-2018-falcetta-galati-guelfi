package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforview.MessageErrorMove;
import it.polimi.se2018.events.messageforview.MessageToolOrder;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;

public class DieMover extends Tool {

    private int numberOfDice;
    private boolean respectColour;
    private boolean respectValue;
    private boolean respectRoundtrack;

    DieMover(String name, int numberOfDice, boolean respectColour, boolean respectValue, boolean respectRoundtrack) {
        super(name);
        this.numberOfDice = numberOfDice;
        this.respectColour = respectColour;
        this.respectValue = respectValue;
        this.respectRoundtrack = respectRoundtrack;
    }

    @Override
    public boolean use(MessageToolResponse message, Match match, Player player, Controller controller) {

        int originalRow = message.getDiceFromWp().get(0)[0];
        int originalColumn = message.getDiceFromWp().get(0)[1];

        Die die = player.getWindowPattern().getBox(originalRow, originalColumn).getDie();

        if (die==null) {
            virtualView.send(new MessageErrorMove( player.getNickname(), "No die in this position!"));
            return true;
        }

        int row = 0;
        int column = 0;

        if(respectColour||respectValue) {
            row = message.getPositionsInWp().get(0)[0];
            column = message.getPositionsInWp().get(0)[1];
            player.getWindowPattern().putDice(null, originalRow, originalColumn);
            player.getWindowPattern().addEmptyBox();
            if(!controller.isNearDie(player.getWindowPattern(), row, column)) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "Violated near dice restriction"));
                player.getWindowPattern().putDice(die, originalRow, originalColumn);
                player.getWindowPattern().decreaseEmptyBox();
                return true;
            }
        }

        if(respectColour) {
            if(!controller.verifyColor(player.getWindowPattern(), row, column, die)) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "Violated colour restriction"));
                player.getWindowPattern().putDice(die, originalRow, originalColumn);
                player.getWindowPattern().decreaseEmptyBox();
                return true;
            }
        }

        if(respectValue) {
            if(!controller.verifyNumber(player.getWindowPattern(), row, column, die)) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "Violated value restriction"));
                player.getWindowPattern().putDice(die, originalRow, originalColumn);
                player.getWindowPattern().decreaseEmptyBox();
                return true;
            }
        }

        player.getWindowPattern().putDice(die, row, column);
        match.getRound().getDraftPool().getBag().remove(die);

        finishToolMove(player);

        return true;
    }

    @Override
    public void requestOrders(Player player, Match match) {
        if(!canUseTool(player)) return;
        virtualView.send(new MessageToolOrder(player.getNickname(), 0, 1,  1));
        this.isBeingUsed = true;
    }
}
