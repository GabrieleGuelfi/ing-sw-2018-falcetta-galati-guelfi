package it.polimi.se2018.events;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.network.socket.server.VisitorServer;
import it.polimi.se2018.view.VisitorView;

import java.io.Serializable;

public class Message implements Serializable{

    protected String nickname;

    public Message(String s){
        this.nickname = s;
    }

    public Message() {
        this.nickname = "everybody";
    }

    public void accept(VisitorController v){
        v.visit(this);
    }

    public void accept(VisitorView v){
        v.visit(this);
    }

    public void accept(VisitorServer v) {v.visit(this);}

    public String getNickname(){
        return this.nickname;
    }
}
