package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

public class MessageMoveDie extends Message {

    private int dieFromDraftPool;
    private int row;
    private int column;

    public MessageMoveDie(String nickname, int dieFromDraftPool, int row, int column) {
        super(nickname);
        this.column = column;
        this.dieFromDraftPool = dieFromDraftPool;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getDieFromDraftPool() {
        return dieFromDraftPool;
    }

    public int getRow() {
        return row;
    }

    @Override
    public void accept(VisitorController v) {
        v.visit(this);
    }
}
