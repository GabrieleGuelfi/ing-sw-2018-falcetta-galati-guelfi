package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.socket.server.ServerInterface;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkHandler extends Thread implements ServerInterface {

    private Socket socket;
    private ObjectInputStream inputStream;
    private ClientInterface client;
    private ObjectOutputStream outputStream;

    public NetworkHandler(String host, int port, ClientInterface client) {

        try {
            this.socket = new Socket(host, port);
            this.client = client;
            this.start();
        }
        catch(IOException e) {
            System.out.println("Connection error!");
        }
    }

    @Override
    public void run() {
        // This thread will wait for messages from the server, and send them to client interface.
        while(!this.socket.isClosed()) {
            try {
                this.inputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) inputStream.readObject();
                if(message == null) {
                    this.stopConnection();
                }
                else {
                    client.notify(message);
                }
            }
            catch(NullPointerException | EOFException e) {

            }
            catch(IOException | ClassNotFoundException e) {
                stopConnection();
            }
        }
    }

    public synchronized void send (Message message ) {

        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("\nYou are now disconnected from the server.");
        }
    }

    @Override
    public void addClient(ClientInterface clientInterface, String nickname) {

    }

    public synchronized void stopConnection() {
        if(!socket.isClosed()) {
            try {
                this.socket.close();
            }
            catch (IOException e) {
                final Logger logger = Logger.getLogger(this.getClass().getName());
                logger.log(Level.WARNING, e.getMessage());
            }
            System.out.println("Closed");
        }
    }
}
