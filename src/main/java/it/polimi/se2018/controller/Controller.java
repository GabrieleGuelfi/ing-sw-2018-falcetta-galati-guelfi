package it.polimi.se2018.controller;


import it.polimi.se2018.events.MoveDie;
import it.polimi.se2018.exceptions.OutOfWindowPattern;
import it.polimi.se2018.model.*;
import it.polimi.se2018.model.publicObjective.PublicObjective;
import it.polimi.se2018.view.View;

import java.util.*;

public class Controller implements Observer {

    Match match;

    public Controller() {
        this.match = null;
    }

    public void startGame(ArrayList<Player> players, View view) {

        ArrayList<PublicObjective> objectives = new ArrayList<>(); // Here we should have the real Public Objectives...

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
        for(Player player: match.getActivePlayers()) {
            calcResults(player, objectives);
        }
    }

    private void giveWindowPatterns(Player player){

    }

    private void givePrivateObjective(Player player) {

    }

    private void giveFavorTokens(Player player) { //probably useless, we can make this in giveWindowPattern

    }

    private void manageRound() {
        int i;
        Round currentRound = new Round(new DraftPool(match.getBag(), match.getActivePlayers().size()), match.getActivePlayers().get(0));
        for(i=0; i<match.getActivePlayers().size(); i++) { //andata
            currentRound.setPlayerTurn(match.getActivePlayers().get(i));
            match.getActivePlayers().get(i).setPlacedDie(false);
            match.getActivePlayers().get(i).setUsedTool(false);
            //update(view, new MoveDie(player, row, column)); //verify correct use

            //manageMoveDie(match.getActivePlayers().get(i)); //two moves per turn, but only one per type, how we can manage this?
            //manageMoveDie(match.getActivePlayers().get(i));
        }
        for(i=match.getActivePlayers().size(); i>=0 ; i--) { //ritorno
            currentRound.setPlayerTurn(match.getActivePlayers().get(i));
            //manageMoveDie(match.getActivePlayers().get(i)); //two moves per turn
            //manageMoveDie(match.getActivePlayers().get(i));
        }

    }

    private void manageMoveDie(MoveDie m) { //better manageMovedie(MoveDie m)
        if (m.getPlayer().isPlacedDie()) { //maybe we can place this in manageRound
            //notify(view, "die already placed")
            return;
        }
        if (m.getRow() < 1 || m.getRow() > 4 || m.getColumn() < 1 || m.getColumn() > 5) {
            //error input not valid
            return;
        }
        if (verifyNear(m) && verifyColor(m) && verifyNumber(m)){
            m.getPlayer().getWindowPattern().putDice(m.getDie(), m.getRow(), m.getColumn());
            return;
        }
        //3 if for search the restrictions violated and notify all to users
        if (!verifyNear(m)) {
            //error
        }
        if (!verifyColor(m)) {
            //error
        }
        if (!verifyNumber(m)) {
            //error
        }
        return;
    }

    /**
     * Verify if there is another Die near the position chosen by user
     * @param m move performed by user
     * @return false if there is another die near
     */
    private boolean verifyNear (MoveDie m) {

        WindowPattern windowPattern = m.getPlayer().getWindowPattern();
        int row = m.getRow();
        int column = m.getColumn();

        if (windowPattern.getEmptyBox() == 20) {
            return (row == 1 || row == windowPattern.Max_ROW || column == 1 || column == windowPattern.MAX_COL);
        }
        for(int i = row-2; i < row+1; i++ ) {
            for(int j = column-2; j < column+1; j++) {
                if (i != row-1 || j != column-1) {
                    try {
                        if (windowPattern.getBox(i, j) != null)
                            return true;
                    } catch (OutOfWindowPattern e) {
                    }
                }
            }
        }
        return false;
    }

    /**
     * verify if there is another Die with the same colour near, or if there is colourRestriction in the Box
     * @param m move performed by user
     * @return false if Die break colour restriction
     */
    private boolean verifyColor(MoveDie m) {

        WindowPattern windowPattern = m.getPlayer().getWindowPattern();
        try {
            if (windowPattern.getBox(m.getRow(), m.getColumn()).getColourRestriction() != m.getDie().getColour() && windowPattern.getBox(m.getRow(), m.getColumn()).getColourRestriction() != Colour.WHITE)
                return false;
        } catch (OutOfWindowPattern | NullPointerException e){}
        try {
            if (windowPattern.getBox(m.getRow() - 2, m.getColumn() - 1).getDie().getColour() == m.getDie().getColour())
                return false;
        } catch (OutOfWindowPattern | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 1, m.getColumn() - 2).getDie().getColour() == m.getDie().getColour())
                return false;
        } catch (OutOfWindowPattern | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 1, m.getColumn()).getDie().getColour() == m.getDie().getColour())
                return false;
        } catch (OutOfWindowPattern | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow(), m.getColumn() - 1).getDie().getColour() == m.getDie().getColour())
                return false;
        } catch (OutOfWindowPattern | NullPointerException e) {}
        return true;
    }

    /**
     * verify if there is another Die with the same number near, or if there is numberRestriction in the Box
     * @param m move performed by user
     * @return false if Die break number restriction
     */
    private boolean verifyNumber( MoveDie m) {

        WindowPattern windowPattern = m.getPlayer().getWindowPattern();
        try {
            if (windowPattern.getBox(m.getRow(), m.getColumn()).getValueRestriction() != m.getDie().getValue() && windowPattern.getBox(m.getRow(), m.getColumn()).getValueRestriction() != -1) //-1 equals to no restriction
                return false;
        } catch (OutOfWindowPattern | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 2, m.getColumn() - 1).getDie().getValue() == m.getDie().getValue()) //if null?
                return false;
        } catch (OutOfWindowPattern | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 1, m.getColumn() - 2).getDie().getValue() == m.getDie().getValue())
                return false;
        } catch (OutOfWindowPattern | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 1, m.getColumn()).getDie().getValue() == m.getDie().getValue())
                return false;
        } catch (OutOfWindowPattern | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow(), m.getColumn() - 1).getDie().getValue() == m.getDie().getValue())
                return false;
        } catch (OutOfWindowPattern | NullPointerException e) {}
        return true;
    }

    /**
     * calculate the score of a player, based on his private objective and the three public ones
     * @param player player whose score is calculated
     * @param p public objective of the match
     */
    private void calcResults(Player player, ArrayList<PublicObjective> p) {

        WindowPattern windowPattern = player.getWindowPattern();

        //privateObjective
        for(int i=0; i<windowPattern.Max_ROW; i++) {
            for (int j=0; j<windowPattern.MAX_COL; j++) {
                try {
                    if (windowPattern.getBox(i, j).getDie().getColour() == player.getPrivateObjective().getShade())
                        player.addPoints(windowPattern.getBox(i, j).getDie().getValue());
                } catch (OutOfWindowPattern | NullPointerException e) {}
            }
        }

        //publicOjectives
        for (PublicObjective publicObjective : p) {
            player.addPoints(publicObjective.calcScore(player.getWindowPattern()));
        }

    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
