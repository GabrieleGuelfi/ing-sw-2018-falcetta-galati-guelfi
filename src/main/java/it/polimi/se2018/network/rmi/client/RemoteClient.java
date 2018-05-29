package it.polimi.se2018.network.rmi.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.network.socket.server.ServerInterface;
import it.polimi.se2018.view.VisitorView;

public class RemoteClient implements ClientInterface {


    @Override
    public void notify(Message message) {
        message.accept(new VisitorView());
    }
}
