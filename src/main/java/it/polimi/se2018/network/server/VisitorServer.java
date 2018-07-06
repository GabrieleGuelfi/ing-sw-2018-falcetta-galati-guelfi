package it.polimi.se2018.network.server;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforserver.*;
import it.polimi.se2018.events.messageforserver.MessageError;

public interface VisitorServer {

    void visit(Message message);

    /**
     * This message is received when a client disconnects using socket
     * Server will manage the event, removing user from game and putting his nickname in the list of the
     * ones who can connect again
     * @param message message to inspect
     */
    void visit(MessageErrorVirtualClientClosed message);

    /**
     * Generic message of error
     * @param message message to inspect
     */
    void visit(MessageError message);

    /**
     * Message used to add a socket connection
     * @param message message to inspect
     */
    void visit(MessageAddClientInterface message);

    /**
     * Error message for ClientGatherer's errors at runtime (due to thread exceptions)
     * @param message message to inspect
     */
    void visit(MessageErrorClientGathererClosed message);

    /**
     * Message used to tell the Server to prepare for a new match
     * @param message message to inspect
     */
    void visit(MessageRestartServer message);
}
