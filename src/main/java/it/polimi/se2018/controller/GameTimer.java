package it.polimi.se2018.controller;

public class GameTimer extends Thread {

    private Controller controller;
    private int time;
    private boolean loop = true;

    GameTimer(Controller controller, int time) {
        this.controller = controller;
        this.time = time;
    }

    @Override
    public void run() {
        try {

            for(int i=time; i>0; i--) {
                if (loop) {
                    Thread.sleep(1000);
                }
            }

            if(loop) {
                controller.handleEndTime();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void stopTimer() {
        this.loop = false;
    }
}
