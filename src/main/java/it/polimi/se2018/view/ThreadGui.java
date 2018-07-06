package it.polimi.se2018.view;

import it.polimi.se2018.network.client.SagradaClient;

public class ThreadGui extends Thread{

    private ViewInterface v;
    private SagradaClient sagradaClient;

    ThreadGui( ViewInterface viewInterface){
        v = viewInterface;
    }

    @Override
    public void run(){

        sagradaClient = new SagradaClient(v);
    }
}
