package it.polimi.se2018.view;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.*;
import it.polimi.se2018.network.socket.server.*;
import it.polimi.se2018.utils.*;

public class VirtualView extends Observable implements Observer, ServerInterface{

    private Controller controller;
    private SagradaServer sagradaServer;

    public void VirtualView(Controller c, SagradaServer s){
        this.controller = c;
        this.sagradaServer = s;

    }

    public void update(Message message){
        this.sagradaServer.broadcast(message);
    }

    public void send(Message message){
        this.sagradaServer.searchVirtualClient(message.getNickname()).notify(message);
    }

}
