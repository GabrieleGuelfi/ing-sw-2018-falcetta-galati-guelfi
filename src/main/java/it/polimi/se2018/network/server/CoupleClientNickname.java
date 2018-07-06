package it.polimi.se2018.network.server;

import it.polimi.se2018.network.client.ClientInterface;

public class CoupleClientNickname {
    private ClientInterface virtualClient;
    private String nickname;

    CoupleClientNickname(ClientInterface c, String s){
        this.virtualClient = c;
        this.nickname = s;
    }

    protected ClientInterface getVirtualClient(){
        return this.virtualClient;
    }

    protected String getNickname(){
        return this.nickname;
    }
}
