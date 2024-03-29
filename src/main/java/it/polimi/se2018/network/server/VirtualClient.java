package it.polimi.se2018.network.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforserver.MessageErrorVirtualClientClosed;
import it.polimi.se2018.network.client.ClientInterface;
import it.polimi.se2018.utils.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.*;

/**
 * VirtualClient is used to mask the socket layer to the server; in fact it will send messages through
 * sockets and will stay running in order to listen for messages from client
 */
public class VirtualClient extends Observable implements ClientInterface, Runnable{

    private Socket clientConnection;
    private boolean loop;
    private ObjectInputStream inputStream;

    public VirtualClient(Socket socket){
        this.clientConnection = socket;
    }

    @Override
    public void run(){
        try
            {
            loop = true;
            while(loop && !this.clientConnection.isClosed()){
                //CREATE AN INPUT STREAM AND SET LOOP TRUE
                //RECEIVED A MESSAGE AND NOTIFY VIRTUAL VIEW
                this.inputStream = new ObjectInputStream(this.clientConnection.getInputStream());
                Message message = (Message) inputStream.readObject();
                out.println("New Message from " + message.getNickname());
                this.notifyObservers(message);
                }
        }
        catch(ClassNotFoundException e){
            final Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, e.getMessage());
        }
         catch(IOException e){

            if(loop) {
                this.notifyObservers(new MessageErrorVirtualClientClosed(this));
                out.println("MessageErrorVirtualClientClosed send.\n");
                try {
                    this.clientConnection.close();
                } catch (IOException ex) {
                    final Logger logger = Logger.getLogger(this.getClass().getName());
                    logger.log(Level.WARNING, ex.getMessage());
                } finally {
                    Thread.currentThread().interrupt();
                }
            }
         }

    }

    public void notify(Message message){
        try{

            //CREATE AN OUTPUT STREAM AND SEND A MESSAGE TO THE CLIENT
            ObjectOutputStream outputStream = new ObjectOutputStream(this.clientConnection.getOutputStream());
            outputStream.writeObject(message);
            outputStream.flush();
            out.println("Message send to " + message.getNickname());
        }
        catch(IOException e){

            //IF IO EXCEPTION WAS CAUGHT SEND A MESSAGE ERROR AND CLOSE THE CONNECTION
            this.notifyObservers(new MessageErrorVirtualClientClosed(this));
            out.println("MessageErrorVirtualClientClosed send. \n");
            this.loop = false;

        }
    }

    @Override
    public void closeConnection() {
        loop = false;
        try {
            this.clientConnection.close();
        }
        catch(IOException e){
            final Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, e.getMessage());
        }
        finally {
            Thread.currentThread().interrupt();
        }
    }
}
