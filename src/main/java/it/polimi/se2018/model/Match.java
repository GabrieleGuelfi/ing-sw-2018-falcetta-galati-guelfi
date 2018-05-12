package it.polimi.se2018.model;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.publicObjective.PublicObjective;

import java.util.*;

public class Match {

    private Bag bag;
    private ArrayList<Player> players;
    private ArrayList<PublicObjective> publicObjectives;
    private ArrayList<Tool> tools;
    private ArrayList<Die> roundTrack;
    private int round;

    public Match(Bag bag, ArrayList<Player> players, ArrayList<PublicObjective> objectives) {
        this.bag = bag;
        this.players = players;
        this.publicObjectives = objectives;
        this.round = 0;
        this.roundTrack = new ArrayList<Die>();
    }

    public ArrayList<Player> getActivePlayers() {
        return this.players; // Change with active ones!
    }

    public void nextRound() {
        this.round++;
    }

    public Bag getBag() {
        return bag;
    }
}
