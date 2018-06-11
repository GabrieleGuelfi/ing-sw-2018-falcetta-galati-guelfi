package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforview.MessageErrorMove;
import it.polimi.se2018.events.messageforview.MessageToolOrder;
import it.polimi.se2018.events.messageforview.MessageWPChanged;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPattern;

import java.util.ArrayList;
import java.util.List;

public class DieMover extends Tool {

    private int numberOfDice;
    private boolean respectColour;
    private boolean respectValue;

    DieMover(String name, int numberOfDice, boolean respectColour, boolean respectValue) {
        super(name);
        this.numberOfDice = numberOfDice;
        this.respectColour = respectColour;
        this.respectValue = respectValue;
    }

    @Override
    public boolean use(MessageToolResponse message, Match match, Player player, Controller controller) {

        List<Integer> originalRows = new ArrayList<>();
        List<Integer> originalColumns = new ArrayList<>();
        List<Die> localDice = new ArrayList<>();
        int row;
        int column;

        WindowPattern localWindowPattern = player.getWindowPattern().copy();

        for(int i=0; i<numberOfDice; i++) {
            originalRows.add(message.getDiceFromWp().get(i)[0]);
            originalColumns.add(message.getDiceFromWp().get(i)[1]);
            localDice.add(localWindowPattern.getBox(originalRows.get(i), originalColumns.get(i)).getDie());
            localWindowPattern.getBox(originalRows.get(i), originalColumns.get(i)).setDie(null);
            localWindowPattern.addEmptyBox();
        }

        for(int i=0; i<numberOfDice; i++){

            if (localDice.get(i)==null) {
                virtualView.send(new MessageErrorMove( player.getNickname(), "No die in this position!"));
                return true;
            }

            row = message.getPositionsInWp().get(i)[0];
            column = message.getPositionsInWp().get(i)[1];

            if(!controller.isNearDie(localWindowPattern, row, column)) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "Violated near dice restriction"));
                return true;
            }

            if(respectColour && !controller.verifyColor(localWindowPattern, row, column, localDice.get(i))) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "Violated colour restriction"));
                return true;
            }

            if(respectValue && !controller.verifyNumber(localWindowPattern, row, column, localDice.get(i))) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "Violated value restriction"));
                return true;
            }

            localWindowPattern.putDice(localDice.get(i), row, column);


        }

        for(int i=0; i<numberOfDice; i++) {
            row = message.getPositionsInWp().get(i)[0];
            column = message.getPositionsInWp().get(i)[1];
            player.getWindowPattern().getBox(row, column).setDie(localDice.get(i));
            player.getWindowPattern().getBox(originalRows.get(i), originalColumns.get(i)).setDie(null);
        }


        finishToolMove(player);
        match.notifyObservers(new MessageWPChanged(player.getNickname(), player.getWindowPattern()));

        return true;
    }



    @Override
    public void requestOrders(Player player, Match match) {
        if(!canUseTool(player)) return;
        if(!(respectValue&&respectColour)) virtualView.send(new MessageToolOrder(player.getNickname(), 0, 1,  1));
        else virtualView.send(new MessageToolOrder(player.getNickname(), 0, 2, 2));
        this.isBeingUsed = true;
    }
}
