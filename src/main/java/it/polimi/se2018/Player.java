package it.polimi.se2018;

import javax.swing.*;

public class Player {

    private String nickname;

    private PrivateObjective privateObjective;

    private int favorTokens;

    private WindowPattern windowPattern;

    private int points;

    Player(String nickname) {
        this.nickname = nickname;
    }

    public void setPrivateObjective(PrivateObjective privateObjective) {
        this.privateObjective = privateObjective;
    }

    public void setWindowPattern(WindowPattern windowPattern1, WindowPattern windowPattern2, WindowPattern windowPattern3) { //or array?

        //WindowPattern userChoice = 1, 2, 3
        this.windowPattern = userChoice;
        setFavorTokens(windowPattern.getFavorTokens());
    }

    private void setFavorTokens(int favorTokens) { //in UML is public, but is necessary?
        this.favorTokens = favorTokens;
    }

    public int getFavorTokens() {
        return favorTokens;
    }

    public void removeFavorTokens(int usage) { //usage = number of favorTokens on the used Toll Card at that moment
        favorTokens = favorTokens - usage;
    }

    public void setPoints() {}

    public int getPoints() {
        return points;
    }
}
