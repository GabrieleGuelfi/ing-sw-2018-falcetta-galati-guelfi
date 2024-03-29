package it.polimi.se2018.model;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.events.messageforview.MessagePublicObj;
import it.polimi.se2018.events.messageforview.MessageTool;
import it.polimi.se2018.model.dicecollection.Bag;
import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.model.publicobjective.PublicObjective;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.view.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Match extends Observable {

    private Bag bag;
    private List<Player> players;
    private List<Player> activePlayers;
    private List<PublicObjective> publicObjectives;
    private List<Tool> tools;
    private Map<Integer, List<Die>> roundTrack;
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
    public Match(Bag bag, List<Player> players, List<PublicObjective> objectives, List<Tool> tools, VirtualView view) {
        if ((bag==null) || (players==null) || (objectives==null) || (tools==null)) {
            throw new IllegalArgumentException("Parameters can't be null!");
            // Is it necessary to check the size and content of all parameters?
        }
        this.bag = bag;
        this.bag.populateBag();
        this.players = players;
        this.activePlayers = players;
        this.publicObjectives = objectives;
        this.tools = tools;
        this.roundTrack = new HashMap<>();
        this.numRound = 1; // Human convention?
        firstPlayerRound = players.get(0);
        this.round = new Round(new DraftPool(this.bag, this.players.size()), firstPlayerRound);

        register(view);

        List<String> publicObjDescriptions = new ArrayList<>();
        List<Integer> publicObjPoints = new ArrayList<>();
        List<Integer> id = new ArrayList<>();
        for(PublicObjective p: objectives) {
            publicObjDescriptions.add(p.getDescription());
            publicObjPoints.add(p.getVp());
            id.add(p.getId());
        }
        if(!publicObjDescriptions.isEmpty()) notifyObservers(new MessagePublicObj(publicObjDescriptions, publicObjPoints, id));

        List<String> toolsNames = new ArrayList<>();
        List<String> toolDescriptions = new ArrayList<>();
        List<Boolean> used = new ArrayList<>();
        for(Tool t: tools) {
            toolsNames.add(t.getName());
            toolDescriptions.add(t.getDescription());
            used.add(t.isUsed());
        }
        if(!toolsNames.isEmpty()) notifyObservers(new MessageTool(toolsNames, toolDescriptions, used));

    }

    public Player getFirstPlayerRound() {
        return firstPlayerRound;
    }

    public Round getRound() {
        return round;
    }

    private void nextNumRound() {
        if (this.numRound == 10) throw new IllegalStateException("Maximum number of turns reached!");
        this.numRound++;
    }

    /**
     * Updates the round number, to a maximum of 10
     * @throws IllegalStateException if 10 rounds are already reached
     */
    public void setRound() {
        nextNumRound();
        if (players.indexOf(firstPlayerRound) == players.size()-1)
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
    public Map<Integer, List<Die>> getRoundTrack() {
        return roundTrack;
    }

    public void setRoundTrack() {
        this.roundTrack.put(numRound, round.getDraftPool().getBag());
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

}
