package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessagePrivObj extends Message {

    private String description;

    public MessagePrivObj(String nickname, String description) {
        super(nickname);
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }

}
