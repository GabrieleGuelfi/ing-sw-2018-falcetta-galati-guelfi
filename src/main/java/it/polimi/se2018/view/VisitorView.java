package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforserver.MessagePing;
import it.polimi.se2018.events.messageforview.*;

public interface VisitorView {

    void visit(Message message);
    void visit(MessageError message);
    void visit(MessageNickname message);
    void visit(MessagePrivObj message);
    void visit(MessagePublicObj message);
    void visit(MessageTool message);
    void visit(MessageChooseWP message);
    void visit(MessageWPChanged message);
    void visit(MessageTurnChanged message);
    void visit(MessageDPChanged message);
    void visit(MessageConfirmMove message);
    void visit(MessageErrorMove message);
    void visit(MessagePing message);
    void visit(MessageRoundChanged message);
    void visit(MessageRoundTrack message);
    void visit(MessageEndMatch message);
    void visit(MessageToolOrder message);
    void visit(MessageAskMove message);
    void visit(MessageForceMove message);

    void visit(MessageTimeFinished message);

}
