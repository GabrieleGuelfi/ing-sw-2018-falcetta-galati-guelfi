package it.polimi.se2018.utils;

import it.polimi.se2018.events.*;

public class VisitorClient implements SagradaVisitor {

    public void visit(MessageChangeValue message){}
    public void visit(MessageChooseDie message){}
    public void visit(MessageChooseMove message){}
    public void visit(MessageNewTurn message){
    }
    public void visit(MessageUpdate message){
    }
    public void visit(Message message){     }
}
