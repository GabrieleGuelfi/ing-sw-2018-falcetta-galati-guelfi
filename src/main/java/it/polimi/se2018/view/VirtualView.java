package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.MessageDoNothing;
import it.polimi.se2018.network.client.ClientInterface;
import it.polimi.se2018.network.client.ConnectionHandlerThread;
import it.polimi.se2018.network.server.SagradaServer;
import it.polimi.se2018.network.server.ServerInterface;
import it.polimi.se2018.utils.*;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * VirtualView is used by the controller and the model to simulate a View
 * This class will reorder the messages to their destination
 *
 */
public class VirtualView extends Observable implements Observer, ServerInterface {

    private SagradaServer sagradaServer;

    public VirtualView( SagradaServer s){
        this.sagradaServer = s;

    }

    public void update(Message message){
        try {
            this.sagradaServer.broadcast(message);
        }
        catch (NullPointerException e) {

        }
    }

    public void send(Message message){
        try {
            ClientInterface clientInterface =this.sagradaServer.searchVirtualClient(message.getNickname());
            if(clientInterface == null) (new ConnectionHandlerThread(this, new MessageDoNothing(message.getNickname()))).start();
            else clientInterface.notify(message);
        }
        catch(RemoteException | NullPointerException e){
            final Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.FINE, e.getMessage());
        }
    }

    @Override
    public void addClient(ClientInterface clientInterface, String nickname) {

    }

    public void sendToServer(Message message){
        if (this.sagradaServer!=null)
            message.accept(this.sagradaServer);
    }

}
