package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;

public class ServerImplementation implements ServerInterface {

    private final SagradaServer sagradaServer;

    public ServerImplementation(SagradaServer server) {
        this.sagradaServer = server;
    }

    public void send(Message message){
        message.setServer(sagradaServer);
        message.notifyThis();
    }
}
