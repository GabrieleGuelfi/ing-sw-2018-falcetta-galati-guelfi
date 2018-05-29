package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.network.socket.server.VisitorServer;

public class MessageErrorVirtualClientClosed extends MessageError {
    private ClientInterface clientInterface;

    public MessageErrorVirtualClientClosed(ClientInterface c){
        super();
        this.clientInterface = c;
    }

    public void accept(VisitorServer visitorServer){
        visitorServer.visit(this);
        System.out.println("3.Accept in message");
    }

    public ClientInterface getClientInterface() {
        return clientInterface;
    }
}
