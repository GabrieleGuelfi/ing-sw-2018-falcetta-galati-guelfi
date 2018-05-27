package it.polimi.se2018.view;

import it.polimi.se2018.events.*;
import it.polimi.se2018.utils.SagradaVisitor;

public class VisitorView implements SagradaVisitor {

    public void visit(Message message){
        System.out.println("This is a message!");
    }

    public void visit(MessageError message) {
        System.out.println("This is a message error!");
    }

    public void visit(MessageNickname message) {
        ViewForClient viewForClient = ViewForClient.createViewForClient();
        viewForClient.nicknameConfirmation(message);
    }

    public void visit(MessagePrivObj message) {
        ViewForClient.createViewForClient().showPrivateObjective(message.getColour());
    }

    public void visit(MessagePublicObj message) {
        ViewForClient.createViewForClient().showPublicObjective(message.getDescriptions(), message.getPoints());
    }

    public void visit(MessageChooseWP message) {
        ViewForClient.createViewForClient().askWindowPattern(message.getFirstIndex(), message.getSecondIndex());
    }

}
