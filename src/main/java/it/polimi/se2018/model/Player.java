package it.polimi.se2018.model;

public class Player {

    private String nickname;

    private PrivateObjective privateObjective;

    private int favorTokens;

    private WindowPattern windowPattern;

    private int points;

    private boolean placedDie;

    private boolean usedTool;

    public Player(String nickname) {
        this.nickname = nickname;
        points = 0;
        placedDie = false;
        usedTool = false;
    }

    public String getNickname() { return this.nickname; }

    public boolean isUsedTool() {
        return usedTool;
    }

    public boolean isPlacedDie() {
        return placedDie;
    }

    public void setPlacedDie(boolean placedDie) {
        this.placedDie = placedDie;
    }

    public void setUsedTool(boolean usedTool) {
        this.usedTool = usedTool;
    }

    /**
     * @return the window pattern of this player
     */
    public WindowPattern getWindowPattern() {
        return windowPattern;
    }

    /**
     * @param privateObjective set the private objective of this player
     */
    public void setPrivateObjective(PrivateObjective privateObjective) {
        this.privateObjective = privateObjective;
    }

    /**
     * @param windowPattern set the window pattern chosen by player
     */
    public void setWindowPattern(WindowPattern windowPattern) {
        if (windowPattern==null) throw new IllegalArgumentException("Invalid window pattern!");
        this.windowPattern = windowPattern;
        setFavorTokens(windowPattern.getDifficulty());
    }

    /**
     * @param favorTokens the amount of favor tokens given to this player
     */
    private void setFavorTokens(int favorTokens) { //in UML is public, but is necessary?
        this.favorTokens = favorTokens;
    }

    /**
     * @return favor tokens of this player
     */
    public int getFavorTokens() {
        return favorTokens;
    }

    /**
     * remove the amount of favor tkens used to use a tool
     * @param usage favor tokens used
     */
    public void removeFavorTokens(int usage) { //usage = number of favorTokens on the used Toll Card at that moment
        if(this.favorTokens-usage<0) throw new IllegalStateException("Insufficient favor tokens!");
        favorTokens = favorTokens - usage;
    }

    /**
     * @param points score to add to the points of this player
     */
    public void addPoints(int points) {
        this.points = this.points + points;
    }

    /**
     * @return the score of this player
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return the Private Objective of this player
     */
    public PrivateObjective getPrivateObjective() {
        return this.privateObjective;
    }


    /**
     * @return Copy of this Object
     */
    public Player copy(){
        Player playerCopy = new Player(this.nickname);

        playerCopy.favorTokens = this.favorTokens;
        playerCopy.windowPattern = this.windowPattern.copy();
        playerCopy.points = this.points;
        playerCopy.placedDie = this.placedDie;
        playerCopy.usedTool = this.usedTool;

        return playerCopy;
    }

}
