package it.polimi.se2018.model;


public class Round {

    private DraftPool draftPool;
    private Player playerTurn;

    public Round(DraftPool dp, Player p) {

        draftPool = dp;
        playerTurn = p;

    }

    public void setPlayerTurn(Player p) { playerTurn = p;}
    public Player getPlayerTurn() { return playerTurn;}

}
