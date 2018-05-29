package it.polimi.se2018.controller;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.*;
import it.polimi.se2018.utils.SagradaVisitor;

public abstract class VisitorController implements SagradaVisitor {

    public void visit(MessageError messageError){}

}
