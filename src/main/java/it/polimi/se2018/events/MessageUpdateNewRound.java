package it.polimi.se2018.events;

import it.polimi.se2018.utils.SagradaVisitor;

public class MessageUpdateNewRound extends MessageUpdate {
    private int roundNumber;

    public MessageUpdateNewRound(){
        super();
    }
    public void accept(SagradaVisitor visitor){
        visitor.visit(this);
    }
}
