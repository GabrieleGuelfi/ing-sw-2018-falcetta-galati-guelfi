package it.polimi.se2018.events.messageforserver;


import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageError extends Message {

    public MessageError(){
        super("MessageError");
    }

    public MessageError(String s){
        super(s);
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }
}
