package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageErrorMove extends Message {

    private  String reason;

    public MessageErrorMove(String nickname, String reason) {
        super(nickname);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
