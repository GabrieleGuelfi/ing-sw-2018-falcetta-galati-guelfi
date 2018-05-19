package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;

public class ClientImplementation implements ClientInterface {

    // Methods exposed to server.

    public void notify(Message message) {
       // Here, notify() should pass the message to the view, which should treat it the right way.
        System.out.println(message.getString());
    }

}
