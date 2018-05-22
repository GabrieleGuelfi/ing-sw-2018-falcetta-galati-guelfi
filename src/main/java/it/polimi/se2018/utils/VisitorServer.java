package it.polimi.se2018.utils;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.*;

public abstract class VisitorServer implements SagradaVisitor{
    Controller controller;

    public void visit(Message message){}
    public void visit(MessageError messageError){}
}
