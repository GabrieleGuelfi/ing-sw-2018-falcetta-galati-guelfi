package it.polimi.se2018.network.socket.server;

public class ServerTimer extends Thread {

    private SagradaServer sagradaServer;
    private int time;
    private boolean loop = true;

    protected ServerTimer(SagradaServer server, int t) {
        this.sagradaServer = server;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(time);
            if(loop == true) {
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