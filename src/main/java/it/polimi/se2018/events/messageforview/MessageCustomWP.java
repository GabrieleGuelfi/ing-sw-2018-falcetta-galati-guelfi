package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageCustomWP extends Message{

    public MessageCustomWP (String nickname) {
        super(nickname);
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
