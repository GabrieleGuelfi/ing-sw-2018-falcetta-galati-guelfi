package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageTimeFinished extends Message {

    public MessageTimeFinished(String nickname) {
        super(nickname);
        this.timeFinished = true;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }

}
