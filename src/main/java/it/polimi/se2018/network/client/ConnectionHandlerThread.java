package it.polimi.se2018.network.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.utils.Observable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandlerThread extends Thread {

    private Observable o;
    private Message m;

    public ConnectionHandlerThread(Observable o, Message m) {
        this.o = o;
        this.m = m;
    }

    @Override

    public void run(){
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            final Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, e.getMessage());
            Thread.currentThread().interrupt();
        }
        this.o.notifyObservers(m);
    }
}
