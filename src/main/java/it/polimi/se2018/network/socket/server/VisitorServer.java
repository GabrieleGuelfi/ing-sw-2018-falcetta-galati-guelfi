package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.messageforview.*;

public class VisitorServer implements SagradaVisitor{

    SagradaServer sagradaServer;

    public VisitorServer(SagradaServer s) {
        this.sagradaServer = s;
    }

    public void visit(MessageErrorVirtualClientClosed message){
        this.sagradaServer.removeClient(message.getClientInterface());
        System.out.println("4.Visitor");
    }
    public void visit(Message message){
        System.out.println("Message from VisitorServer");
    }
    public void visit(MessageError message){}
    public void visit(MessageNickname message){
        this.sagradaServer.getVirtualView().notifyObservers(message);
    }
    public void visit(MessagePrivObj message){}
    public void visit(MessagePublicObj message){}

    @Override
    public void visit(MessageChooseWP message) {

    }

    @Override
    public void visit(MessageWPChanged message) {

    }

    @Override
    public void visit(MessageTurnChanged message) {

    }

    @Override
    public void visit(MessageDPChanged message) {

    }

    public void visit(MessageClientInterface message){
        boolean valueAccess = this.sagradaServer.getNicknames().verifyNickname(message.getNickname());
        if(valueAccess) this.sagradaServer.addClient(message.getClientInterface(), message.getNickname());
        else message.getClientInterface().notify(new Message());
    }

}
