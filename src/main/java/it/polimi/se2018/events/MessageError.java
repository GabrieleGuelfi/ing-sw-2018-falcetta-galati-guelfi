package it.polimi.se2018.events;


import it.polimi.se2018.utils.SagradaVisitor;
import it.polimi.se2018.view.VisitorView;

public class MessageError extends Message {

    public MessageError(){
        super("MessageError");
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }
}
