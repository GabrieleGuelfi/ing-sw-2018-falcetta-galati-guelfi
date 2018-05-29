package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageConfirmMove extends Message {

    private boolean hasPlacedDie;
    private boolean hasUsedTool;

    public MessageConfirmMove(String nickname, boolean hasPlacedDie, boolean hasUsedTool) {
        super(nickname);
        this.hasPlacedDie = hasPlacedDie;
        this.hasUsedTool = hasUsedTool;
    }

    public boolean hasPlacedDie() {
        return hasPlacedDie;
    }

    public boolean hasUsedTool() {
        return hasUsedTool;
    }


    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
