package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

public class MessageSetWP extends Message {


    private int firstIndex;
    private int secondIndex;

    public MessageSetWP(String nickname, int firstIndex, int secondIndex) {
        super(nickname);
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
        this.noTurn = true;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public int getSecondIndex() {
        return secondIndex;
    }

    @Override
    public void accept(VisitorController v) {
        v.visit(this);
    }

}
