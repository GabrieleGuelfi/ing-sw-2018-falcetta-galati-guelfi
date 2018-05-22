package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.socket.server.ServerInterface;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;

public class ClientImplementation extends Observable implements ClientInterface, Observer {

    private ServerInterface server;

    public void addServer(ServerInterface server) {
        this.server = server;
    }

    public void notify(Message message) {
        notifyObservers(message);
    }

    @Override
    public void update(Message m) {
        server.send(m);
    }
}
