package it.polimi.se2018.network.socket.server;


import it.polimi.se2018.events.messageforserver.MessageErrorVirtualClientClosed;
import it.polimi.se2018.events.messageforserver.MessagePing;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.utils.Observable;

import java.rmi.RemoteException;

public class VerifyConnectionRmi extends Observable implements Runnable {
    private ClientInterface clientInterface;
    private boolean loop;

    protected VerifyConnectionRmi(ClientInterface c){
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
            //e.printStackTrace();
            this.notifyObservers(new MessageErrorVirtualClientClosed(this.clientInterface));
            this.loop = false;
            Thread.currentThread().interrupt();
        }
        catch (InterruptedException e ){
            e.printStackTrace();
        }

    }

    protected void closeThread(){
        this.loop = false;
        Thread.currentThread().interrupt();
    }

}
