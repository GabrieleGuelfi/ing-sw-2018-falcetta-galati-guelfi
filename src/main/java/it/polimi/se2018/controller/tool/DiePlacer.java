package it.polimi.se2018.controller.tool;

import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.events.messageforview.MessageAskMove;
import it.polimi.se2018.events.messageforview.MessageErrorMove;
import it.polimi.se2018.events.messageforview.MessageForceMove;
import it.polimi.se2018.events.messageforview.MessageToolOrder;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.StringJSON;

import java.util.List;
import java.util.Random;

import static it.polimi.se2018.controller.VerifyRules.*;

public class DiePlacer extends Tool {

    private boolean respectDistance;
    private boolean takeFromBag;

    DiePlacer(List<String> name, boolean respectDistance, boolean takeFromBag) {
        super(name);
        this.respectDistance = respectDistance;
        this.takeFromBag = takeFromBag;
    }

    @Override
    public boolean use(MessageToolResponse message, Match match, Player player) {

        Die die = match.getRound().getDraftPool().getBag().get(message.getDiceFromDp());


        if(!respectDistance && takeFromBag) {
            match.getRound().getDraftPool().getBag().remove(die);
            match.getBag().addDie(die);
            Random generator = new Random();
            Die d = match.getBag().removeDie(generator.nextInt(match.getBag().getBag().size()));
            match.getRound().getDraftPool().addDie(d);
            d.setRandomValue();
            d.setPlacing(true);
            if(this.used) player.removeFavorTokens(2);
            else {
                this.used = true;
                player.removeFavorTokens(1);
            }
            player.setUsedTool(true);
            this.isBeingUsed = false;
            virtualView.send(new MessageForceMove(player.getNickname(), d, player.getWindowPattern(), true, player.isPlacedDie(), true));
        }
        else {
            int row = message.getPositionsInWp().get(0)[0];
            int column = message.getPositionsInWp().get(0)[1];

            if (respectDistance && !takeFromBag) {
                if (isNearDie(player.getWindowPattern(), row, column)) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorTool","isNearDie")));
                    return true;
                }

                if (!verifyColor(player.getWindowPattern(), row, column, die)) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorMove","colourRestriction")));
                    return true;
                }

                if (!verifyNumber(player.getWindowPattern(), row, column, die)) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorMove","valueRestriction")));
                    return true;
                }

                match.getRound().getDraftPool().getBag().remove(die);
                player.getWindowPattern().putDice(die, row, column);
                player.setPlacedDie(true);
                finishToolMove(player);

                return true;
            }
            if (!takeFromBag) {

                if (!isNearDie(player.getWindowPattern(), row, column)) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorMove","nearDie")));
                    return true;
                }

                if (!verifyColor(player.getWindowPattern(), row, column, die)) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorMove","colourRestriction")));
                    return true;
                }

                if (!verifyNumber(player.getWindowPattern(), row, column, die)) {
                    virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorMove","valueRestriction")));
                    return true;
                }

                match.getRound().getDraftPool().getBag().remove(die);
                player.getWindowPattern().putDice(die, row, column);
                player.setPlacedDie(true);
                finishToolMove(player);

                return false;
            }
        }
        return false;
    }

    @Override
    public void requestOrders(Player player, Match match) {

        if(!canUseTool(player)) return;
        if(!respectDistance && !takeFromBag) {
            if(match.getRound().getNumTurn() > match.getPlayers().size()) {
                virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorTool","secondTurn")));
                virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie()));
                return;
            }
            else virtualView.send(new MessageToolOrder(player.getNickname(), 1, 0, 1, 0));
        }
        if(respectDistance && !takeFromBag) {
            if(player.isPlacedDie())  {
                virtualView.send(new MessageErrorMove(player.getNickname(), StringJSON.printStrings("errorTool","alreadyPlaced")));
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
