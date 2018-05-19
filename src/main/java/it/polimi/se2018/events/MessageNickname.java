package it.polimi.se2018.events;

import java.io.Serializable;

public class MessageNickname extends Message implements Serializable {

    private String nickname;

    public MessageNickname(String s) {
        super("foo");
        nickname = s;
    }

    public String getNickname() {
        return this.nickname;
    }

    @Override
    public void notifyThis() {
        this.getServer().setNickname(this);
    }
}
