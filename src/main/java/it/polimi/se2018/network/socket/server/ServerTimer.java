package it.polimi.se2018.network.socket.server;

public class ServerTimer extends Thread {

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
            Thread.sleep(time);
            if(loop) {
                this.sagradaServer.startGame();
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }

    }

    protected void stopTimer(){
        this.loop = false;
    }
}