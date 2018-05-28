package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageError;
import it.polimi.se2018.network.socket.server.ServerInterface;
import it.polimi.se2018.view.ViewForClient;

public class SagradaClient {

    private static final int PORT = 1111;
    private static final String HOST = "localhost";

    public static void main(String[] args) {

        ViewForClient viewForClient = ViewForClient.createViewForClient();
        ClientImplementation client = new ClientImplementation();

        viewForClient.register(client);
        client.register(viewForClient);

        ServerInterface server = new NetworkHandler(HOST, PORT, client);

        client.addServer(server);

        viewForClient.askNickname();


    }
}
