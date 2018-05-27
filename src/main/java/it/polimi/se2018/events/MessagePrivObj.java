package it.polimi.se2018.events;

import it.polimi.se2018.utils.SagradaVisitor;

public class MessagePrivObj extends Message {

    String colour;

    public MessagePrivObj(String nickname, String colour) {
        super(nickname);
        this.colour = colour;
    }

    public String getColour() {
        return this.colour;
    }

    @Override
    public void accept(SagradaVisitor v){
        v.visit(this);
    }

}
