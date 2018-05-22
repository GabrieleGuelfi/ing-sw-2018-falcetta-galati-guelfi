package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.view.VirtualView;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class SagradaServer {
    private static int PORT = 1111;
    private VirtualView virtualView;
    private Controller controller;
    private ClientGatherer clientGatherer;

    private ArrayList<VirtualClient> clients = new ArrayList<VirtualClient>();

    public SagradaServer() {

        // Avvio il ClientGatherer, un nuovo thread che si occupa di gestire la connessione di nuovi client
        (this.clientGatherer = new ClientGatherer(this, PORT)).start();

    }

    protected synchronized void addClient( Socket clientConnection ) {

        VirtualClient cm = new VirtualClient(this.virtualView, clientConnection);
        clients.add(cm);
        cm.start();
        try {
            if (this.clients.size() >= 4) this.clientGatherer.wait();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    protected synchronized ArrayList<VirtualClient> getClients() {
        return this.clients;
    }

    protected synchronized void removeClient(ClientInterface client) {
        this.clients.remove(client);
        if(this.clients.size() < 4) clientGatherer.notify();
    }

    public VirtualClient searchVirtualClient(String player){
        try {
            for(VirtualClient v : this.clients){
                if(v.getPlayer().equals(player)) return v;
            }
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void broadcast(Message message){
        for(VirtualClient v: this.clients){
            v.notify(message);
        }
    }


    public static void main(String[] args) {
        new SagradaServer();
    }
}