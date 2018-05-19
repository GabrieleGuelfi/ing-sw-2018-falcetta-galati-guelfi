package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.view.View;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
            if (this.clients.size() == 2) {
                Player player = new Player("foo");
                Player player2 = new Player("bar");
                clients.get(0).setPlayer(player);
                clients.get(1).setPlayer(player2);
                List<Player> players = new ArrayList<>();
                players.add(player);
                players.add(player2);
                System.out.println("Partita iniziata!");
                Controller controller = new Controller();
                controller.startGame(players, View.createView(controller, this));
            }
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
        try {
            for(VirtualClient v : this.clients){
                if(v.getPlayer().equals(player)) return v;
            }
        }
        catch(NullPointerException e) {
            System.out.println("fail qua");
        }
        return null;
    }

    public static void main(String[] args) {
        new SagradaServer();
    }
}