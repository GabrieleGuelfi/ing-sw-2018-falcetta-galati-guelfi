package it.polimi.se2018.network.server;

import static java.lang.System.*;

public class ServerTimer extends Thread{

    private SagradaServer sagradaServer;
    private int time;
    private boolean loop = true;

    ServerTimer(SagradaServer server, int t) {
        this.sagradaServer = server;
        this.time = t;
    }

    @Override
    public void run() {
        try {
            for(int i = time; i > 0; i--){
                if(loop) {
                    out.println(i + " seconds remains");
                    Thread.sleep(1000);
                }
            }
            if(loop) {
                this.sagradaServer.startGame();
            }
            else{
                out.println("Not ready. Wait a moment please.\n");
            }
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }

    protected void stopTimer(){
        this.loop = false;
        Thread.currentThread().interrupt();
    }
}