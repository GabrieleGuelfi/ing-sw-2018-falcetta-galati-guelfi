package it.polimi.se2018.network;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.view.View;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

    private int port;
    private int ip;
    private boolean toSend;
    private boolean toClose;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private View view;
    private Player player;
    private Message message;
    private Object clientMessage;

    public ServerThread(int serverPort, int serverIp) {
        this.port = serverPort;
        this.ip = serverIp;
        this.view =View.getView();
        this.toSend = false;
        this.toClose = false;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer( Player p){this.player = p;}

    public void setToClose(Boolean value){this.toClose = value;}

    public void setMessage(Message sms){
        this.message = sms;
        toSend = true;
    }

    public Message getMessage(){return this.message;}

    @Override
    public void run(){
    try {
        try {
            this.serverSocket = new ServerSocket(this.port);
            System.out.println("Server socket ready on " + this.port);

            this.clientSocket = serverSocket.accept();
            System.out.println("Received client connection.");

            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());

            while(!toClose){
                if(!toSend) {
                    Thread.yield();
                }
                else{
                    this.toSend = false;
                    out.writeObject(this.message);

                    view.notifyObservers(this.clientMessage = in.readObject());
                }

            }
        }
        catch (IOException | ClassNotFoundException e) { }

        finally {
            this.serverSocket.close();
            this.clientSocket.close();
            this.in.close();
            this.out.close();
        }

    }
    catch(IOException e){

    }
    }


}
