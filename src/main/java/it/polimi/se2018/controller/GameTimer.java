package it.polimi.se2018.controller;

public class GameTimer extends Thread {


    /**
     * Implementation of game timer
     * This thread will count down for the duration of time, and then call controller.handleEndtime()
     * It can be stopped with stopTimer method.
     */
    private Controller controller;
    private int time;
    private boolean loop;

    GameTimer(Controller controller, int time) {
        this.controller = controller;
        this.time = time;
        this.loop = true;
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
