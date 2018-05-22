package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.view.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VirtualClient extends Thread implements ClientInterface{
    private Socket clientConnection;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final VirtualView virtualView;
    private String player;

    private boolean loop;

    public VirtualClient(VirtualView v, Socket socket){
        this.virtualView = v;
        this.clientConnection = socket;
    }

    @Override
    public void run(){
        try {
            inputStream = new ObjectInputStream(this.clientConnection.getInputStream());

            loop = true;

            while(loop && !this.clientConnection.isClosed()){
                Message message = (Message) inputStream.readObject();
                this.virtualView.notifyObservers(message);
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

    public String getPlayer() {
        return player;
    }
    public void setPlayer(String p) {
        this.player = p;
    }

}
