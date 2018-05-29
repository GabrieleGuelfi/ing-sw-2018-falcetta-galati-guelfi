package it.polimi.se2018.events.messageforview;


import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.events.Message;

public class MessageClientInterface extends Message {

    private ClientInterface clientInterface;

    public MessageClientInterface(ClientInterface c, String nick){
        super(nick);
        this.clientInterface = c;
    }

    public ClientInterface getClientInterface() {
        return clientInterface;
    }

    public void accept(VisitorServer visitorServer){
        visitorServer.visit(this);
    }
}
