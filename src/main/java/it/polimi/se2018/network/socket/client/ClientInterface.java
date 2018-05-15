package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageDie;

public interface ClientInterface {

    public void notify(MessageDie message);
}
