package it.polimi.se2018.network.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientGatherer extends Thread{
    private final SagradaServer sagradaServer;
    private final int port;
    private ServerSocket serverSocket;


    public ClientGatherer( SagradaServer server, int port ) {
        this.sagradaServer = server;
        this.port = port;

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){

        System.out.println("Waiting for clients.\n");

        while(true) {

            Socket newClientConnection;

            try {

                newClientConnection = serverSocket.accept();
                System.out.println("A new client connected.");

                // Aggiungo il client
                sagradaServer.addClient(newClientConnection);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
