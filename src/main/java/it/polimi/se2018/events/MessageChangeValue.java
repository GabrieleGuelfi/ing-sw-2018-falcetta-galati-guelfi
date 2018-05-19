package it.polimi.se2018.events;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.SagradaVisitor;

public class MessageChangeValue extends Message {

    private boolean onlyIncrement;
    private int value;

    public MessageChangeValue(Player p){
        super("MessageChangeValue", p);
        this.onlyIncrement = false;
    }

    public MessageChangeValue(Player p, boolean increment){
        super("MessageChangeValueOnlyIncrement", p);
        this.onlyIncrement = true;
    }

    public void setValue(int i){
        this.value = i;
    }

    public int getValue(){
        return this.value;
    }

    public boolean getOnlyIncrement(){return this.onlyIncrement;}

    public void accept(SagradaVisitor visitor){
        visitor.visit(this);
    }
}
