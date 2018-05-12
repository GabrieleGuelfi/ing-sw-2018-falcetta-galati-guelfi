package it.polimi.se2018.model;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.dicecollection.Bag;
import it.polimi.se2018.model.publicObjective.PublicObjective;

import java.util.ArrayList;

public class Match {

    private Bag bag;
    private ArrayList<Player> players;
    private ArrayList<Player> activePlayers;
    private ArrayList<PublicObjective> publicObjectives;
    private ArrayList<Tool> tools;
    private ArrayList<Die> roundTrack;
    private int round;

    public Match(Bag bag, ArrayList<Player> players, ArrayList<PublicObjective> objectives, ArrayList<Tool> tools) {
        if ((bag==null) || (players==null) || (objectives==null) || (tools==null)) {
            throw new IllegalArgumentException("Parameters can't be null!");
            // Is it necessary to check the size and content of all parameters?
        }
        this.bag = bag;
        bag.populateBag();
        this.activePlayers = players;
        this.publicObjectives = objectives;
        this.tools = tools;
        this.round = 1; // Human convention?
        this.roundTrack = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public void nextRound() {
        if (this.round==10) throw new IllegalStateException("Maximum number of turns reached!");
        this.round++;
    }

    public Bag getBag() {
        return bag;
    }

    public ArrayList<Player> getActivePlayers() {
        return this.activePlayers;
    }

    public ArrayList<Player> getPlayers(){ return this.players;}

    public ArrayList<Tool> getTools() { return this.tools;}

    public ArrayList<PublicObjective> getPublicObjectives() {
        return this.publicObjectives;
    }

    public ArrayList<Die> getRoundTrack() {
        return this.roundTrack;
    }

    public int getRound() { return this.round;}

    public void deactivatePlayer(Player p) {
        if (!activePlayers.contains(p)) throw new IllegalArgumentException("This player is not active or present!");
        int index;
        index = activePlayers.indexOf(p);
        players.add(activePlayers.remove(index));
    }

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
