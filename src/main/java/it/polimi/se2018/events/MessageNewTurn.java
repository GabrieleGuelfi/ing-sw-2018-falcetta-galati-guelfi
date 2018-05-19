package it.polimi.se2018.events;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.SagradaVisitor;

public class MessageNewTurn extends Message {

    private Player player;

    public MessageNewTurn(Player p){
        super("MessageNewTurn", p);
    }

    public void accept(SagradaVisitor visitor){
        visitor.visit(this);
    }
}
