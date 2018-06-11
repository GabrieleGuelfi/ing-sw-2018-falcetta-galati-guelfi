package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

public class MessageForcedMove extends Message {

    private int row;
    private int column;

    public MessageForcedMove(String nickname, int row, int column) {
        super(nickname);
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public void accept(VisitorController v) {
        v.visit(this);
    }
}
