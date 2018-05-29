package it.polimi.se2018.controller;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.MessageSetWP;

public interface VisitorController {

    void visit (Message message);
    void visit(MessageSetWP message);

}
