package it.polimi.se2018.network;

import it.polimi.se2018.events.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SagradaClient {

    private Socket socket;
    private int port;
    private InetAddress ipServer;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Scanner stdin;

    private void startClient()throws IOException {
        this.socket = new Socket(this.ipServer, this.port);
        System.out.println("Connection.");
        this.in = new ObjectInputStream(this.socket.getInputStream());
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.stdin = new Scanner (System.in);

        try{
            Object messageServer = in.readObject();
            if(messageServer instanceof Message){
               // switch ((Message) messageServer){

                //}

            }
        }
        catch(NoSuchElementException e){

        }
        catch (ClassNotFoundException e){}
        finally {
            in.close();
            out.close();
            stdin.close();
            socket.close();
        }

    }
}
