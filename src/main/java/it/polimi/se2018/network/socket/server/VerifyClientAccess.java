package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageError;

public class VerifyClientAccess extends Thread {
    private SagradaServer sagradaServer;
    private Socket clientConnection;
    boolean loop = true;

    public VerifyClientAccess(SagradaServer server, Socket client){
        this.sagradaServer = server;
        this.clientConnection = client;
    }

    public void run(){

        try {
            ObjectInputStream in = new ObjectInputStream(this.clientConnection.getInputStream());

            ObjectOutputStream out = new ObjectOutputStream(this.clientConnection.getOutputStream());
            out.writeObject(new Message());
            while(loop){
                try {
                    Message message = (Message) in.readObject();
                    boolean access = this.sagradaServer.getNicknames().verifyNickname(message.getNickname());
                    if(access){
                        this.sagradaServer.addClient(this.clientConnection, message.getNickname());
                        this.loop = false;
                    }
                    else{
                        out = new ObjectOutputStream(this.clientConnection.getOutputStream());
                        out.writeObject(new MessageError());
                    }
                }
                catch(ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void closeThread(){
        try {
            ObjectOutputStream out = new ObjectOutputStream(this.clientConnection.getOutputStream());
            out.writeObject(new MessageError());
        }
        catch(IOException e){
            e.printStackTrace();
        }
        this.loop = false;
    }
}
