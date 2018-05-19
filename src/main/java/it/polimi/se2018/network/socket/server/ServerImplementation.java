package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VirtualView;

public class ServerImplementation implements ServerInterface {

    private final SagradaServer sagradaServer;
    private VirtualView view;

    public ServerImplementation(SagradaServer server) {
        this.sagradaServer = server;
    }

    public void send(Message message){
        view.notifyObservers(message);
    }

    public void broadcast(Message message){
        for(VirtualClient v : sagradaServer.getClients()){
            v.notify(message);
        }
    }

    public void notifyClient(Message message){
        try {
            for(VirtualClient v : this.sagradaServer.getClients()){
                if(v.getPlayer().equals(message.getPlayer())) v.notify(message);
            }
        }
        catch(NullPointerException e) {
            System.out.println("fail qua");
        }

    }
}
