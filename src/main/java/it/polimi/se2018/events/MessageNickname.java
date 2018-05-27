package it.polimi.se2018.events;

public class MessageNickname extends Message{

    private boolean nicknameUsed;

    public MessageNickname(boolean value){
        this.nicknameUsed = value;
    }

    public boolean getBoolean(){
        return this.nicknameUsed;
    }
}
