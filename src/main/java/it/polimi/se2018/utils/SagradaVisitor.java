package it.polimi.se2018.utils;

import it.polimi.se2018.events.*;

public interface SagradaVisitor {

    void visit(Message message);
    void visit(MessageError message);
    void visit(MessageNickname message);
    void visit(MessagePrivObj message);
    void visit(MessagePublicObj message);
    void visit(MessageChooseWP message);
}
