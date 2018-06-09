package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

import java.util.List;
import java.util.Map;

public class MessageToolResponse extends Message {

    private List<Integer> diceFromDp;
    private Map<Integer, Integer> diceFromWp;
    private List<Integer> diceFromRoundtrack;
    private Map<Integer, Integer> positionsInWp;
    private int newValue;
    private boolean plusOne;

    public MessageToolResponse(String nickname, List<Integer> diceFromDp, Map<Integer, Integer> diceFromWp, List<Integer> diceFromRoundtrack, Map<Integer, Integer> positionsInWp, int newValue, boolean plusOne) {
        super(nickname);
        this.diceFromDp = diceFromDp;
        this.diceFromWp = diceFromWp;
        this.diceFromRoundtrack = diceFromRoundtrack;
        this.positionsInWp = positionsInWp;
        this.newValue = newValue;
        this.plusOne = plusOne;
    }

    public List<Integer> getDiceFromDp() {
        return diceFromDp;
    }

    public Map<Integer, Integer> getDiceFromWp() {
        return diceFromWp;
    }

    public List<Integer> getDiceFromRoundtrack() {
        return diceFromRoundtrack;
    }

    public Map<Integer, Integer> getPositionsInWp() {
        return positionsInWp;
    }

    public int getNewValue() {
        return newValue;
    }

    public boolean getPlusOne() {
        return plusOne;
    }

    @Override
    public void accept(VisitorController v) {
        v.visit(this);
    }
}
