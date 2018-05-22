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

    public WindowPattern getWindowPattern() {
        return windowPattern;
    }

    public void setPrivateObjective(PrivateObjective privateObjective) {
        this.privateObjective = privateObjective;
    }

    public void setWindowPattern(WindowPattern windowPattern) {
        if (windowPattern==null) throw new IllegalArgumentException("Invalid window pattern!");
        this.windowPattern = windowPattern;
        setFavorTokens(windowPattern.getDifficulty());
    }

    private void setFavorTokens(int favorTokens) { //in UML is public, but is necessary?
        this.favorTokens = favorTokens;
    }

    public int getFavorTokens() {
        return favorTokens;
    }

    public void removeFavorTokens(int usage) { //usage = number of favorTokens on the used Toll Card at that moment
        if(this.favorTokens-usage<0) throw new IllegalStateException("Insufficient favor tokens!");
        favorTokens = favorTokens - usage;
    }

    public void addPoints(int points) {
        this.points = this.points + points;
    }

    public int getPoints() {
        return points;
    }

    public PrivateObjective getPrivateObjective() {
        return this.privateObjective;
    }


    //MISS THE COPY OF OBJECTIVE

    /**
     *
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
