package it.polimi.se2018.model;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.dicecollection.Bag;
import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.model.publicobjective.PublicObjective;
import it.polimi.se2018.utils.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alessandro Falcetta
 */

// This means that, when Match ( = Model ) is modified
// notify(ModelUpdate) is called. This message contains a copy of the new
// Match, with all the new objects. This can be changed, if we decide to
// update only the objects modified.

public class Match extends Observable {

    private Bag bag;
    private List<Player> players;
    private List<Player> activePlayers;
    private List<PublicObjective> publicObjectives;
    private List<Tool> tools;
    private List<Die> roundTrack;
    private int numRound;
    private Round round;
    private Player firstPlayerRound;

    /**
     * Constructor of the class
     * @throws IllegalArgumentException if any of the argument is null
     * @param bag Bag of 90 dice correctly initialized
     * @param players Set of players
     * @param objectives Set of public objectives
     * @param tools Set of tools
     */
    public Match(Bag bag, List<Player> players, List<PublicObjective> objectives, List<Tool> tools) {
        if ((bag==null) || (players==null) || (objectives==null) || (tools==null)) {
            throw new IllegalArgumentException("Parameters can't be null!");
            // Is it necessary to check the size and content of all parameters?
        }
        this.bag = bag;
        bag.populateBag();
        this.activePlayers = players;
        this.publicObjectives = objectives;
        this.tools = tools;
        this.numRound = 1; // Human convention?
        this.roundTrack = new ArrayList<>();
        this.players = new ArrayList<>();

        //notify(new ModelUpdate(this)); // EXAMPLE: here Model throws a ModelUpdate.
        // All the Observers will be notified with this message (ie: View will be notified!)
        // We should use this.copy(), however.
    }

    public Player getFirstPlayerRound() {
        return firstPlayerRound;
    }

    public void setRound(Round r) {
        if(r==null) throw new IllegalArgumentException("Invalid round!");
        this.round = r;
    }

    public Round getRound() {
        return round;
    }

    public void nextNumRound() {
        if (this.numRound == 10) throw new IllegalStateException("Maximum number of turns reached!");
        this.numRound++;
    }

    /**
     * Updates the round number, to a maximum of 10
     * @throws IllegalStateException if 10 rounds are already reached
     */
    public void setRound() {
        nextNumRound();
        if (players.indexOf(firstPlayerRound) == players.size())
            firstPlayerRound = players.get(0);
        else
            firstPlayerRound = players.get(players.indexOf(firstPlayerRound)+1);
        this.round = new Round(new DraftPool(bag, getPlayers().size()), getFirstPlayerRound());
    }

    /**
     * Getter for bag
     * @return bag of this match
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * Method which returns the active players, aka the ones still online and playing
     * @return active players of the match
     */
    public List<Player> getActivePlayers() {
        return this.activePlayers;
    }

    /**
     * Method which returns the players, aka the ones who were playing, but then disconnected
     * @return players of the match
     */
    public List<Player> getPlayers(){ return this.players;}

    /**
     * Getter for tools
     * @return tools of the match
     */
    public List<Tool> getTools() { return this.tools;}

    /**
     * Getter for public objectives
     * @return public objectives of the match
     */
    public List<PublicObjective> getPublicObjectives() {
        return this.publicObjectives;
    }

    /**
     * Getter for roundtrack
     * @return roundtrack of the match
     */
    public List<Die> getRoundTrack() {
        return this.roundTrack;
    }

    /**
     * Getter for round number
     * @return round number of the match
     */
    public int getNumRound() { return this.numRound;}

    /**
     * Method which deactivates a player; this means that the player goes from "active players" to "players"
     * Useful when a players disconnect from the game
     * @param p player to be deactivated
     * @throws IllegalArgumentException if p is not active or present in the match
     */
    public void deactivatePlayer(Player p) {
        if (!activePlayers.contains(p)) throw new IllegalArgumentException("This player is not active or present!");
        int index;
        index = activePlayers.indexOf(p);
        players.add(activePlayers.remove(index));
    }

    /**
     * Method which activates a player; this means the player goes from "players" to "active players"
     * Useful when a players reconnects to the game
     * @param p player to be activated
     * @throws IllegalArgumentException if the player is already activated or not present in the match
     */
    public void activatePlayer(Player p) {
        if (!players.contains(p)) throw new IllegalArgumentException("This player is already active or not present!");
        int index;
        index = players.indexOf(p);
        activePlayers.add(players.remove(index));
    }

    // Is this needed? If yes, we have to implement copy() for PublicObjectives and Tools, but it can be tough.
    /* public Match copy() {
        ArrayList<Player> activePlayersCopy = new ArrayList<>();
        for(Player p: this.activePlayers) {
            activePlayersCopy.add(p.copy());
        }

        ArrayList<Player> playersCopy = new ArrayList<>();
        for(Player p: this.players) {
            playersCopy.add(p.copy());
        }

        ArrayList<PublicObjective> publicObjectivesCopy= new ArrayList<>();
        for(PublicObjective p: this.publicObjectives) {
            publicObjectivesCopy.add(p.copy());
        }

        ArrayList<PublicObjective> toolsCopy= new ArrayList<>();
        for(Tool t: this.tools) {
            toolsCopy.add(t.copy());
        }

        ArrayList<Die> roundTrackCopy = new ArrayList<>();
        for(Die d: this.roundTrack) {
            roundTrackCopy.add(d.copy());
        }

        Match matchCopy = new Match(this.bag.copy(), activePlayersCopy, publicObjectivesCopy, toolsCopy);

        matchCopy.players = playersCopy;
        matchCopy.round = this.round;
        matchCopy.roundTrack = roundTrackCopy;

        return matchCopy;
    }*/
}
