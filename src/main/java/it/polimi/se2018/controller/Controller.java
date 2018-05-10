package it.polimi.se2018.controller;

import com.sun.istack.internal.NotNull;
import it.polimi.se2018.events.MoveDie;
import it.polimi.se2018.exceptions.OutOfWindowPattern;
import it.polimi.se2018.model.*;
import it.polimi.se2018.view.View;

import java.util.*;

public class Controller implements Observer {

    Match match;

    public Controller() {
        this.match = null;
    }

    public void startGame(ArrayList<Player> players, View view) {

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
        for(i=0; i<match.getActivePlayers().size(); i++) { //andata
            currentRound.setPlayerTurn(match.getActivePlayers().get(i));
            match.getActivePlayers().get(i).setPlacedDie(false);
            match.getActivePlayers().get(i).setUsedTool(false);
            //update(view, new MoveDie(player, row, column)); //verify correct use

            manageMove(match.getActivePlayers().get(i)); //two moves per turn, but only one per type, how we can manage this?
            manageMove(match.getActivePlayers().get(i));
        }
        for(i=match.getActivePlayers().size(); i>=0 ; i--) { //ritorno
            currentRound.setPlayerTurn(match.getActivePlayers().get(i));
            manageMove(match.getActivePlayers().get(i)); //two moves per turn
            manageMove(match.getActivePlayers().get(i));
        }

    }

    private void manageMove(MoveDie m) { //better manageMovedie(MoveDie m)
        if (m.getPlayer().isPlacedDie()) { //maybe we can place this in manageRound
            //notify(view, "die already placed")
            return;
        }
        if (m.getRow() < 1 || m.getRow() > 4 || m.getColumn() < 1 || m.getColumn() > 5) {
            //error input not valid
            return;
        }
        if (m.getPlayer().getWindowPattern().getEmptyBox() == 20) {
            if ( m.getColumn() == 1 || m.getColumn() == 5 || m.getRow() == 1 || m.getRow() == 4 )
            if (!verifyColor(m))
                //error
                return;
            if (!verifyNumber(m))
                //error
                return;
            m.getPlayer().getWindowPattern().putDice(m.getDie(), m.getRow(), m.getColumn());
            return;
        }

    }

    private boolean verifyNear (WindowPattern windowPattern, int row, int column) {

        if (windowPattern.getEmptyBox() == 20) {
            return (row == 1 || row == 4 || column == 1 || column == 5);
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

    private boolean verifyColor(MoveDie m) {

        WindowPattern windowPattern = m.getPlayer().getWindowPattern();
        try {
            if (windowPattern.getBox(m.getRow(), m.getColumn()).getColourRestriction() != m.getDie().getColour() && windowPattern.getBox(m.getRow(), m.getColumn()).getColourRestriction() != Colour.WHITE)
                return false;
        } catch (OutOfWindowPattern e){}
        try {
            if (windowPattern.getBox(m.getRow() - 2, m.getColumn() - 1).getDie().getColour() == m.getDie().getColour()) //if null?
                return false;
        } catch (OutOfWindowPattern e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 1, m.getColumn() - 2).getDie().getColour() == m.getDie().getColour())
                return false;
        } catch (OutOfWindowPattern e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 1, m.getColumn()).getDie().getColour() == m.getDie().getColour())
                return false;
        } catch (OutOfWindowPattern e) {}
        try {
            if (windowPattern.getBox(m.getRow(), m.getColumn() - 1).getDie().getColour() == m.getDie().getColour())
                return false;
        } catch (OutOfWindowPattern e) {}
        return true;
    }

    private boolean verifyNumber( MoveDie m) {

        WindowPattern windowPattern = m.getPlayer().getWindowPattern();
        try {
            if (windowPattern.getBox(m.getRow(), m.getColumn()).getValueRestriction() != m.getDie().getValue() && windowPattern.getBox(m.getRow(), m.getColumn()).getValueRestriction() != -1) //-1 equals to no restriction
                return false;
        } catch (OutOfWindowPattern e){}
        try {
            if (windowPattern.getBox(m.getRow() - 2, m.getColumn() - 1).getDie().getValue() == m.getDie().getValue()) //if null?
                return false;
        } catch (OutOfWindowPattern e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 1, m.getColumn() - 2).getDie().getValue() == m.getDie().getValue())
                return false;
        } catch (OutOfWindowPattern e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 1, m.getColumn()).getDie().getValue() == m.getDie().getValue())
                return false;
        } catch (OutOfWindowPattern e) {}
        try {
            if (windowPattern.getBox(m.getRow(), m.getColumn() - 1).getDie().getValue() == m.getDie().getValue())
                return false;
        } catch (OutOfWindowPattern e) {}
        return true;
    }

    private void calcResults() {
        
    }

}
