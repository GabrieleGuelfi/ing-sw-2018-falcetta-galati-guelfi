package it.polimi.se2018.events.messageforserver;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessagePing extends Message{

    public MessagePing(){
        super();
    }

    @Override
    public void accept(VisitorView visitorView){
        visitorView.visit(this);
    }
}
