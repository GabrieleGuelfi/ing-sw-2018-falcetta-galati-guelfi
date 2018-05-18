package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageDie;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.network.socket.server.ServerInterface;

import java.io.*;
import java.net.Socket;

public class NetworkHandler extends Thread implements ServerInterface {

    private Socket socket;
    private ObjectInputStream inputStream;
    private ClientInterface client;
    private ObjectOutputStream outputStream;

    public NetworkHandler(String host, int port, ClientInterface client) {

        try {

            this.socket = new Socket(host, port);
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.client = client;
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());

            this.start();
        }
        catch(IOException e) {
            System.out.println("Connection error!");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // This thread will wait for messages from the server, and send them to client interface.
        while(!this.socket.isClosed()) {
            try {
                Message message = (Message) inputStream.readObject();
                if(message == null) {
                    this.stopConnection();
                }
                else {
                    System.out.println("Messaggio ricevuto!");
                    client.notify(message);
                }
            }
            catch(NullPointerException e) {

            }
            catch(IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void send (Message message ) {

        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopConnection() {
        if(!socket.isClosed()) {
            try {
                this.socket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Closed");
        }
    }
}
