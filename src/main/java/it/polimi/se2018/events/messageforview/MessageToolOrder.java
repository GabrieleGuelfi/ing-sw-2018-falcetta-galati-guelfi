package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageToolOrder extends Message {

    private int diceFromDp = 0;
    private int diceFromWp = 0;
    private int positionInWp = 0;
    private boolean askPlusOrMinusOne = false;
    private int diceFromRoundtrack = 0;
    private boolean canReduceDiceFromWP;

    public MessageToolOrder(String nickname, int diceFromWp, int positionInWp, boolean canReduceDiceFromWP) {
        super(nickname);
        this.diceFromWp = diceFromWp;
        this.positionInWp = positionInWp;
        this.canReduceDiceFromWP = canReduceDiceFromWP;
    }

    public MessageToolOrder(String nickname, int diceFromDp, boolean askPlusOrMinusOne) {
        super(nickname);
        this.diceFromDp = diceFromDp;
        this.askPlusOrMinusOne = askPlusOrMinusOne;
    }

    public MessageToolOrder(String nickname, int diceFromDp, int diceFromWp, int positionInWp, int diceFromRoundtrack) {
        super(nickname);
        this.diceFromDp = diceFromDp;
        this.diceFromWp = diceFromWp;
        this.positionInWp = positionInWp;
        this.diceFromRoundtrack = diceFromRoundtrack;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }

    public int getDiceFromDp() {
        return diceFromDp;
    }

    public int getDiceFromWp() {
        return diceFromWp;
    }

    public int getPositionInWp() {
        return positionInWp;
    }

    public int getDiceFromRoundtrack() {
        return diceFromRoundtrack;
    }

    public boolean isAskPlusOrMinusOne() {
        return this.askPlusOrMinusOne;
    }

    public boolean isCanReduceDiceFromWP() {
        return canReduceDiceFromWP;
    }
}
