package it.polimi.se2018.events;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.SagradaVisitor;

public class MessageChooseDie extends Message{

    private Die die;

    public MessageChooseDie(){
        super("MessageChooseDie");
            }
    public MessageChooseDie(Player p){
        super("MessageChooseDie", p);
    }
    public void setDie(Die d){this.die = d;}
    public Die getDie(){return this.die;}

    public void accept(SagradaVisitor visitor){
        visitor.visit(this);
    }
}
