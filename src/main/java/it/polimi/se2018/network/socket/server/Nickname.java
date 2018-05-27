package it.polimi.se2018.network.socket.server;

import java.util.ArrayList;

public class Nickname{
    private ArrayList<String> nicknames;
    private boolean gameStarted = false;

    Nickname(){ this.nicknames = new ArrayList<>();}

    public synchronized boolean verifyNickname(String s){

        if(this.nicknames.size() < 4 && !gameStarted){
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

    protected ArrayList<String> getNicknames() {
        return nicknames;
    }

    protected void setGameStarted(){this.gameStarted = true;}

    protected void notifyClientClosed(String client){
        if(!gameStarted) {
            for(String s : this.nicknames) {
                if(s.equals(client)) this.nicknames.remove(s);
            }
        }
    }
}
