package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

public class MessageEndGame extends Message{
    public MessageEndGame(String nickname){
        super(nickname);
    }

    @Override
    public void accept(VisitorController visitor){
        visitor.visit(this);
    }
}
