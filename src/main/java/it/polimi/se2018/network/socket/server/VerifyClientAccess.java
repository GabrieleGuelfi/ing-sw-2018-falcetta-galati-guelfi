package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import it.polimi.se2018.events.MessageNickname;
import static java.lang.System.*;

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

        try {
            while(loop){
                try {
                    ObjectInputStream in = new ObjectInputStream(this.clientConnection.getInputStream());
                    Message message = (Message) in.readObject();
                    boolean access = this.sagradaServer.getNicknames().verifyNickname(message.getNickname());
                    if(access){
                        out.println("NewClient Verified:" + message.getNickname());
                        ObjectOutputStream out = new ObjectOutputStream(this.clientConnection.getOutputStream());
                        out.writeObject(new MessageNickname(true));
                        this.sagradaServer.addClient(this.clientConnection, message.getNickname());
                        this.loop = false;
                    }
                    else{
                        ObjectOutputStream outputStream = new ObjectOutputStream(this.clientConnection.getOutputStream());
                        out.println("Request reject.");
                        outputStream.writeObject(new MessageNickname(false));
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
            this.loop = false;
    }
}
