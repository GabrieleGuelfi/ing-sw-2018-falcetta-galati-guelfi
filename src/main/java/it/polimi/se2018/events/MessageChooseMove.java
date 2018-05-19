package it.polimi.se2018.events;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.SagradaVisitor;

public class MessageChooseMove extends Message {

    private boolean tool;
    private boolean moveDie;
    private boolean nothing;

    public MessageChooseMove(Player p){
        super("MessageChooseMove", p);
        this.tool = false;
        this.moveDie = false;
        this.nothing = false;
    }

    public void setTool(){
        this.tool = true;
    }

    public void setMoveDie(){
        this.moveDie = true;
    }

    public void setNothing(){
        this.nothing = true;
    }

    public boolean getTool(){return this.tool;}
    public boolean getMoveDie(){return this.moveDie; }
    public boolean getNothing(){return this.nothing;}

    public void accept(SagradaVisitor visitor){
        visitor.visit(this);
    }
}
