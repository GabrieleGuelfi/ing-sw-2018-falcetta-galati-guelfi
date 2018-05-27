package it.polimi.se2018.network.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import static java.lang.System.*;


public class ClientGatherer extends Thread{
    private final SagradaServer sagradaServer;
    private final int port;
    private ServerSocket serverSocket;
    private boolean loop = true;
    private ArrayList<VerifyClientAccess> verifyClientAccess = new ArrayList<>();


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

        out.println("Waiting for clients.\n");

        while((sagradaServer.getClients().size() <= 4) && loop)  {

            Socket newClientConnection;

            try {

                newClientConnection = serverSocket.accept();
                out.println("A new client connected.");
                VerifyClientAccess v = new VerifyClientAccess(this.sagradaServer, newClientConnection);
                v.start();
                this.verifyClientAccess.add(v);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void closeClientGatherer(){
        for(VerifyClientAccess v: this.verifyClientAccess){
            v.closeThread();
        }
        this.loop = false;
    }

}
