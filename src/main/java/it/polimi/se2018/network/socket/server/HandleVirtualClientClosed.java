package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.messageforvie.Message;
import it.polimi.se2018.utils.Observer;

public class HandleVirtualClientClosed implements Observer {

    private SagradaServer sagradaServer;

    HandleVirtualClientClosed(SagradaServer server){
        this.sagradaServer = server;
    }

    public synchronized void update(Message message){
        message.accept(new VisitorServer(this.sagradaServer));
    }
}
