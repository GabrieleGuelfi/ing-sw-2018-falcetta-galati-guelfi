package it.polimi.se2018.controller;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.MessageDoNothing;
import it.polimi.se2018.events.messageforcontroller.MessageMoveDie;
import it.polimi.se2018.events.messageforcontroller.MessageSetWP;

public interface VisitorController {

    void visit(MessageSetWP message);
    void visit(MessageMoveDie message);
    void visit(MessageDoNothing message);

}
