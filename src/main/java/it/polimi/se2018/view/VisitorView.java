package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforview.*;

public interface VisitorView {

    void visit(Message message);
    void visit(MessageError message);
    void visit(MessageNickname message);
    void visit(MessagePrivObj message);
    void visit(MessagePublicObj message);
    void visit(MessageChooseWP message);
    void visit(MessageWPChanged message);
    void visit(MessageTurnChanged message);
    void visit(MessageDPChanged message);

}
