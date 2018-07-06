package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

public class MessageClientDisconnected extends Message{

    private boolean matchHasToFinish;

    public MessageClientDisconnected(String nickname, boolean matchHasToFinish){
        super(nickname);
        noTurn = true;
        this.matchHasToFinish = matchHasToFinish;
    }

    public boolean isMatchHasToFinish() {
        return matchHasToFinish;
    }

    @Override
    public void accept(VisitorController visitor){
        visitor.visit(this);
    }
}
