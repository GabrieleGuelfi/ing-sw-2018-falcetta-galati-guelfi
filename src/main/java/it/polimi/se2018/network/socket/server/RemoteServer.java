package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforserver.MessageAddClientInterface;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.utils.Observable;


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
