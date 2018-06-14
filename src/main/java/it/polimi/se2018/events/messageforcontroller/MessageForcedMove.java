package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

public class MessageForcedMove extends Message {

    private int row;
    private int column;
    private int newValue;
    private boolean chosen;

    public MessageForcedMove(String nickname, int row, int column, int newValue) {
        super(nickname);
        this.row = row;
        this.column = column;
        this.newValue = newValue;
    }

    public MessageForcedMove(String nickname, int row, int column, int newValue, boolean chosen) {
        super(nickname);
        this.row = row;
        this.column = column;
        this.newValue = newValue;
        this.chosen = chosen;
    }

    public boolean isChosen() {
        return chosen;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getNewValue() {
        return newValue;
    }

    @Override
    public void accept(VisitorController v) {
        v.visit(this);
    }
}
