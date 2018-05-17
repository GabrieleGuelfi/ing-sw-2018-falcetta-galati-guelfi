package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageDie;
import it.polimi.se2018.network.socket.client.ClientInterface;

import static it.polimi.se2018.view.View.getView;

public class ServerImplementation implements ServerInterface {

    private final SagradaServer sagradaServer;

    public ServerImplementation(SagradaServer server) {
        this.sagradaServer = server;
    }

    public void send(MessageDie messageDie) {

            getView().notifyController(messageDie);
        }


    public void send(Message message){
        getView().notifyController(message);
    }
}
