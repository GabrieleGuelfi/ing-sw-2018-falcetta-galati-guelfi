package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.socket.client.ClientInterface;

public interface ServerInterface {
    void send(Message message);
    void addClient(ClientInterface clientInterface, String nickname);

}
