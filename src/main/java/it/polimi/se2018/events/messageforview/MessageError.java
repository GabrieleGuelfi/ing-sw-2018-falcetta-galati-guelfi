package it.polimi.se2018.events.messageforview;


import it.polimi.se2018.events.Message;
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
