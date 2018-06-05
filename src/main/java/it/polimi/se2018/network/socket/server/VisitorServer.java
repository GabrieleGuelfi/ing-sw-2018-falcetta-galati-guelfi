package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforserver.*;
import it.polimi.se2018.events.messageforserver.MessageError;

public interface VisitorServer {

    void visit(Message message);
    void visit(MessageErrorVirtualClientClosed message);
    void visit(MessageError message);
    void visit(MessageAddClientInterface message);
    void visit(MessageErrorClientGathererClosed message);
    void visit(MessageRestartServer message);
}
