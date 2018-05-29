package it.polimi.se2018.network.rmi;

import it.polimi.se2018.events.*;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.network.socket.server.ServerInterface;
import it.polimi.se2018.utils.Observable;

import java.rmi.Remote;


public class RemoteServer extends Observable implements ServerInterface, Remote {

    public void send(Message message){
        this.notifyObservers(message);
    }

    public void addClient(ClientInterface clientInterface, String nickaname) {
        this.notifyObservers(new MessageClientInterface(clientInterface, nickaname));
    }
}
