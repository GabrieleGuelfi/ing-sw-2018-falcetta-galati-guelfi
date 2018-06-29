package it.polimi.se2018.controller;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.WindowPattern;

public final class VerifyRules {

    private VerifyRules() {
        throw new IllegalStateException("utility class");
    }

    /**
     * Verify if there is another Die near the position chosen by user
     * @param w window pattern of the user
     * @param row chosen by user
     * @param column chosen by user
     * @return true if there is another die near
     */
    public static boolean isNearDie(WindowPattern w, int row, int column) {
        if (w.getEmptyBox() == 20) {
            return (row == 0 || row == WindowPattern.MAX_ROW-1 || column == 0 || column == WindowPattern.MAX_COL-1);
        }
        for(int i=row-1; i<row+2; i++ ) {
            for(int j=column-1; j<column+2; j++) {
                if (i!=row || j!=column) {
                    try {
                        if (w.getBox(i, j).getDie() != null)
                            return true;
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
        }
        return false;

    }

    /**
     * verify if there is another Die with the same colour near, or if there is colourRestriction in the Box
     * @param w window pattern of the user
     * @param row chosen by user
     * @param column chosen by user
     * @param die which the player want to place
     * @return false if Die break colour restriction
     */
    public static boolean verifyColor(WindowPattern w, int row, int column, Die die) {

        try {
            if (w.getBox(row, column).getColourRestriction() != die.getColour() && w.getBox(row, column).getColourRestriction() != Colour.WHITE)
                return false;
        } catch (IllegalArgumentException | NullPointerException e){}
        try {
            if (w.getBox(row - 1, column).getDie().getColour() == die.getColour())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row, column - 1).getDie().getColour() == die.getColour())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row, column + 1).getDie().getColour() == die.getColour())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row + 1, column).getDie().getColour() == die.getColour())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        return true;
    }

    /**
     * verify if there is another Die with the same number near, or if there is numberRestriction in the Box
     * @param w window pattern of the user
     * @param row chosen by user
     * @param column chosen by user
     * @param die which the player want to place
     * @return false if Die break number restriction
     */
    public static boolean verifyNumber(WindowPattern w, int row, int column, Die die) {

        try {
            if (w.getBox(row, column).getValueRestriction() != die.getValue() && w.getBox(row, column).getValueRestriction() != 0) //0 equals to no restriction
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row - 1, column).getDie().getValue() == die.getValue())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row, column - 1).getDie().getValue() == die.getValue())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row, column + 1).getDie().getValue() == die.getValue())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row + 1, column).getDie().getValue() == die.getValue())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        return true;
    }
}
