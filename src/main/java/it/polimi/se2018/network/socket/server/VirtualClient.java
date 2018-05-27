package it.polimi.se2018.network.socket.server;

import com.sun.corba.se.pept.encoding.InputObject;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageError;
import it.polimi.se2018.events.MessageErrorCloseVirtualClient;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.view.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class VirtualClient extends Thread implements ClientInterface{
    private Socket clientConnection;
    private final VirtualView virtualView;
    private boolean loop;

    public VirtualClient(VirtualView v, Socket socket){
        this.virtualView = v;
        this.clientConnection = socket;
    }

    @Override
    public void run(){
        try

        {
            ObjectInputStream inputStream = new ObjectInputStream(this.clientConnection.getInputStream());
            loop = true;

            while(loop && !this.clientConnection.isClosed()){
                Message message = (Message) inputStream.readObject();
                System.out.println("New Message from " + message.getNickname());
                this.virtualView.notifyObservers(message);

                }
             inputStream.close();
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            this.virtualView.notifyObservers(new MessageError());
        }

    }

    public void notify(Message message){
        try{

            ObjectOutputStream outputStream = new ObjectOutputStream(this.clientConnection.getOutputStream());
            outputStream.writeObject(message);
            outputStream.flush();
            System.out.println("Message send to" + message.getNickname());
        }
        catch(IOException e){
            e.printStackTrace();
            this.virtualView.notifyObservers(new MessageErrorCloseVirtualClient(this));
            this.loop = false;
            Thread.currentThread().interrupt();
        }
    }

    protected void closeVirtualClient(){
        this.loop = false;
    }

}
