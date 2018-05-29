package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.messageforview.Message;
import it.polimi.se2018.utils.Observer;

public class HandleVirtualClientClosed implements Observer {

    private SagradaServer sagradaServer;
    private VisitorServer visitorServer;

    HandleVirtualClientClosed(SagradaServer server){
        this.sagradaServer = server;
        this.visitorServer = new VisitorServer(this.sagradaServer);
    }

    public synchronized void update(Message message){
        message.accept(this.visitorServer);
        System.out.println("2.HandleVirtualClientClosed");
    }
}
