package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.socket.server.ServerInterface;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.view.View;

import java.rmi.RemoteException;

public class ClientImplementation extends Observable implements ClientInterface, Observer {

    private ServerInterface server;

    void addServer(ServerInterface server) {
        this.server = server;
    }

    public void notify(Message message) {
        if(message.isTimeFinished()) SagradaClient.stopTurn();
        else {
            (new ConnectionHandlerThread(this, message)).start();
        }
    }

    @Override
    public void update(Message m) {
        try {
            server.send(m);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){}
}
