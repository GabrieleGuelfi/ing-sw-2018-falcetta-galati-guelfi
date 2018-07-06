package it.polimi.se2018.network.server;


import it.polimi.se2018.events.messageforserver.MessageErrorVirtualClientClosed;
import it.polimi.se2018.events.messageforserver.MessagePing;
import it.polimi.se2018.network.client.ClientInterface;
import it.polimi.se2018.utils.Observable;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VerifyConnectionRmi extends Observable implements Runnable {
    private ClientInterface clientInterface;
    private boolean loop;

    VerifyConnectionRmi(ClientInterface c){
        this.clientInterface = c;
        loop = true;
    }

    @Override
    public void run(){
        try{
            while(loop) {
                this.clientInterface.notify(new MessagePing());
                Thread.sleep(2000);
            }
        }
        catch(RemoteException e){
            this.notifyObservers(new MessageErrorVirtualClientClosed(this.clientInterface));
            this.loop = false;
            Thread.currentThread().interrupt();
        }
        catch (InterruptedException e ){
            final Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, e.getMessage());
            Thread.currentThread().interrupt();
        }

    }

    protected void closeThread(){
        this.loop = false;
        Thread.currentThread().interrupt();
    }

}
