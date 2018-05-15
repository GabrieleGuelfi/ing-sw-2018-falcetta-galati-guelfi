package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageDie;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.view.View;

public class ClientImplementation implements ClientInterface {

    // Methods exposed to server.

    public void notify(Message message) {
       // Here, notify() should pass the message to the view, which should treat it the right way.
        // View.update(Message);
    }

    public void notify(MessageDie message) {
        System.out.println("message : " + message.getDie().getColour());
    }
}
