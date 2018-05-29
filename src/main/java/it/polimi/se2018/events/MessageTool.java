package it.polimi.se2018.events;

import it.polimi.se2018.utils.SagradaVisitor;
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
