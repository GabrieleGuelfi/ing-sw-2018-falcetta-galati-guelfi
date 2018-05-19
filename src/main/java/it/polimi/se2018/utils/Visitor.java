package it.polimi.se2018.utils;

import com.sun.security.ntlm.Server;
import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.*;
import it.polimi.se2018.network.socket.server.SagradaServer;
import it.polimi.se2018.network.socket.server.ServerImplementation;
import it.polimi.se2018.view.VirtualView;

public class Visitor implements SagradaVisitor {

    Controller controller;
    ServerImplementation sagradaServer;

    public Visitor(Controller c){
        this.controller = c;
    }

    public Visitor(ServerImplementation s){
        this.sagradaServer = s;
    }

    public void visit(MessageChangeValue message){}
    public void visit(MessageChooseDie message){}
    public void visit(MessageChooseMove message){}
    public void visit(MessageNewTurn message){
        this.sagradaServer.broadcast(message);
    }
    public void visit(MessageUpdate message){
        this.sagradaServer.broadcast(message);
    }
    public void visit(Message message){
        this.sagradaServer.send(message);
    }
}
