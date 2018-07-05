package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.MessageDoNothing;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.network.socket.client.ConnectionHandlerThread;
import it.polimi.se2018.network.socket.server.*;
import it.polimi.se2018.utils.*;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VirtualView extends Observable implements Observer, ServerInterface{

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
        message.accept(this.sagradaServer);
    }

}
