package it.polimi.se2018.network.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforserver.MessageErrorClientGathererClosed;
import it.polimi.se2018.utils.Observable;

import static java.lang.System.*;


public class ClientGatherer extends Observable implements Runnable{

    private final SagradaServer sagradaServer;
    private ServerSocket serverSocket;
    private boolean loop = true;
    private ArrayList<VerifyClientAccess> verifyClientAccess = new ArrayList<>();


    public ClientGatherer( SagradaServer server, int port ) {
        this.sagradaServer = server;
        //CREATE THE SERVER SOCKET
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            out.println("IOException in Client Gatherer\n");
            this.notifyObservers(new MessageErrorClientGathererClosed());
        }

        out.println("ClientGatherer active\n");
    }

    @Override
    public void run(){

        out.println("Waiting for clients.\n");

        while((sagradaServer.getClients().size() <= 4) && loop)  {

            Socket newClientConnection;

            try {

                //CREATE A CONNECTION TO THE CLIENT
                newClientConnection = serverSocket.accept();
                out.println("A new client connected.\n");

                //VERIFY IF HE CAN ENJOY IN THIS BEAUTIFUL GAME
                VerifyClientAccess v = new VerifyClientAccess(this.sagradaServer, newClientConnection);
                v.start();

                //ADD THIS NEW THREAD TO THE ARRAY LIST
                this.verifyClientAccess.add(v);

            } catch (IOException e) {

                //NOTIFY THAT THERE WAS A PROBLEM AND CLIENT GATHERER CLOSED
                e.printStackTrace();
                out.println("IOException in Client Gatherer\n");
                this.notifyObservers(new MessageErrorClientGathererClosed());
            }
        }
    }

    public void stopClientGatherer(){
        //CLOSE ALL VERIFY CLIENT ACCESS
        for(VerifyClientAccess v: this.verifyClientAccess){
            v.closeThread();
        }
        this.loop = false;
        out.println("Client Gatherer is closing.\n");
    }

}
