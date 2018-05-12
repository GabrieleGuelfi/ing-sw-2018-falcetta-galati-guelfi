package it.polimi.se2018.model;


public class Round {

    private DraftPool draftPool;
    private Player playerTurn;

    public Round(DraftPool dp, Player p) {

        if((dp==null) || (p==null)) throw new IllegalArgumentException("Arguments can't be null!");

        draftPool = dp;
        playerTurn = p;

    }

    public void setPlayerTurn(Player p) { playerTurn = p;}
    public Player getPlayerTurn() { return playerTurn;}

}
