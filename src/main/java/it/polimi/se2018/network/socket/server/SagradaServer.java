package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageNickname;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.view.View;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class SagradaServer extends Thread {
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

    public void setNickname(MessageNickname message) {
        int index = clients.indexOf(message.getVirtualClient());
        clients.get(index).setPlayer(new Player(message.getNickname()));
        out.println("Nickname set for " + message.getNickname());
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
        if (this.clients.size()==2) start();


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
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        try {
            for(VirtualClient vc: clients) {
                vc.notify(new Message("Match will start in 10 seconds..."));
            }
            TimeUnit.SECONDS.sleep(10);
            List<Player> players = new ArrayList<>();
            players.add(clients.get(0).getPlayer());
            players.add(clients.get(1).getPlayer());
            out.println("Match started!!");
            Controller controller = new Controller();
            controller.startGame(players, View.createView(controller, this));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SagradaServer();
    }
}