package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.messageforcontroller.MessageToolResponse;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;

public class DieMover extends Tool {

    private int numberOfDice;
    private boolean respectColour;
    private boolean respectValue;
    private boolean respectRoundtrack;

    public DieMover(String name, int numberOfDice, boolean respectColour, boolean respectValue, boolean respectRoundtrack) {
        super(name);
        this.numberOfDice = numberOfDice;
        this.respectColour = respectColour;
        this.respectValue = respectValue;
        this.respectRoundtrack = respectRoundtrack;
    }

    @Override
    public boolean use(MessageToolResponse message, Match match, Player player) {
        return true;
    }

    @Override
    public void requestOrders(Player player) {

    }
}
