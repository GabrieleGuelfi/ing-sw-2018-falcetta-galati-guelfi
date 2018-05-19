package it.polimi.se2018.utils;

import it.polimi.se2018.events.*;

public interface SagradaVisitor {

    void visit(Message message);
    void visit(MessageChangeValue message);
    void visit(MessageChooseDie message);
    void visit(MessageChooseMove message);
    void visit(MessageNewTurn message);
    void visit(MessageUpdate message);


}
