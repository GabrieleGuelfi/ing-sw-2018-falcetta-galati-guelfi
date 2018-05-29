package it.polimi.se2018.network.rmi.server;

import it.polimi.se2018.events.messageforview.Message;
import it.polimi.se2018.events.messageforview.MessageClientInterface;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.network.socket.server.ServerInterface;
import it.polimi.se2018.utils.Observable;


public class RemoteServer extends Observable implements ServerInterface {

    public RemoteServer() {
        super();
    }


    public void send(Message message){
        this.notifyObservers(message);
    }

    public void addClient(ClientInterface clientInterface, String nickaname) {
        this.notifyObservers(new MessageClientInterface(clientInterface, nickaname));
    }
}
