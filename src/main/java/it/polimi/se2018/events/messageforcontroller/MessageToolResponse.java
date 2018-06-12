package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

import java.util.List;
import java.util.Map;

public class MessageToolResponse extends Message {

    private boolean confirmUse;
    private int diceFromDp;
    private List<Integer[]> positionsInWp;
    private List<Integer[]> diceFromWp;
    private List<Integer> diceFromRoundtrack;
    private boolean plusOne;

    public MessageToolResponse(String nickname, int diceFromDp, List<Integer[]> diceFromWp, List<Integer> diceFromRoundtrack, List<Integer[]> positionsInWp, boolean plusOne) {
        super(nickname);
        this.confirmUse = true;
        this.diceFromDp = diceFromDp;
        this.diceFromWp = diceFromWp;
        this.diceFromRoundtrack = diceFromRoundtrack;
        this.positionsInWp = positionsInWp;
        this.plusOne = plusOne;
    }

    public MessageToolResponse(String nickname) {
        super(nickname);
        this.confirmUse = false;
    }

    public int getDiceFromDp() {
        return diceFromDp;
    }

    public List<Integer[]> getDiceFromWp() {
        return diceFromWp;
    }

    public List<Integer> getDiceFromRoundtrack() {
        return diceFromRoundtrack;
    }

    public List<Integer[]> getPositionsInWp() {
        return positionsInWp;
    }

    public boolean getPlusOne() {
        return plusOne;
    }

    public boolean isConfirmUse() {
        return confirmUse;
    }

    @Override
    public void accept(VisitorController v) {
        v.visit(this);
    }
}
