package it.polimi.se2018.network.rmi.server;

import it.polimi.se2018.events.messageforview.Message;
import it.polimi.se2018.network.socket.server.SagradaServer;
import it.polimi.se2018.network.socket.server.ServerInterface;
import it.polimi.se2018.network.socket.server.VisitorServer;
import it.polimi.se2018.utils.Observer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class HandleRemoteServer implements Observer {

    private SagradaServer sagradaServer;

    public HandleRemoteServer(SagradaServer server){
        this.sagradaServer = server;
        try{
            LocateRegistry.createRegistry(1099);
            RemoteServer remoteServer = new RemoteServer();
            remoteServer.register(this);
            ServerInterface serverInterface =  (ServerInterface) UnicastRemoteObject.exportObject(remoteServer, 0);
            //Registry registry = LocateRegistry.getRegistry();
            Naming.rebind("//localhost/RemoteServer", serverInterface);
            System.out.println("REGISTRY REBIND BLABLABLA...");
        }
        catch(RemoteException | MalformedURLException e){
            e.printStackTrace();
            System.out.println("HANDLESERVER");
        }

    }

    public synchronized void update(Message message){
        message.accept(new VisitorServer(this.sagradaServer));
    }


}
