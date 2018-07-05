package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessagePrivObj extends Message {

    private String description;
    private String shade;

    public MessagePrivObj(String nickname, String description, String shade) {
        super(nickname);
        this.description = description;
        this.shade = shade;
    }

    public String getDescription() {
        return this.description;
    }

    public String getShade() {
        return shade;
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }

}
