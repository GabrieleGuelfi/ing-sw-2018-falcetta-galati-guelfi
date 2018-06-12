package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.socket.server.ServerInterface;
import it.polimi.se2018.view.View;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static java.lang.System.out;

public class SagradaClient {

    private static final int PORT = 1111;
    private static final String HOST = "localhost";

    private static ClientImplementation client;
    private static ServerInterface server;
    private static View view;
    private static ClientInterface remoteRef=null;

    private static void connectThroughSocket(String nickname) {
        server = new NetworkHandler(HOST, PORT, client);
        client.addServer(server);
        view.notifyObservers(new Message(nickname));
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

        int choice = view.askConnection();
        if(choice==1) connectThroughSocket(nickname);
        else connectThroughRmi(nickname);

    }

    public static void closeClient() {

        System.exit(0);

    }

    public SagradaClient() {
        view = new View();
        client = new ClientImplementation();

        view.register(client);
        client.register(view);

        newConnection(view.askNickname());

    }


    public static void main(String[] args) {
        new SagradaClient();
    }



}
