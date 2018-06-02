package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

public class MessageRequest extends Message {

    private RequestType type;

    public MessageRequest(String nickname, RequestType type) {
        super(nickname);
        this.type = type;
    }

    @Override
    public void accept(VisitorController v) {
        v.visit(this);
    }

    public RequestType getType() {
        return type;
    }
}
