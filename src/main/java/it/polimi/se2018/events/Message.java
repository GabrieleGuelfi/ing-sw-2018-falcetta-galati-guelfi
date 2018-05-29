package it.polimi.se2018.events;

import java.io.Serializable;

/**
 * @author Federico Galati
 *
 */
public class Message implements Serializable  {

    private String nickname;

    public Message(String s){
        this.nickname = s;
    }

    public Message() {
        this.nickname = "everybody";
    }

    public void accept(SagradaVisitor v){
        v.visit(this);
    }

    public String getNickname(){
        return this.nickname;
    }
}
