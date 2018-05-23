package it.polimi.se2018.events;

import it.polimi.se2018.utils.SagradaVisitor;

public class MessagePublicObj extends Message {

    String description;
    int points;

    public MessagePublicObj(String nickname, String description, int points) {
        super(nickname);
        this.description = description;
        this.points = points;
    }

    @Override
    public void accept(SagradaVisitor v){
        v.visit(this);
    }
}
