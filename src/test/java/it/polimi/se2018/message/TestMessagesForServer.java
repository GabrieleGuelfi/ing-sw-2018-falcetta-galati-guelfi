package it.polimi.se2018.message;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforserver.*;
import it.polimi.se2018.network.client.ClientImplementation;
import it.polimi.se2018.network.client.ClientInterface;
import it.polimi.se2018.network.server.SagradaServer;
import it.polimi.se2018.network.server.VisitorServer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestMessagesForServer {

    SagradaServer sagradaServer;
    VisitorServer visitorServer;

    @Before
    public void setUp() {
        visitorServer = new VisitorServer() {
            @Override
            public void visit(Message message) {

            }

            @Override
            public void visit(MessageErrorVirtualClientClosed message) {

            }

            @Override
            public void visit(MessageError message) {

            }

            @Override
            public void visit(MessageAddClientInterface message) {

            }

            @Override
            public void visit(MessageErrorClientGathererClosed message) {

            }

            @Override
            public void visit(MessageRestartServer message) {

            }
        };
    }


    @Test
    public void testMessageAddClientInterface() {
        ClientInterface clientInterface = new ClientImplementation();
        String nick = "foo";
        MessageAddClientInterface messageAddClientInterface = new MessageAddClientInterface(clientInterface, nick);
        assertEquals("foo", messageAddClientInterface.getNickname());
        assertEquals(clientInterface, messageAddClientInterface.getClientInterface());

        messageAddClientInterface.accept(visitorServer);
    }

    @Test
    public void testMessageError() {
        MessageError messageError = new MessageError("reason");
        assertEquals("reason", messageError.getNickname());

        MessageError messageError1 = new MessageError();

        messageError.accept(visitorServer);
    }

    @Test
    public void testMessageErrorVirtualClientClosed() {
        ClientInterface clientInterface = new ClientImplementation();
        MessageErrorVirtualClientClosed messageErrorVirtualClientClosed = new MessageErrorVirtualClientClosed(clientInterface);
        assertEquals(clientInterface, messageErrorVirtualClientClosed.getClientInterface());

        messageErrorVirtualClientClosed.accept(visitorServer);
    }


    @Test
    public void testMessagePing() {
        MessagePing messagePing = new MessagePing();
        messagePing.accept(visitorServer);
    }

    @Test
    public void testMessageErrorClientGathererClosed() {
        MessageErrorClientGathererClosed messageErrorClientGathererClosed = new MessageErrorClientGathererClosed();

        messageErrorClientGathererClosed.accept(visitorServer);
    }

    @Test
    public void testMessageRestartServer() {
        MessageRestartServer messageRestartServer = new MessageRestartServer();
        messageRestartServer.accept(visitorServer);
    }
}
