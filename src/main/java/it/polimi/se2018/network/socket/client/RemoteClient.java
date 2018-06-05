package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;
import static java.lang.System.*;
import it.polimi.se2018.utils.Observable;

public class RemoteClient extends Observable implements ClientInterface {


    @Override
    public void notify(Message message) {

        this.notifyObservers(message);
    }

    public void closeConnection(){}
}
