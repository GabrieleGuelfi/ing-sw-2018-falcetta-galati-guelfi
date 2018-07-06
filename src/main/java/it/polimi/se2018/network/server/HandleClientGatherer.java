package it.polimi.se2018.network.server;

import static java.lang.System.*;

/**
 * Utility class used from server to handle the ClientGatherer, which is a thread
 * which listens for incoming socket connections
 */
public class HandleClientGatherer extends Thread {

    private final ClientGatherer clientGatherer;
    private boolean active;

    HandleClientGatherer( ClientGatherer c){

        this.clientGatherer= c;
        this.active = true;
        out.println("Client Gatherer is closing...\n");

    }

    @Override
    public void run(){
        synchronized (clientGatherer){
            while(this.active){
                try {
                    this.clientGatherer.wait();
                }
                catch(InterruptedException e){

                    this.clientGatherer.notifyAll();
                    Thread.currentThread().interrupt();
                }

            }
            this.clientGatherer.notifyAll();
            out.println("Client Gatherer is open.\n");
        }
    }

    protected void setClientGathererActive(){this.active = false; }
}
