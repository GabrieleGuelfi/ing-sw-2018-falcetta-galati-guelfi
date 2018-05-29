package it.polimi.se2018.model;

/**
 * possible colour of a die or colour restriction of a box, WHITE is no restriction
 */
public enum Colour {

    WHITE,
    GREEN,
    RED,
    YELLOW,
    PURPLE,
    BLUE;

    public static char getFirstLetter(Colour colour) {
         switch (colour) {
             case WHITE: return 'W';
             case GREEN: return 'G';
             case RED: return 'R';
             case YELLOW: return 'Y';
             case PURPLE: return 'P';
             case BLUE: return 'B';
         }

         return 'W';
    }

}
