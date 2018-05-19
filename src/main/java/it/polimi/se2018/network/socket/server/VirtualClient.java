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

public class VirtualClient extends Thread implements ClientInterface{
    private Socket clientConnection;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final SagradaServer sagradaServer;
    private Player player;

    private boolean loop;

    public VirtualClient(SagradaServer server, Socket socket){
        this.sagradaServer = server;
        this.clientConnection = socket;
    }


    public void setPlayer(Player p) {
        this.player = p;
    }

    @Override
    public void run(){
        try {
            inputStream = new ObjectInputStream(this.clientConnection.getInputStream());

            loop = true;

            while(loop && !this.clientConnection.isClosed()){
                Message message = (Message) inputStream.readObject();
                sagradaServer.getImplementation().send(message);
            }

            inputStream.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e) {

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

        try{
            outputStream = new ObjectOutputStream(this.clientConnection.getOutputStream());
            outputStream.writeObject(message);
            outputStream.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public Player getPlayer() {
        return player;
    }

}
