package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;

public class MessageRoundChanged extends Message {

    private int numRound;

    public MessageRoundChanged(String nickname, int numRound) {
        super(nickname);
        this.numRound = numRound;
    }

    public int getNumRound() {
        return numRound;
    }

}
