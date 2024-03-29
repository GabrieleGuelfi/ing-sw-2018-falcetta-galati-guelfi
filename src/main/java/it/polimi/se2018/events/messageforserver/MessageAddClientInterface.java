package it.polimi.se2018.events.messageforserver;


import it.polimi.se2018.network.client.ClientInterface;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.server.VisitorServer;

public class MessageAddClientInterface extends Message {

    private ClientInterface clientInterface;

    public MessageAddClientInterface(ClientInterface c, String nick){
        super(nick);
        this.clientInterface = c;
    }

    public ClientInterface getClientInterface() {
        return clientInterface;
    }

    @Override
    public void accept(VisitorServer visitorServer){
        visitorServer.visit(this);
    }
}
