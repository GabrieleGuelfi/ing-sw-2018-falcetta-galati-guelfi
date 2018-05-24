package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.view.VirtualView;

import java.net.Socket;
import java.util.ArrayList;

public class SagradaServer {
    private int PORT = 1111;
    private VirtualView virtualView;
    private Controller controller;
    private ClientGatherer clientGatherer;
    private ServerTimer timer;

    private ArrayList<VirtualClient> clients = new ArrayList<>();
    private Nickname nicknames = new Nickname();

    public SagradaServer() {

        this.controller = new Controller();
        this.virtualView = new VirtualView(this);
        (this.clientGatherer = new ClientGatherer(this, PORT)).start();

    }

    protected synchronized void addClient(Socket clientConnection, String nick) {

        VirtualClient cm = new VirtualClient(this.virtualView, clientConnection);
        clients.add(cm);
        cm.setPlayer(nick);
        cm.start();
        if ((this.clients.size() == 2) && (this.timer == null)) {
            this.timer = new ServerTimer(this, 10000);
            this.timer.run();
        }
        if (this.clients.size() == 4) this.clientGatherer.closeClientGatherer();
    }

    protected synchronized ArrayList<VirtualClient> getClients() {
        return this.clients;
    }

    protected synchronized void removeClient(ClientInterface client) {
        this.clients.remove(client);
        if (this.clients.size() < 4) this.clientGatherer = new ClientGatherer(this, this.PORT);
        if (this.clients.size() == 1) {
            if (this.timer != null) this.timer.stopTimer();
            this.timer = new ServerTimer(this, 10000);
            this.timer.run();
        }
    }

    public VirtualClient searchVirtualClient(String player) {
        try {
            for (VirtualClient v : this.clients) {
                if (v.getPlayer().equals(player)) return v;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void broadcast(Message message) {
        for (VirtualClient v : this.clients) {
            v.notify(message);
        }
    }

    protected void startGame() {
        if (this.clients.size() == 1) {
            this.timer = new ServerTimer(this, 10000);
            this.timer.run();
        } else {
            this.clientGatherer.closeClientGatherer();
            if (this.timer != null) this.timer.stopTimer();
            System.out.println("FUNZIONA");
        }
    }

    public Nickname getNicknames() {
        return nicknames;
    }

    public static void main(String[] args){
        new SagradaServer();
    }
}