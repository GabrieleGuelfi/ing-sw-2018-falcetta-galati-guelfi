package it.polimi.se2018.events;

import it.polimi.se2018.utils.SagradaVisitor;

public class MessagePrivObj extends Message {

    String description;

    public MessagePrivObj(String nickname, String description) {
        super(nickname);
        this.description = description;
    }

    @Override
    public void accept(SagradaVisitor v){
        v.visit(this);
    }

}
