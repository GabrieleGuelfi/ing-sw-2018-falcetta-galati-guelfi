package it.polimi.se2018.network.socket.client;

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

    public static void main(String[] args) {

        View view = View.createView();
        ClientImplementation client = new ClientImplementation();
        ServerInterface server = null;

        view.register(client);
        client.register(view);


        String choice = view.askRmiOrSocket(); //insert a while to ask Rmi or Socket, if user write wrong use socket for default
        if(choice.equals("Rmi")) {
            String nicknameForRmi = view.getNicknameForRmi();
            try {
                server = (ServerInterface) Naming.lookup("//localhost/RemoteServer");
                ClientInterface remoteRef =  (ClientInterface) UnicastRemoteObject.exportObject(client, 0);
                server.addClient(remoteRef, nicknameForRmi);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            finally { client.addServer(server); }
        }
        else {
            server = new NetworkHandler(HOST, PORT, client);
            client.addServer(server);
            view.askNickname();
        }

    }
}
