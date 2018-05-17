package it.polimi.se2018.model;

import it.polimi.se2018.model.dicecollection.DraftPool;

import java.util.List;

/**
 * @author Alessandro Falcetta
 */
public class Round {

    private DraftPool draftPool;
    private Player playerTurn;
    private int numTurn;

    /**
     * Constructor
     * @param dp Draftpool assigned to the round, built from the caller
     * @param p The first player who will play
     */
    public Round(DraftPool dp, Player p) {

        if((dp==null) || (p==null)) throw new IllegalArgumentException("Arguments can't be null!");

        draftPool = dp;
        playerTurn = p;
        numTurn = 1;

    }

    public int getNumTurn() {
        return numTurn;
    }

    /**
     * Setter for playing player
     * @param p Player who will become the current one playing
     */
    public void setPlayerTurn(Player p) { playerTurn = p;} //useless?

    /**
     * Getter for playing player
     * @return the player who is currently playing
     */
    public Player getPlayerTurn() { return playerTurn;}


    public void nextTurn(List<Player> players) {
        if (numTurn < players.size())
            if (players.indexOf(playerTurn) < players.size()-1) {
                playerTurn = players.get(players.indexOf(playerTurn)+1);
            }
            else {
                playerTurn = players.get(0);
            }
        else if (numTurn > players.size()) {
            if (players.indexOf(playerTurn) == 0) {
                playerTurn = players.get(players.size()-1);
            }
            else {
                playerTurn = players.get(players.indexOf(playerTurn)-1);
            }
        }
        if (numTurn < 2*players.size())
            numTurn++;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }
}
