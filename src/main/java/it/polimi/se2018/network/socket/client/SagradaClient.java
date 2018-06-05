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

    private static final int PORT = 12345;
    private static final String HOST = "localhost";

    private ClientImplementation client;
    private ServerInterface server;
    private View view;

    private void connectThroughSocket() {
        String nickname = view.askNickname();
        server = new NetworkHandler(HOST, PORT, client);
        client.addServer(server);
        view.notifyObservers(new Message(nickname));
    }

    private void connectThroughRmi() {
        String nickname = view.askNickname();
        try {
            server = (ServerInterface) Naming.lookup("//localhost/RemoteServer");
            ClientInterface remoteRef =  (ClientInterface) UnicastRemoteObject.exportObject(client, 0);
            server.addClient(remoteRef, nickname);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        finally {
            client.addServer(server); }

    }

    public SagradaClient() {
        view = View.createView();
        client = new ClientImplementation();

        view.register(client);
        client.register(view);

        int choice = view.askConnection();

        if(choice==1) connectThroughSocket();
        else connectThroughRmi();
    }


    public static void main(String[] args) {
        new SagradaClient();
    }



}
