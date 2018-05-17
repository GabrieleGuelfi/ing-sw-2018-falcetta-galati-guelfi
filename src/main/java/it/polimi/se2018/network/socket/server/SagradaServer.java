package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.socket.client.ClientInterface;

import java.net.Socket;
import java.util.ArrayList;

public class SagradaServer {
    private static int PORT = 1111;
    private static ServerInterface server;
    private ClientGatherer clientGatherer;

    private ArrayList<VirtualClient> clients = new ArrayList<VirtualClient>();

    public SagradaServer() {

        // servizio offerto ai client
        this.server = new ServerImplementation(this);

        // Avvio il ClientGatherer, un nuovo thread che si occupa di gestire la connessione di nuovi client
        (this.clientGatherer = new ClientGatherer(this, PORT)).start();

    }

    protected synchronized void addClient( Socket clientConnection ) {

        VirtualClient cm = new VirtualClient(this, clientConnection);
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

    protected synchronized ServerInterface getImplementation() {
        return server;
    }

    public VirtualClient searchVirtualClient(Player player){
        for(VirtualClient v : this.clients){
            if(v.getPlayer().equals(player)) return v;
        }
        return null;
    }

    public static void main(String[] args) {
        new SagradaServer();
    }
}