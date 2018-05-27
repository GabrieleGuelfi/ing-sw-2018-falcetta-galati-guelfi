package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import it.polimi.se2018.events.MessageError;
import it.polimi.se2018.events.MessageNickname;

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
            ObjectInputStream in = new ObjectInputStream(this.clientConnection.getInputStream());

            while(loop){
                try {
                    Message message = (Message) in.readObject();
                    boolean access = this.sagradaServer.getNicknames().verifyNickname(message.getNickname());
                    if(access){
                        System.out.println("NewClient Verified:" + message.getNickname());
                        ObjectOutputStream out = new ObjectOutputStream(this.clientConnection.getOutputStream());
                        out.writeObject(new MessageNickname(true));
                        out.close();
                        this.sagradaServer.addClient(this.clientConnection, message.getNickname());
                        this.loop = false;
                    }
                    else{
                        ObjectOutputStream out = new ObjectOutputStream(this.clientConnection.getOutputStream());
                        System.out.println("Request reject.");
                        out.writeObject(new MessageNickname(false));
                        out.close();
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
        try
            (ObjectOutputStream out = new ObjectOutputStream(this.clientConnection.getOutputStream()))
        {
            out.writeObject(new MessageError());
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally {
            this.loop = false;
        }
    }
}
