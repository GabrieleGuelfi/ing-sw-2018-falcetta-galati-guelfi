package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforview.*;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPattern;

import static java.lang.Math.abs;

public class DieChanger extends Tool {

    private boolean oppositeFace;
    private boolean plusMinusOne;
    private boolean mixAllDice;

    DieChanger(String name, boolean oppositeFace, boolean plusMinusOne, boolean mixAllDice) {
        super(name);

        this.oppositeFace = oppositeFace;
        this.plusMinusOne = plusMinusOne;
        this.mixAllDice = mixAllDice;
    }


    @Override
    public boolean use(MessageToolResponse message, Match match, Player player, Controller controller) {

        if(mixAllDice) {
            for (Die d : match.getRound().getDraftPool().getBag())
                d.setRandomValue();
        }

        if(plusMinusOne) {
            if(!verifyDiceInDraftpool(message.getDiceFromDp(), match.getRound().getDraftPool().size())) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "Invalid choice of die from draftpool"));
                return true;
            }
            Die die = match.getRound().getDraftPool().getBag().get(message.getDiceFromDp());
            if (die.getValue()==6 && message.getPlusOne()) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "Can't add 1 to a die with value 6"));
                return true;
            }
            if (die.getValue()==1 && !message.getPlusOne()) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "Can't remove 1 to a die with value 1"));
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
                virtualView.send(new MessageErrorMove(player.getNickname(), "Invalid choice of die from draftpool"));
                return true;
            }
            Die die = match.getRound().getDraftPool().getBag().get(message.getDiceFromDp());
            die.setValue(abs(die.getValue()-7));
        }

        if(!oppositeFace && !plusMinusOne && !mixAllDice) {
            if(!verifyDiceInDraftpool(message.getDiceFromDp(), match.getRound().getDraftPool().size())) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "Invalid choice of die from draftpool"));
                return true;
            }
            Die die = match.getRound().getDraftPool().getBag().get(message.getDiceFromDp());
            die.setRandomValue();
            // Here we should force the user to set the value...
            boolean found = false;
            for (int i=0; i< WindowPattern.MAX_ROW && !found; i++) {
                for (int j=0; j<WindowPattern.MAX_COL && !found; j++) {
                    if(controller.isNearDie(player.getWindowPattern(), i, j) && controller.verifyColor(player.getWindowPattern(), i, j, die) && controller.verifyNumber(player.getWindowPattern(), i, j, die))
                        found = true;
                }
            }
            if (found) {
                die.setPlacing(true);
                player.setPlacedDie(true);
                virtualView.send(new MessageForceMove(player.getNickname(), die, player.getWindowPattern()));
                if(this.used) player.removeFavorTokens(2);
                else {
                    this.used = true;
                    player.removeFavorTokens(1);
                }

                player.setUsedTool(true);
                return false;
            }
            else {
                virtualView.send(new MessageError("You can't place the die, sorry"));
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
                virtualView.send(new MessageErrorMove(player.getNickname(), "You can't use this tool!"));
                virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie()));
                return;
            }
            virtualView.send(new MessageToolOrder(player.getNickname(), 0, false));
        }
        if (plusMinusOne) virtualView.send(new MessageToolOrder(player.getNickname(), 1, true));
        if (oppositeFace) virtualView.send(new MessageToolOrder(player.getNickname(), 1, false));
        if (!oppositeFace && !plusMinusOne && !mixAllDice){
            if (player.isPlacedDie()) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "You have already placed a die"));
                virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie()));
                return;
            }
            virtualView.send(new MessageToolOrder(player.getNickname(), 1, false));
        }
        this.isBeingUsed = true;
    }
}
