package it.polimi.se2018.events;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.SagradaVisitor;

public class MessageUpdatePlayer extends MessageUpdate {

    private Player player;

    public MessageUpdatePlayer(Player p){
        super();
        this.player = p;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public void accept(SagradaVisitor visitor){
        visitor.visit(this);
    }
}
