package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageConfirmMove extends Message {

    private boolean isThereAnotherMove;

    public MessageConfirmMove(String nickname, boolean isThereAnotherMove) {
        super(nickname);
        this.isThereAnotherMove = isThereAnotherMove;
    }

    public boolean isThereAnotherMove() {
        return isThereAnotherMove;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
