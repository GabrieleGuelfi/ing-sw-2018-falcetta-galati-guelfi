package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.view.VirtualView;

import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class SagradaServer {
    private int PORT = 1111;
    private VirtualView virtualView;
    private Controller controller;
    private ClientGatherer clientGatherer;
    private ServerTimer timer;
    private final int time;

    private ArrayList<CoupleClientNickname> clients = new ArrayList<>();
    private Nickname nicknames = new Nickname();

    public SagradaServer() {

        Scanner stdin = new Scanner(System.in);
        System.out.println("Set time for lobby timer.");
        this.time = stdin.nextInt();
        stdin.close();
        this.controller = new Controller();
        this.virtualView = new VirtualView(this);
        this.clientGatherer = new ClientGatherer(this, PORT);
        this.clientGatherer.start();
    }

    protected synchronized void addClient(Socket clientConnection, String nick) {

        VirtualClient cm = new VirtualClient(this.virtualView, clientConnection);
        clients.add(new CoupleClientNickname(cm, nick));
        cm.start();
        if ((this.clients.size() == 2) && (this.timer == null)) {
            this.timer = new ServerTimer(this, this.time);
            this.timer.start();
        }
        if (this.clients.size() == 4) this.clientGatherer.closeClientGatherer();
    }

    protected synchronized ArrayList<CoupleClientNickname> getClients() {
        return this.clients;
    }

    protected synchronized void removeClient(ClientInterface client) {
        for(CoupleClientNickname c: this.clients) {
            if(c.getVirtualClient().equals(client)) this.clients.remove(c);
        }
        if (this.clients.size() < 4) this.clientGatherer = new ClientGatherer(this, this.PORT);
        if (this.clients.size() == 1) {
            if (this.timer != null) this.timer.stopTimer();
            this.timer = new ServerTimer(this, this.time);
            this.timer.start();
        }
    }

    public ClientInterface searchVirtualClient(String player) {
        try {
            for (CoupleClientNickname c : this.clients) {
                if (c.getNickname().equals(player)) return c.getVirtualClient();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void broadcast(Message message) {
        for (CoupleClientNickname c : this.clients) {
            c.getVirtualClient().notify(message);
        }
    }

    protected void startGame() {
        if (this.clients.size() == 1) {
            this.timer = new ServerTimer(this, this.time);
            this.timer.start();
        } else {
            this.clientGatherer.closeClientGatherer();
            if (this.timer != null) this.timer.stopTimer();
            this.nicknames.setGameStarted();
            System.out.println("START GAME");
            this.controller.startGame(this.nicknames.getNicknames(), this.virtualView);
        }
    }

    public Nickname getNicknames() {
        return nicknames;
    }

    protected VirtualView getVirtualView(){return this.virtualView;}

    public static void main(String[] args){
        new SagradaServer();
    }
}