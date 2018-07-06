package it.polimi.se2018.network.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforserver.MessageAddClientInterface;
import it.polimi.se2018.network.client.ClientInterface;
import it.polimi.se2018.utils.Observable;

/**
 * RMI server
 */

public class RemoteServer extends Observable implements ServerInterface {

    RemoteServer() {
        super();
    }

    public void send(Message message){
        this.notifyObservers(message);

    }

    public void addClient(ClientInterface clientInterface, String nickaname) {
        this.notifyObservers(new MessageAddClientInterface(clientInterface, nickaname));
    }
}
