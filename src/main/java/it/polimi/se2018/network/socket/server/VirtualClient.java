package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageDie;
import it.polimi.se2018.events.MoveDie;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.socket.client.ClientInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import it.polimi.se2018.utils.Observable;

import static it.polimi.se2018.view.View.getView;

public class VirtualClient extends Thread implements ClientInterface{
    private Socket clientConnection;
    private final SagradaServer sagradaServer;
    private Player player;

    private boolean loop;

    public VirtualClient(SagradaServer server, Socket socket){
        this.sagradaServer = server;
        this.clientConnection = socket;
    }

    @Override
    public void run(){
        try {
            ObjectInputStream in = new ObjectInputStream(this.clientConnection.getInputStream());

            loop = true;

            while(loop && !this.clientConnection.isClosed()){

                System.out.println("waiting...");
                MoveDie message = this.readMessage(in);
                getView().notifyController(message);

            }

            in.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                clientConnection.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    public void notify(Message message){
        ObjectOutputStream out;

        try{
            out = new ObjectOutputStream(this.clientConnection.getOutputStream());
            out.writeObject(message);
            out.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    private MoveDie readMessage(ObjectInputStream i){
        Object sms = new Object();
        try {
            sms = i.readObject();
        }
        catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }

        return (MoveDie) sms;
    }

    public Player getPlayer() {
        return player;
    }





}
