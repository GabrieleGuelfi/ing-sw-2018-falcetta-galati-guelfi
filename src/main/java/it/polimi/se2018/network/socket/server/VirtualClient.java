package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforview.MessageErrorVirtualClientClosed;
import it.polimi.se2018.network.socket.client.ClientInterface;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.view.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import static java.lang.System.*;

public class VirtualClient extends Observable implements ClientInterface, Runnable{
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
                out.println("New Message from " + message.getNickname());
                this.virtualView.notifyObservers(message);

                }
             inputStream.close();
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            this.notifyObservers(new MessageErrorVirtualClientClosed(this));
            out.println("MessageErrorVirtualClientClosed send");
        }

    }

    public void notify(Message message){
        try{

            ObjectOutputStream outputStream = new ObjectOutputStream(this.clientConnection.getOutputStream());
            outputStream.writeObject(message);
            outputStream.flush();
            out.println("Message send to" + message.getNickname());
        }
        catch(IOException e){
            e.printStackTrace();
            this.virtualView.notifyObservers(new Message());
            this.loop = false;
            Thread.currentThread().interrupt();
        }
    }

    protected void closeVirtualClient(){
        this.loop = false;
    }

}
