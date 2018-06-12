package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.events.messageforview.*;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;

import java.util.List;

public class DieSwapper extends Tool {

    DieSwapper(String name) {
        super(name);
    }

    @Override
    public boolean use(MessageToolResponse message, Match match, Player player, Controller controller) {
        if (!match.getRoundTrack().containsKey(message.getDiceFromRoundtrack().get(0))) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "No roundTrack for this turn"));
            return true;
        }
        try {
            Die dieDp = match.getRound().getDraftPool().getBag().get(message.getDiceFromDp());
            Die dieTrack = match.getRoundTrack().get(message.getDiceFromRoundtrack().get(0)).get(message.getDiceFromRoundtrack().get(1));

            match.getRoundTrack().get(message.getDiceFromRoundtrack().get(0)).remove(dieTrack);
            match.getRound().getDraftPool().getBag().remove(dieDp);

            match.getRoundTrack().get(message.getDiceFromRoundtrack().get(0)).add(dieDp);
            match.getRound().getDraftPool().addDie(dieTrack);
        }
        catch (NullPointerException e) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "No dice in this position"));
            return true;
        }
        finishToolMove(player);
        player.setUsedTool(true);
        return true;
    }

    @Override
    public void requestOrders(Player player, Match match) {

        if (canUseTool(player) ) {
            if (match.getRoundTrack().isEmpty()) {
                virtualView.send(new MessageErrorMove(player.getNickname(), "First Turn! Empty RoundTrack"));
                virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie()));
            }
            else {
                this.isBeingUsed = true;
                virtualView.send(new MessageRoundTrack(player.getNickname(), match.getRoundTrack()));
                virtualView.send(new MessageToolOrder(player.getNickname(), 1, 0, 0, 1));
            }
        }

    }
}
