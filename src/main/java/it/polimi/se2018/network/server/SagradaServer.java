package it.polimi.se2018.network.server;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.MessageClientDisconnected;
import it.polimi.se2018.events.messageforserver.*;
import it.polimi.se2018.events.messageforview.MessageNickname;
import it.polimi.se2018.network.client.ClientInterface;
import it.polimi.se2018.network.client.ConnectionHandlerThread;
import it.polimi.se2018.view.VirtualView;
import static java.lang.System.*;

import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import it.polimi.se2018.utils.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SagradaServer implements VisitorServer, Observer{
    private int port = 1111;
    private int maxClients = 4;
    private VirtualView virtualView;
    private Controller controller;
    private ClientGatherer clientGatherer;
    private Registry rmiRegistry;
    private ServerTimer timer;
    private boolean gameIsStarted;
    private final int time;
    private ArrayList<String> nicknameDisconnected;
    private HandleClientGatherer handleClientGatherer;
    private ArrayList<VerifyConnectionRmi> verifyConnectionRmi = new ArrayList<>();

    private ArrayList<CoupleClientNickname> clients = new ArrayList<>();

    public SagradaServer() {

        //WHEN SERVER START, IT ASKS HOW LONG IT HAVE TO WAIT ON LOBBY BEFORE START THE GAME
        Scanner stdin = new Scanner(System.in);
        out.println("Set time for lobby timer.");
        this.time = stdin.nextInt();
        stdin.close();

        //INITIALIZE VIRTUAL VIEW
        this.virtualView = new VirtualView(this);
        this.nicknameDisconnected = new ArrayList<>();
        this.gameIsStarted = false;

        //INITIALIZE REMOTE SERVER
        try{
            rmiRegistry = LocateRegistry.createRegistry(1099);
            RemoteServer remoteServer = new RemoteServer();
            remoteServer.register(this);
            ServerInterface serverInterface =  (ServerInterface) UnicastRemoteObject.exportObject(remoteServer, 0);
            Naming.rebind("//localhost/RemoteServer", serverInterface);
            out.println("Remote Server enabled.");
        }
        catch(RemoteException | MalformedURLException e){
            out.println("Remote Server has a problem.");
        }


        //RUN CLIENT GATHERER FOR SOCKET CONNECTION
        this.clientGatherer = new ClientGatherer(this, port);
        (new Thread(this.clientGatherer)).start();
    }

    public static void main(String[] args){
        new SagradaServer();
    }

    /**
     * It will save the user and his nickname / socketConnection to the current players
     * @param clientConnection socket of the client
     * @param nick nick to save
     */
    protected void addClient(Socket clientConnection, String nick) {

        //CREATE A VIRTUAL CLIENT AND SET NICKNAME
        VirtualClient cm = new VirtualClient(clientConnection);
        clients.add(new CoupleClientNickname(cm, nick));

        //ADD SAGRADA SERVER AS OBSERVER OF THIS NEW VIRTUAL CLIENT
        cm.register(this);

        //START VIRTUAL CLIENT
        new Thread(cm).start();

        out.println(nick + " enjoyed");

        if(gameIsStarted && this.nicknameDisconnected.contains(nick)) this.nicknameDisconnected.remove(nick);

        //CONTROL TIMER AND CLIENT GATHERER
        if (this.clients.size() == 2 && !this.gameIsStarted) {
            this.timer = new ServerTimer(this, this.time);
            this.timer.start();
        }
        else if (this.clients.size() == this.maxClients && !this.gameIsStarted) {
            this.timer.interrupt();
            out.println("All player enjoyed the party.");
            this.startGame();
        }
        else if(this.clients.size() == this.maxClients){
            this.handleClientGatherer = new HandleClientGatherer(this.clientGatherer);
            this.handleClientGatherer.start();
            out.println(nick + " was reconnected");
        }
        else if(this.gameIsStarted) out.println(nick + " was reconnected");

    }

    /**
     * It will add the user and his nickname / RMI client to the current players
     * @param clientInterface RMI client
     * @param nick nickname to save
     */
    private void addClient(ClientInterface clientInterface, String nick){

        //CREATE NEW COUPLE-CLIENT-NICKNAME AND ADD IT TO THIS.CLIENTS
        this.clients.add(new CoupleClientNickname(clientInterface, nick));

        out.println(nick + " enjoyed");

        if(gameIsStarted && this.nicknameDisconnected.contains(nick)) this.nicknameDisconnected.remove(nick);

        //CONTROL TIMER AND CLIENT GATHERER
        if (this.clients.size() == 2 && !this.gameIsStarted) {
            this.timer = new ServerTimer(this, this.time);
            this.timer.start();
        }
        else if (this.clients.size() == this.maxClients && !this.gameIsStarted) {
            this.timer.interrupt();
            out.println("All player enjoyed the party.");
            this.startGame();
        }
        else if(this.clients.size() == this.maxClients){
            this.handleClientGatherer = new HandleClientGatherer(this.clientGatherer);
            this.handleClientGatherer.start();

            out.println(nick + " was reconnected");
        }
        else if(this.gameIsStarted) out.println(nick + " was reconnected");
    }

    /**
     * Getter for current clients
     * @return current clients
     */
    protected ArrayList<CoupleClientNickname> getClients() {
        return this.clients;
    }

    private void removeClient(ClientInterface client) {
        //REMOVE THIS CLIENT INTERFACE
        String string = null;
        CoupleClientNickname couple = null;

        for(CoupleClientNickname c: this.clients) {
            if(c.getVirtualClient().equals(client)){
                string = c.getNickname();
                couple = c;
            }
        }
        if(gameIsStarted) this.nicknameDisconnected.add(string);
        this.clients.remove(couple);
        out.println(string  + " is removed.");

        if(this.gameIsStarted && this.clients.size()==1) this.virtualView.notifyObservers(new MessageClientDisconnected(this.clients.get(0).getNickname(), true));
        else {
            this.virtualView.notifyObservers(new MessageClientDisconnected(null, false));

            //CONTROL TIMER AND CLIENT GATHERER
            if (this.clients.size() < this.maxClients && gameIsStarted) {
                this.handleClientGatherer.setClientGathererActive();
                this.handleClientGatherer.interrupt();
            }
            if (this.clients.size() == 1 && this.timer != null && !this.gameIsStarted) {
                this.timer.stopTimer();
                out.println("not enough players");
            }
        }
    }

    /**
     * Used to look for the virtualClient / RMI client, from a nickname
     * @param player nickname to look for
     * @return virtualClient / RMI of the user
     */
    public ClientInterface searchVirtualClient(String player) {
        //SEARCH WHICH VIRTUAL CLIENT IS ASSOCIATED TO NICKNAME 'PLAYER'
        try {
            for (CoupleClientNickname c : this.clients) {
                if (c.getNickname().equals(player)) return c.getVirtualClient();
            }
        } catch (NullPointerException e) {
            out.println("Client" + player + "does not exists.");
        }
        return null;
    }

    /**
     * Send a message to all users
     * @param message to send
     */
    public void broadcast(Message message) {
        //SEND A MESSAGE TO ALL CLIENT
        for (CoupleClientNickname c : this.clients) {
            try {
                c.getVirtualClient().notify(message);
            }
            catch (RemoteException e){
                final Logger logger = Logger.getLogger(this.getClass().getName());
                logger.log(Level.WARNING, e.getMessage());
            }
        }
    }

    /**
     * Check if a nickname is in the ones that were previously connected but then crashed
     * Or, in a match which is not started, checks if it is already choosen
     * @param string player's nickname to verify
     * @return true if the nickname can be used
     */
    protected boolean verifyNickname(String string){
        if(gameIsStarted && this.nicknameDisconnected.contains(string)) return true;
        else if(gameIsStarted) return false;
        else{
            for(CoupleClientNickname c : this.clients) if(c.getNickname().equals(string)) return false;
        }
        return true;
    }

    /**
     * Starts the controller for the incoming match
     * Called if lobby time is finished or if 4 players were reached
     */
    protected void startGame() {

        if (this.clients.size() == 1) {
            out.println("not enough player.");
        } else {
            //CONTROL TIMER AND CLOSE CLIENT GATHERER
            //SET MAX CLIENTS
            //NOTIFY CONTROLLER THE BEGGING OF THE GAME
            this.handleClientGatherer = new HandleClientGatherer(this.clientGatherer);
            this.handleClientGatherer.start();
            this.maxClients = this.clients.size();
            this.gameIsStarted = true;
            out.println("START GAME");
            ArrayList<String> allClients = new ArrayList<>();
            for(CoupleClientNickname c :  this.clients){
                allClients.add(c.getNickname());
                out.println(c.getNickname());
            }

            this.controller = new Controller(allClients, this.virtualView);
        }
    }

    /**
     * Restart the game if players want to play again
     */
    private void restartGame(){

        //BE READY FOR A NEW MATCH
        this.gameIsStarted = false;
        this.nicknameDisconnected = new ArrayList<>();

        //CLOSE ALL CONNECTION
        for(CoupleClientNickname c : this.clients){
            try {
                c.getVirtualClient().closeConnection();
            }
            catch(RemoteException e){
                final Logger logger = Logger.getLogger(this.getClass().getName());
                logger.log(Level.WARNING, e.getMessage());
            }
        }


        //REMOVE ALL CLIENTS
        /*for(int i = 0; i < this.clients.size(); i++){
            this.clients.remove(this.clients.get(i));
        } */
        this.clients = new ArrayList<>();

        this.maxClients = 4;
        this.controller = null;

        this.virtualView = new VirtualView(this); // Errors if this isn't done.

        this.handleClientGatherer.setClientGathererActive();
        this.handleClientGatherer.interrupt();

    }

    @Override
    public void update(Message message){
        message.accept(this);
    }

    @Override
    public void visit(Message message){
        //NOTIFY CONTROLLER
        (new ConnectionHandlerThread(this.virtualView, message)).start();

    }

    @Override
    public void visit(MessageErrorVirtualClientClosed message){
        //REMOVE A CLIENT
        out.println("VirtualClient was disconnected");
        this.removeClient(message.getClientInterface());
    }

    @Override
    public void visit(MessageError message){
        //PRINT THE ERROR
       out.println(message.getNickname());
    }

    @Override
    public void visit(MessageAddClientInterface message){
        //VERIFY IF THE NICKNAME IS AVAILABLE
        //USED ONLY BY RMI
        try {
            boolean value = this.verifyNickname(message.getNickname());
            if (value) {
                message.getClientInterface().notify(new MessageNickname(true));
                this.addClient(message.getClientInterface(), message.getNickname());
                VerifyConnectionRmi v = new VerifyConnectionRmi(message.getClientInterface());
                v.register(this);
                (new Thread(v)).start();
            } else message.getClientInterface().notify(new MessageNickname(false));
        }
        catch (RemoteException e){
            final Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    public void visit(MessageErrorClientGathererClosed message){
        //CREATE A NEW CLIENT GATHERER
        out.println(message.getNickname());
        if(this.clientGatherer != null)this.clientGatherer.stopClientGatherer();
        this.clientGatherer = new ClientGatherer(this,this.port);
    }

    @Override
    public void visit(MessageRestartServer message){
        this.restartGame();
    }


}