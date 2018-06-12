package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforview.MessageAskMove;
import it.polimi.se2018.events.messageforview.MessageErrorMove;
import it.polimi.se2018.events.messageforview.MessageForceMove;
import it.polimi.se2018.events.messageforview.MessageToolOrder;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPattern;

import java.util.Random;

public class DiePlacer extends Tool {

    private boolean respectDistance;
    private boolean takeFromBag;

    DiePlacer(String name, boolean respectDistance, boolean takeFromBag) {
        super(name);
        this.respectDistance = respectDistance; //true
        this.takeFromBag = takeFromBag; //false
    }

    @Override
    public boolean use(MessageToolResponse message, Match match, Player player, Controller controller) {

        Die die = match.getRound().getDraftPool().getBag().get(message.getDiceFromDp());


        if(!respectDistance && takeFromBag) {
            match.getRound().getDraftPool().getBag().remove(die);
            match.getBag().addDie(die);
            Random generator = new Random();
            Die d = match.getBag().removeDie(generator.nextInt(match.getBag().getBag().size()));
            d.setRandomValue();
            d.setPlacing(true);
            virtualView.send(new MessageForceMove(player.getNickname(), d, player.getWindowPattern(), true, player.isPlacedDie()));
        }
        else {
            int row = message.getPositionsInWp().get(0)[0];
            int column = message.getPositionsInWp().get(0)[1];

            if (respectDistance && !takeFromBag) {
                if (controller.isNearDie(player.getWindowPattern(), row, column)) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), "There is a die near the position"));
                    return true;
                }

                if (!controller.verifyColor(player.getWindowPattern(), row, column, die)) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), "Violated colour restriction"));
                    return true;
                }

                if (!controller.verifyNumber(player.getWindowPattern(), row, column, die)) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), "Violated value restriction"));
                    return true;
                }

                match.getRound().getDraftPool().getBag().remove(die);
                player.getWindowPattern().putDice(die, row, column);
                player.setPlacedDie(true);
                finishToolMove(player);

                return true;
            }
            if (!takeFromBag && !respectDistance) {

                if (!controller.isNearDie(player.getWindowPattern(), row, column)) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), "Violated near dice restriction"));
                    return true;
                }

                if (!controller.verifyColor(player.getWindowPattern(), row, column, die)) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), "Violated colour restriction"));
                    return true;
                }

                if (!controller.verifyNumber(player.getWindowPattern(), row, column, die)) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), "Violated value restriction"));
                    return true;
                }

                match.getRound().getDraftPool().getBag().remove(die);
                player.getWindowPattern().putDice(die, row, column);
                player.setPlacedDie(true);
                finishToolMove(player);

                return false;
            }
        }
        return true;
    }

    @Override
    public void requestOrders(Player player, Match match) {

        if(!canUseTool(player)) return;
        if(!respectDistance && !takeFromBag) {
            if(match.getRound().getNumTurn() > match.getPlayers().size()) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "You can't use this tool in your second turn!"));
                virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie()));
                return;
            }
            else virtualView.send(new MessageToolOrder(player.getNickname(), 1, 0, 1, 0));
        }
        if(respectDistance && !takeFromBag) {
            if(player.isPlacedDie())  {
                virtualView.send(new MessageErrorMove(player.getNickname(), "You have already placed a die in this turn!"));
                virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie()));
                return;
            }
            else virtualView.send(new MessageToolOrder(player.getNickname(), 1, 0, 1, 0));
        }
        if(!respectDistance && takeFromBag) {
            virtualView.send(new MessageToolOrder(player.getNickname(), 1, 0, 0, 0));
        }
        this.isBeingUsed = true;
    }
}
