package it.polimi.se2018;

import java.util.*;

public class Match {
    private Bag bag;
    private ArrayList<Player> players;
    private ArrayList<PublicObjective> publicObjectives;
    private ArrayList<Tool> tools;
    private ArrayList<Die> roundTrack;
    private int round;

    public Match() { // Others starting objects? Maybe "Bag"?
        this.round = 0;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        int position = this.players.indexOf(player);
        this.players.remove(position);
    }

    public void addTool(Tool tool) {
        this.tools.add(tool);
    }

    public void addPublicObjective(PublicObjective objective) {
        this.publicObjectives.add(objective);
    }

    // Maybe these methods go in Controller?

    public void startMatch() {

    }

    public void endMatch() {

    }

    public void prepareMatch() {

    }

    public void giveWindowsPattern() {

    }
}
