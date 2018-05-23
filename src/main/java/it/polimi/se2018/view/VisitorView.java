package it.polimi.se2018.view;

import it.polimi.se2018.events.*;
import it.polimi.se2018.utils.SagradaVisitor;

public class VisitorView implements SagradaVisitor {

    public void visit(Message message){
        System.out.println("This is a message!");
    }

    public void visit(MessageError message) {
        System.out.println("This is a message error!");
    }

}
