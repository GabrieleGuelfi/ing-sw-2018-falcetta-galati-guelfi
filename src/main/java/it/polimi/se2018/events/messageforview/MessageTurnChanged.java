package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageTurnChanged extends Message {

    private String playerTurn;

    public MessageTurnChanged(String playerTurn) {
        super();
        this.playerTurn = playerTurn;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }
}
