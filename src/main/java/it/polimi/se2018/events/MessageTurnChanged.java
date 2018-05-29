package it.polimi.se2018.events;

import it.polimi.se2018.utils.SagradaVisitor;
import it.polimi.se2018.view.VisitorView;

public class MessageTurnChanged extends Message {

    private String playerTurn;
    private boolean hasUsedTool;
    private boolean hasPlacedDie;

    public MessageTurnChanged(String playerTurn, boolean hasPlacedDie, boolean hasUsedTool) {
        super();
        this.playerTurn = playerTurn;
        this.hasPlacedDie = hasPlacedDie;
        this.hasUsedTool = hasUsedTool;
    }

    public boolean hasPlacedDie() {
        return hasPlacedDie;
    }

    public boolean hasUsedTool() {
        return hasUsedTool;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }
}
