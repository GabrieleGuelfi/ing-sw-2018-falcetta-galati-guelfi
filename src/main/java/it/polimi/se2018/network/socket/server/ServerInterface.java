package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.MessageDie;

public interface ServerInterface {
    public void send(MessageDie message);
}
