package it.polimi.se2018.events;


public class MessageError extends Message {

    public MessageError(){
        super("MessageError");
    }

    @Override
    public void accept(SagradaVisitor v){
        v.visit(this);
    }
}
