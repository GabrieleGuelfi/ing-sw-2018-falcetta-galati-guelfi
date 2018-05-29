package it.polimi.se2018.events;

import it.polimi.se2018.network.socket.server.SagradaServer;
import it.polimi.se2018.network.socket.server.VirtualClient;
import it.polimi.se2018.utils.SagradaVisitor;
import it.polimi.se2018.view.VisitorView;

import java.io.Serializable;

/**
 * @author Federico Galati
 *
 */
public class Message implements Serializable  {

    protected String nickname;

    public Message(String s){
        this.nickname = s;
    }

    public Message() {
        this.nickname = "everybody";
    }

    public void accept(VisitorView v){
        v.visit(this);
    }

    public String getNickname(){
        return this.nickname;
    }
}
