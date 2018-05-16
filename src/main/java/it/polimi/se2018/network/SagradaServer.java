package it.polimi.se2018.network;

import it.polimi.se2018.view.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SagradaServer {

    private int port;
    private int ip;
    private ArrayList<ServerThread> serverThreads;
    private int numberOfClient;

    public SagradaServer(int number){

        //HAVE TO CHANGE THIS VALUES
        this.port = 3333;
        this.ip = 3333;
        this.numberOfClient = number;
    }

    public void startSagradaServer() throws IOException{
        for(int i = 0; i < this.numberOfClient; i++)  { this.serverThreads.add(new ServerThread(this.port, this.ip)); }
        for(int i = 0; i < this.numberOfClient; i++)  {
            serverThreads.get(i).setPlayer(View.getView().getViewModel().getPlayer().get(i));
            serverThreads.get(i).start();
        }
    }


    public ArrayList<ServerThread> getServerThread(){
        return this.serverThreads;
    }

}