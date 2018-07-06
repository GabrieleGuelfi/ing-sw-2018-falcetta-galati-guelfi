package it.polimi.se2018.network.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.server.ServerInterface;
import it.polimi.se2018.view.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.application.Application.launch;

public class SagradaClient {

    private static final int PORT = 1111;
    private static String HOST = "localhost";

    private static ClientImplementation client;
    private static ServerInterface server;
    private static ViewInterface view;
    private static ClientInterface remoteRef=null;
    private String nick;


    /**
     * Perform a connection through Socket and send the selected nickname to server
     * @param nickname nickname to send to server
     */
    private static void connectThroughSocket(String nickname) {
        server = new NetworkHandler(HOST, PORT, client);
        client.addServer(server);
        view.notifyObserver(new Message(nickname));
    }

    /**
     * Perform a connection through RMI and send the selected nickname to server
     * @param nickname nickname to send to server
     */
    private static void connectThroughRmi(String nickname) {
        try {
            server = (ServerInterface) Naming.lookup("//"+ HOST + "/RemoteServer");
            if(remoteRef==null) remoteRef =  (ClientInterface) UnicastRemoteObject.exportObject(client, 0);
            server.addClient(remoteRef, nickname);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            final Logger logger = Logger.getLogger(SagradaClient.class.getName());
            logger.log(Level.WARNING, e.getMessage());
        } finally {
            client.addServer(server); }

    }

    /**
     * Used by main or by ViewInterface to perform a new connection with the selected nickname
     * @param nickname nickname to send to server
     */
    public static void newConnection(String nickname) {

        int choice;
        do{
            choice = view.askConnection();
        }while (choice == 0);
        if(choice==1) connectThroughSocket(nickname);
        else connectThroughRmi(nickname);

    }

    /**
     * Exit from the game
     */
    public static void closeClient() {

        System.exit(0);

    }

    /**
     * Constructor of the class
     * @param viewInterface view which will be used during the game
     */
    public SagradaClient(ViewInterface viewInterface) {
        view = viewInterface;
        client = new ClientImplementation();

        view.addObserver(client);
        client.register(view);
        nick = view.askNickname();

        String userHost = view.getHost();
        if(!userHost.equals("")) HOST = userHost;

        newConnection(nick);

    }

    /**
     * Used by GUI to switch to CLI
     */
    public static void setCLI(String nickname) {

        client.deregister(view);
        view = new View(nickname);
        view.addObserver(client);
        client.register(view);


    }

    /**
     * Reachable method used by client to interrupt the timer
     */
    public static void stopTurn(){
        view.stopTimer();
    }

    public static void main(String[] args) {
       launch(ViewGame.class, args);
    }

}
