package it.polimi.se2018.controller;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.*;
import it.polimi.se2018.events.messageforview.MessageTool;

public interface VisitorController {

    void visit(MessageSetWP message);
    void visit(MessageMoveDie message);
    void visit(MessageDoNothing message);
    void visit(MessageRequest message);
    void visit(MessageEndGame message);
    void visit(MessageToolResponse message);
    void visit(MessageRequestUseOfTool message);

}
