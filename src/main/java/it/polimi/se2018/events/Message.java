package it.polimi.se2018.events;

import it.polimi.se2018.network.socket.server.SagradaServer;
import it.polimi.se2018.network.socket.server.VirtualClient;
import it.polimi.se2018.utils.SagradaVisitor;

import java.io.Serializable;

/**
 * @author Federico Galati
 *
 */
public class Message implements Serializable  {

    String nickname;

    public Message(String s){
        this.nickname = s;
    }

    public void accept(SagradaVisitor v){
        v.visit(this);
    }

    public String getNickname(){
        return this.nickname;
    }
}
