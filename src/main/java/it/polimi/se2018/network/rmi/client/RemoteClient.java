package it.polimi.se2018.network.rmi.client;

import it.polimi.se2018.events.messageforview.Message;
import it.polimi.se2018.network.socket.client.ClientInterface;

public class RemoteClient implements ClientInterface {


    @Override
    public void notify(Message message) {
        client.notify(message);
    }
}
