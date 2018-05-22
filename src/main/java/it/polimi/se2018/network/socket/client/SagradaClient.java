package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.MessageNickname;
import it.polimi.se2018.network.socket.server.ServerInterface;
import it.polimi.se2018.view.ViewForClient;

public class SagradaClient {

    private static final int PORT = 1111;
    private static final String HOST = "localhost";

    public static void main(String[] args) {

        ViewForClient viewForClient = new ViewForClient();

        String nickname = viewForClient.getNickname();

        ServerInterface server = new NetworkHandler(HOST, PORT, new ClientImplementation());
        System.out.println("Connected! Waiting for the game to start...");
        server.send(new MessageNickname(nickname));

    }
}
