package it.polimi.se2018.utils;

import it.polimi.se2018.events.*;

public class VisitorClient implements SagradaVisitor {

    public void visit(Message message){
        System.out.println("This is a message!");
    }

    public void visit(MessageError message) {
        System.out.println("This is a message error!");
    }

}
