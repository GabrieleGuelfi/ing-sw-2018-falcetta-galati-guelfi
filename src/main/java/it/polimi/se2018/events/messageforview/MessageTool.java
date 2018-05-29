package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageTool extends Message {

    private String description;

    public MessageTool(String nickname, String description) {
        super(nickname);
        this.description = description;
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }

}
