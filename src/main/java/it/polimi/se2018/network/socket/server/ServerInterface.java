package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageDie;

public interface ServerInterface {
    void send(Message message);

}
