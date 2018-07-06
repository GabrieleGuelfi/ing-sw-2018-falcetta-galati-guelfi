package it.polimi.se2018.network.server;

import it.polimi.se2018.events.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.se2018.events.messageforview.MessageNickname;
import static java.lang.System.*;

/**
 * Check if a user can really access the game with the choosen nickname
 */
public class VerifyClientAccess extends Thread{
    private SagradaServer sagradaServer;
    private Socket clientConnection;
    private boolean loop = true;

    VerifyClientAccess(SagradaServer server, Socket client){
        this.sagradaServer = server;
        this.clientConnection = client;
    }

    @Override
    public void run(){

            while(loop){
                try {
                    //CREATE AN INPUT STREAM AND WAIT THAT SOME MESSAGE ARRIVED
                    ObjectInputStream in = new ObjectInputStream(this.clientConnection.getInputStream());
                    Message message = (Message) in.readObject();

                    //CONTROL OF NICKNAME
                    boolean access = this.sagradaServer.verifyNickname(message.getNickname());

                    //IF CONTROL RETURN TRUE, SEND TO CLIENT THE CONFIRMATION
                    //SETTING TRUE THE PARAMETER ON MESSAGE NICKNAME
                    //AND ADD THIS NEW CLIENT TO THE LIST OF PLAYERS
                    //THAN SET LOOP TO FALSE TO EXIT FROM THE LOOP
                    if(access){
                        out.println("NewClient Verified:" + message.getNickname());
                        ObjectOutputStream out = new ObjectOutputStream(this.clientConnection.getOutputStream());
                        out.writeObject(new MessageNickname(true));
                        this.sagradaServer.addClient(this.clientConnection, message.getNickname());
                        this.loop = false;
                    }
                    //IF CONTROL RETURN FALSE, SE TO CLIENT THE COMMUNICATION TO CHANGE NICKNAME
                    //SETTING FALSE THE PARAMETER ON MESSAGE NICKNAME
                    else{
                        ObjectOutputStream outputStream = new ObjectOutputStream(this.clientConnection.getOutputStream());
                        out.println("Request reject.");
                        outputStream.writeObject(new MessageNickname(false));
                    }
                }
                catch(ClassNotFoundException e){
                    final Logger logger = Logger.getLogger(this.getClass().getName());
                    logger.log(Level.WARNING, e.getMessage());
                }
                catch(IOException e){
                    this.loop = false;
                }
            }
            Thread.currentThread().interrupt();

    }

    public void closeThread(){
            this.loop = false;
    }
}
