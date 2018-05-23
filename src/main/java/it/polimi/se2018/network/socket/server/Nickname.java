package it.polimi.se2018.network.socket.server;

import java.util.ArrayList;

public class Nickname {
    private ArrayList<String> nicknames;

    Nickname(){
        this.nicknames = new ArrayList<>();
    }

    public boolean verifyNickname(String s){
        if(this.nicknames.size() < 4){
            if(!this.nicknames.isEmpty()){
                for(String string: this.nicknames){
                    if(string.equals(s)) return false;
                }
            }
            this.nicknames.add(s);
            return true;
        }
        else{
            for(String string : this.nicknames){
                if(string.equals(s)) return true;
            }
            return false;
        }
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }
}
