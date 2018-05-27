package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.*;
import it.polimi.se2018.events.MessageErrorVirtualClientClosed;
import it.polimi.se2018.utils.SagradaVisitor;

public class VisitorServer implements SagradaVisitor{

    SagradaServer sagradaServer;

    VisitorServer(SagradaServer s){
        this.sagradaServer = s;
    }

    public void visit(MessageErrorVirtualClientClosed message){
        this.sagradaServer.removeClient(message.getClientInterface());
    }
    public void visit(Message message){}
    public void visit(MessageError messageError){}
}
