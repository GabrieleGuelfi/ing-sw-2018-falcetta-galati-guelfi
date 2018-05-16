package it.polimi.se2018.model;

import it.polimi.se2018.model.dicecollection.DraftPool;

/**
 * @author Alessandro Falcetta
 */

public class Round {

    private DraftPool draftPool;
    private Player playerTurn;

    /**
     * Constructor
     * @param dp Draftpool assigned to the round, built from the caller
     * @param p The first player who will play
     */
    public Round(DraftPool dp, Player p) {

        if((dp==null) || (p==null)) throw new IllegalArgumentException("Arguments can't be null!");

        draftPool = dp;
        playerTurn = p;

    }

    /**
     * Setter for playing player
     * @param p Player who will become the current one playing
     */
    public void setPlayerTurn(Player p) { playerTurn = p;}

    /**
     * Getter for playing player
     * @return the player who is currently playing
     */
    public Player getPlayerTurn() { return playerTurn;}

    public DraftPool getDraftPool() {
        return draftPool;
    }
}
