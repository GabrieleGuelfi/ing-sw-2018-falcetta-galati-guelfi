package it.polimi.se2018.events;

import it.polimi.se2018.network.socket.server.SagradaServer;
import it.polimi.se2018.network.socket.server.VirtualClient;
import it.polimi.se2018.view.View;

import java.io.Serializable;

/**
 * @author Federico Galati
 *
 */
public class Message implements Serializable  {
    String s;
    VirtualClient vc;
    SagradaServer server;

    public Message(String string){

        s = string;
    }

    public void setServer(SagradaServer s) {
        this.server = s;
    }

    public SagradaServer getServer() {
        return this.server;
    }

    public void setVirtualClient(VirtualClient vc) {
        this.vc = vc;
    }

    public VirtualClient getVirtualClient() {
        return this.vc;
    }

    public String getString() {
        return s;
    }

    public void notifyThis() {
        View.getView().notifyObservers(this);
    }

}
