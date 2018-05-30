package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    void notify(Message message) throws RemoteException;

}
