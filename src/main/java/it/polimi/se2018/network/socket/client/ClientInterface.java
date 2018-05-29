package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;

import java.rmi.Remote;

public interface ClientInterface extends Remote {

    void notify(Message message);

}
