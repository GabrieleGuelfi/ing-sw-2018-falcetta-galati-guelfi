package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.utils.Observable;

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
            e.printStackTrace();
        }
        this.o.notifyObservers(m);
    }
}
