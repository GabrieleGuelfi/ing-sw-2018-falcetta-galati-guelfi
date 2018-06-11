package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;

public class DieSwapper extends Tool {

    public DieSwapper(String name) {
        super(name);
    }

    @Override
    public boolean use(MessageToolResponse message, Match match, Player player, Controller controller) {
        return true;
    }

    @Override
    public void requestOrders(Player player, Match match) {

    }
}
