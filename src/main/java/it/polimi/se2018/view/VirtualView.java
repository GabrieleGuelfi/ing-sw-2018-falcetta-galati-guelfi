package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.network.socket.server.*;
import it.polimi.se2018.utils.*;

public class VirtualView extends Observable implements Observer, ServerInterface{

    private SagradaServer sagradaServer;

    public VirtualView( SagradaServer s){
        this.sagradaServer = s;

    }

    public void update(Message message){
        this.sagradaServer.broadcast(message);
    }

    public void send(Message message){
        this.sagradaServer.searchVirtualClient(message.getNickname()).notify(message);
    }

    @Override
    public void addClient(ClientInterface clientInterface, String nickname) {

    }

}
