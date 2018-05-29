package it.polimi.se2018.network.rmi.server;

import it.polimi.se2018.events.*;
import it.polimi.se2018.network.socket.server.SagradaServer;
import it.polimi.se2018.network.socket.server.VisitorServer;
import it.polimi.se2018.utils.Observer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class HandleRemoteServer implements Observer {

    private SagradaServer sagradaServer;

    public HandleRemoteServer(SagradaServer server){
        this.sagradaServer = server;
        try{
            LocateRegistry.createRegistry(0);
            RemoteServer remoteServer = new RemoteServer();
            remoteServer.register(this);
            UnicastRemoteObject.exportObject(remoteServer, 0);
            Naming.rebind("//localhost/RemoteServer", remoteServer);
        }
        catch(RemoteException | MalformedURLException e){
            e.printStackTrace();
        }

    }

    public synchronized void update(Message message){
        message.accept(new VisitorServer(this.sagradaServer));
    }


}
