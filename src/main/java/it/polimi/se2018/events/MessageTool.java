package it.polimi.se2018.events;

import it.polimi.se2018.utils.SagradaVisitor;

public class MessageTool extends Message {

    private String description;

    public MessageTool(String nickname, String description) {
        super(nickname);
        this.description = description;
    }

    @Override
    public void accept(SagradaVisitor v){
        v.visit(this);
    }

}
