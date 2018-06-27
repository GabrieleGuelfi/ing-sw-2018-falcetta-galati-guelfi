package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.socket.server.ServerInterface;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.view.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static java.lang.System.out;
import static javafx.application.Application.launch;

public class SagradaClient {

    private static final int PORT = 1111;
    private static final String HOST = "localhost";

    private static ClientImplementation client;
    private static ServerInterface server;
    private static ViewInterface view;
    private static ClientInterface remoteRef=null;
    private String nick;

    private static void connectThroughSocket(String nickname) {
        server = new NetworkHandler(HOST, PORT, client);
        client.addServer(server);
        view.notifyObserver(new Message(nickname));
    }

    private static void connectThroughRmi(String nickname) {
        try {
            server = (ServerInterface) Naming.lookup("//localhost/RemoteServer");
            if(remoteRef==null) remoteRef =  (ClientInterface) UnicastRemoteObject.exportObject(client, 0);
            server.addClient(remoteRef, nickname);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        } finally {
            client.addServer(server); }

    }

    public static void newConnection(String nickname) {

        int choice;
        do{
            choice = view.askConnection();
        }while (choice == 0);
        out.println("Connessione presa");
        if(choice==1) connectThroughSocket(nickname);
        else connectThroughRmi(nickname);

    }

    public static void closeClient() {

        System.exit(0);

    }

    public SagradaClient(ViewInterface viewInterface) {
        view = viewInterface;
        client = new ClientImplementation();

        view.addObserver(client);
        client.register((Observer) view);
        nick = view.askNickname();

        newConnection(nick);

    }

    public static void setView(ViewInterface v){
        client.deregister((Observer) view);
        view = v;
        view.addObserver(client);
        client.register((Observer) view);
    }


    public static void main(String[] args) {
       launch(ViewGame.class, args);
    }

}
