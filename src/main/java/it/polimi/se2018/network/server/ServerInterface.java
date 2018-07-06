package it.polimi.se2018.network.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.client.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void send(Message message) throws RemoteException;
    void addClient(ClientInterface clientInterface, String nickname) throws RemoteException;

}
