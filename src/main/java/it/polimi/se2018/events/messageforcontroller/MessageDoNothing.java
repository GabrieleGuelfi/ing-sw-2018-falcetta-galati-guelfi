package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

public class MessageDoNothing extends Message {

    public MessageDoNothing(String nickname) {
        super(nickname);
    }

    @Override
    public void accept(VisitorController v) {
        v.visit(this);
    }
}
