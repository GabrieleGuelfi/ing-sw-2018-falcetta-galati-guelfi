package it.polimi.se2018.events;

import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.utils.SagradaVisitor;

public class MessageErrorCloseVirtualClient extends MessageError {
    ClientInterface clientInterface;

    public MessageErrorCloseVirtualClient(ClientInterface c){
        this.clientInterface = c;
    }

    public void accept(SagradaVisitor s){
        s.visit(this);
    }
}
