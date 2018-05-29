package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;

public class MessageErrorMove extends Message {

    private  String reason;
    private  boolean hasUsedTool;
    private  boolean hasPlacedDie;

    public MessageErrorMove(String nickname, String reason, boolean hasPlacedDie, boolean hasUsedTool) {
        super(nickname);
        this.hasPlacedDie = hasPlacedDie;
        this.hasUsedTool = hasUsedTool;
        this.reason = reason;
    }

    public boolean hasPlacedDie() {
        return hasPlacedDie;
    }

    public boolean hasUsedTool() {
        return hasUsedTool;
    }

    public String getReason() {
        return reason;
    }
}
