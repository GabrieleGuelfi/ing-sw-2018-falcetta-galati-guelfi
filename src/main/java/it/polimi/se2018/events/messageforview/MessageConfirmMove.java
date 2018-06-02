package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageConfirmMove extends Message {

    private boolean hasPlacedDie;
    private boolean hasUsedTool;
    private boolean movePerformed;

    public MessageConfirmMove(String nickname, boolean hasPlacedDie, boolean hasUsedTool, boolean movePerformed) {
        super(nickname);
        this.hasPlacedDie = hasPlacedDie;
        this.hasUsedTool = hasUsedTool;
        this.movePerformed = movePerformed;
    }

    public boolean hasPlacedDie() {
        return hasPlacedDie;
    }

    public boolean hasUsedTool() {
        return hasUsedTool;
    }

    public boolean isMovePerformed() {
        return movePerformed;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
