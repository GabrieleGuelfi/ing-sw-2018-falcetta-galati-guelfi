package it.polimi.se2018.network.socket.server;

import it.polimi.se2018.network.socket.client.ClientInterface;

public class CoupleClientNickname {
    private ClientInterface virtualClient;
    private String nickname;

    protected CoupleClientNickname(VirtualClient v, String s){
        this.virtualClient = v;
        this.nickname = s;
    }

    protected ClientInterface getVirtualClient(){
        return this.virtualClient;
    }

    protected String getNickname(){
        return this.nickname;
    }
}
