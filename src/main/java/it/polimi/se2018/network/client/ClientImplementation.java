package it.polimi.se2018.network.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.server.ServerInterface;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for the functioning of the Client
 * In RMI this is the class directly used from the server
 * In Socket, NetworkHandler will contact this class after the socket parsing
 */

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
            final Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void closeConnection(){}
}
