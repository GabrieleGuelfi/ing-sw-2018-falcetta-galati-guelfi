package it.polimi.se2018.controller.tool;

import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforview.*;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.utils.StringJSON;

import java.util.List;

import static it.polimi.se2018.controller.VerifyRules.*;
import static java.lang.Math.abs;

public class DieChanger extends Tool {

    private boolean oppositeFace;
    private boolean plusMinusOne;
    private boolean mixAllDice;

    DieChanger(List<String> name, boolean oppositeFace, boolean plusMinusOne, boolean mixAllDice) {
        super(name);

        this.oppositeFace = oppositeFace;
        this.plusMinusOne = plusMinusOne;
        this.mixAllDice = mixAllDice;
    }


    @Override
    public boolean use(MessageToolResponse message, Match match, Player player) {

        if(mixAllDice) {
            for (Die d : match.getRound().getDraftPool().getBag())
                d.setRandomValue();
        }

        if(plusMinusOne) {
            if(!verifyDiceInDraftpool(message.getDiceFromDp(), match.getRound().getDraftPool().size())) {
                virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorMove","noDieDraftpool")));
                return true;
            }
            Die die = match.getRound().getDraftPool().getBag().get(message.getDiceFromDp());
            if (die.getValue()==6 && message.getPlusOne()) {
                virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorTool","plusOne")));
                return true;
            }
            if (die.getValue()==1 && !message.getPlusOne()) {
                virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorTool","minusOne")));
                return true;
            }

            if(message.getPlusOne()) {
                die.setValue(die.getValue()+1);
            } else {
                die.setValue(die.getValue()-1);
            }

        }

        if(oppositeFace) {
            if(!verifyDiceInDraftpool(message.getDiceFromDp(), match.getRound().getDraftPool().size())) {
                virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorMove","noDieDraftpool")));
                return true;
            }
            Die die = match.getRound().getDraftPool().getBag().get(message.getDiceFromDp());
            die.setValue(abs(die.getValue()-7));
        }

        if(!oppositeFace && !plusMinusOne && !mixAllDice) {
            if(!verifyDiceInDraftpool(message.getDiceFromDp(), match.getRound().getDraftPool().size())) {
                virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorMove","noDieDraftpool")));
                return true;
            }
            Die die = match.getRound().getDraftPool().getBag().get(message.getDiceFromDp());
            die.setRandomValue();
            boolean found = false;
            for (int i=0; i< WindowPattern.MAX_ROW && !found; i++) {
                for (int j=0; j<WindowPattern.MAX_COL && !found; j++) {
                    if(isNearDie(player.getWindowPattern(), i, j) && verifyColor(player.getWindowPattern(), i, j, die) && verifyNumber(player.getWindowPattern(), i, j, die))
                        found = true;
                }
            }
            if (found) {
                die.setPlacing(true);
                virtualView.send(new MessageForceMove(player.getNickname(), die, player.getWindowPattern(), false, true, false));
                if(this.used) player.removeFavorTokens(2);
                else {
                    this.used = true;
                    player.removeFavorTokens(1);
                }

                player.setUsedTool(true);
                this.isBeingUsed = false;
                return false;
            }
            else {
                virtualView.send(new MessageError(StringJSON.printStrings("errorTool","errorPlacing")));
            }

        }

        match.notifyObservers(new MessageDPChanged(match.getRound().getDraftPool()));
        finishToolMove(player);
        return true;
    }

    @Override
    public void requestOrders(Player player, Match match) {
        if(!canUseTool(player)) return;
        if (mixAllDice) {
            if(match.getRound().getNumTurn() <= match.getPlayers().size() || player.isPlacedDie()) {
                virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorTool","cantUseTool")));
                virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), -1));
                return;
            }
            virtualView.send(new MessageToolOrder(player.getNickname(), 0, false));
        }
        if (plusMinusOne) virtualView.send(new MessageToolOrder(player.getNickname(), 1, true));
        if (oppositeFace) virtualView.send(new MessageToolOrder(player.getNickname(), 1, false));
        if (!oppositeFace && !plusMinusOne && !mixAllDice){
            if (player.isPlacedDie()) {
                virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorTool","alreadyPlaced")));
                virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), -1));
                return;
            }
            virtualView.send(new MessageToolOrder(player.getNickname(), 1, false));
        }
        this.isBeingUsed = true;
    }
}
