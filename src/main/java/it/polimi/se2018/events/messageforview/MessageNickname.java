package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageNickname extends Message{

    private boolean nicknameUsed;

    public MessageNickname(boolean value){
        this.nicknameUsed = value;
    }

    public boolean getBoolean(){
        return this.nicknameUsed;
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }
}
