package it.polimi.se2018.network.socket.server;

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
                System.out.println(time + "seconds remains");
                Thread.sleep(1000);
            }
            if(loop) {
                this.sagradaServer.startGame();
            }
        }catch(InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

    }

    protected void stopTimer(){
        this.loop = false;
    }
}