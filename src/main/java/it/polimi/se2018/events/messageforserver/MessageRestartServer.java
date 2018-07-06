package it.polimi.se2018.events.messageforserver;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.network.server.VisitorServer;

public class MessageRestartServer extends Message {
    @Override
    public void accept(VisitorServer visitor){
        visitor.visit(this);
    }
}
