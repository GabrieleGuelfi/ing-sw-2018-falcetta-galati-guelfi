package it.polimi.se2018;

import java.util.*;

public class Controller {

    Match match;

    public Controller() {
        this.match = null;
    }

    public void startGame(ArrayList<Player> players) {

        ArrayList<PublicObjective> objectives = new ArrayList<PublicObjective>(); // Here we should have the real Public Objectives...

        // Create the match...
        this.match = new Match(new Bag(), players, objectives);

        for(Player player: match.getActivePlayers()) {
            givePrivateObjective(player);
        }

        for(Player player: match.getActivePlayers()) {
            giveWindowPatterns(player);
            giveFavorTokens(player);
        }

        // TOOLS PART!

        for (int i = 0; i < 10; i++) {
            manageRound();
        }

        calcResults();
    }

    private void giveWindowPatterns(Player player){

    }

    private void givePrivateObjective(Player player) {

    }

    private void giveFavorTokens(Player player) {

    }

    private void manageRound() {
        int i;
        Round currentRound = new Round(new DraftPool(match.getBag(), match.getActivePlayers().size()), match.getActivePlayers().get(0));
        for(i=0; i<match.getActivePlayers().size(); i++) {
            currentRound.setPlayerTurn(match.getActivePlayers().get(i));
            manageMove(match.getActivePlayers().get(i));
        }

    }

    private void manageMove(Player player) {

    }

    private void calcResults() {
        
    }

}
