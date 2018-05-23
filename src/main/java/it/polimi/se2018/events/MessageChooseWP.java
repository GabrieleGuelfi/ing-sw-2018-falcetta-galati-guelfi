package it.polimi.se2018.events;

public class MessageChooseWP extends Message {

    private int firstIndex;
    private int secondIndex;

    public MessageChooseWP(String nickname, int firstIndex, int secondIndex) {
        super(nickname);
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
    }

}
