package it.polimi.se2018.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.out;

public class InputThread extends Thread {

    private int choice;
    private boolean isChosing;
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    InputThread() {
        isChosing = true;
    }

    int getChoice() {
        while (isChosing) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
        return choice;
    }

    @Override
    public void run() {

        try {
            System.in.read(new byte[System.in.available()]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(isChosing) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                isChosing = false;
                choice = -1;
                this.interrupt();
            }

            try {
                if(br.ready()) {
                    choice=Integer.parseInt(br.readLine());
                    isChosing = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void stopThread() {
        this.interrupt();
    }

}
