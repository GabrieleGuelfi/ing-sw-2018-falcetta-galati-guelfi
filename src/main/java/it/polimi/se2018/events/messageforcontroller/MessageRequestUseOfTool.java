package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

public class MessageRequestUseOfTool extends Message {

    private int numberOfTool;

    public MessageRequestUseOfTool(String nickname, int numberOfTool) {
        super(nickname);
        this.numberOfTool = numberOfTool;
    }

    public int getNumberOfTool() {
        return numberOfTool;
    }

    @Override
    public void accept(VisitorController v) {
        v.visit(this);
    }
}
