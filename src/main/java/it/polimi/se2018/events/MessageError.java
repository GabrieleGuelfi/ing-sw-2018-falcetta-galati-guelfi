package it.polimi.se2018.events;


import it.polimi.se2018.utils.SagradaVisitor;

public class MessageError extends Message {

    public MessageError(){
        super("MessageError");
    }

    @Override
    public void accept(SagradaVisitor v){
        v.visit(this);
    }
}
