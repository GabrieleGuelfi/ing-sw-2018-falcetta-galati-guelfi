package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.events.messageforview.MessageErrorMove;
import it.polimi.se2018.events.messageforview.MessageToolOrder;
import it.polimi.se2018.events.messageforview.MessageWPChanged;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        List<Integer> originalRows = new ArrayList<>();
        List<Integer> originalColumns = new ArrayList<>();
        List<Die> localDice = new ArrayList<>();
        int row;
        int column;

        if(respectRoundtrack) {

            Die firstDie = player.getWindowPattern().getBox(message.getDiceFromWp().get(0)[0], message.getDiceFromWp().get(0)[1]).getDie();
            if(firstDie==null) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "No die in this position!"));
                return true;
            }
            boolean foundInRT = false;

            for (Map.Entry<Integer, List<Die>> entry : match.getRoundTrack().entrySet()) {
                for(Die d: entry.getValue())
                    if(d.getColour().equals(firstDie.getColour())) foundInRT = true;
            }

            if(!foundInRT) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "No die of this colour in roundtrack!"));
                return true;
            }
            if(message.getDiceFromWp().size()>1) {
                Die secondDie = player.getWindowPattern().getBox(message.getDiceFromWp().get(1)[0], message.getDiceFromWp().get(1)[1]).getDie();
                if(!secondDie.getColour().equals(firstDie.getColour())) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), "Dice have different colours!"));
                    return true;
                }
            }

        }

        WindowPattern localWindowPattern = player.getWindowPattern().copy();

        for(int i=0; i<this.numberOfDice; i++) {
            originalRows.add(message.getDiceFromWp().get(i)[0]);
            originalColumns.add(message.getDiceFromWp().get(i)[1]);
            localDice.add(localWindowPattern.getBox(originalRows.get(i), originalColumns.get(i)).getDie());
            localWindowPattern.getBox(originalRows.get(i), originalColumns.get(i)).setDie(null);
            localWindowPattern.addEmptyBox();
        }

        for(int i=0; i<message.getDiceFromWp().size(); i++){

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

        for(int i=0; i<message.getDiceFromWp().size(); i++) {
            player.getWindowPattern().getBox(originalRows.get(i), originalColumns.get(i)).setDie(null);
        }

        for(int i=0; i<message.getDiceFromWp().size(); i++) {
            row = message.getPositionsInWp().get(i)[0];
            column = message.getPositionsInWp().get(i)[1];
            player.getWindowPattern().getBox(row, column).setDie(localDice.get(i));
        }

        finishToolMove(player);
        match.notifyObservers(new MessageWPChanged(player.getNickname(), player.getWindowPattern()));

        return true;
    }



    @Override
    public void requestOrders(Player player, Match match) {
        if(!canUseTool(player)) return;
        if(!(respectValue&&respectColour)) virtualView.send(new MessageToolOrder(player.getNickname(), 0, 1,  1, 0));
        else if(!respectRoundtrack) virtualView.send(new MessageToolOrder(player.getNickname(), 0, 2, 2, 0));
        else virtualView.send(new MessageToolOrder(player.getNickname(),2, 2, true));
        this.isBeingUsed = true;
    }
}
