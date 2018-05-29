package it.polimi.se2018.events;


import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.network.socket.server.VisitorServer;

public class MessageClientInterface extends Message {

    private ClientInterface clientInterface;

    public MessageClientInterface(ClientInterface c, String nick){
        super();
        this.clientInterface = c;
        super.nickname = nick;
    }

    public ClientInterface getClientInterface() {
        return clientInterface;
    }

    public void accept(VisitorServer visitorServer){
        visitorServer.visit(this);
    }
}
