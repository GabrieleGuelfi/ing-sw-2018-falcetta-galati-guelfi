package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.events.messageforview.MessageConfirmMove;
import it.polimi.se2018.events.messageforview.MessageDPChanged;
import it.polimi.se2018.events.messageforview.MessageErrorMove;
import it.polimi.se2018.events.messageforview.MessageToolOrder;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;

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
        if(message.getDiceFromDp().get(0)<0 || message.getDiceFromDp().get(0)>match.getRound().getDraftPool().size()-1) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "Invalid choice of die from draftpool", player.isPlacedDie(), player.isUsedTool()));
            return false;
        }
        if(mixAllDice)
            for (Die d : match.getRound().getDraftPool().getBag())
                d.setRandomValue();
        if(plusMinusOne) {
            Die die = match.getRound().getDraftPool().getBag().get(message.getDiceFromDp().get(0));
            if (die.getValue()==6 && message.getPlusOne()) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "Can't add 1 to a die with value 6", player.isPlacedDie(), player.isUsedTool()));
                return false;
            }
            if (die.getValue()==1 && !message.getPlusOne()) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "Can't remove 1 to a die with value 1", player.isPlacedDie(), player.isUsedTool()));
                return false;
            }

            if(message.getPlusOne()) {
                die.setValue(die.getValue()+1);
            } else {
                die.setValue(die.getValue()-1);
            }

        }
        match.notifyObservers(new MessageDPChanged(match.getRound().getDraftPool()));
        return finishToolMove(player, controller, match);
    }

    @Override
    public void requestOrders(Player player) {
        if(!canUseTool(player)) return;
        if (plusMinusOne) virtualView.send(new MessageToolOrder(player.getNickname(), 1, 0, true));
        if (oppositeFace) virtualView.send(new MessageToolOrder(player.getNickname(), 1, 0, false));
        if (!oppositeFace && !plusMinusOne) virtualView.send(new MessageToolOrder(player.getNickname(), 1, 1, false));
        this.isBeingUsed = true;
    }
}
