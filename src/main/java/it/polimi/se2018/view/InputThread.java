package it.polimi.se2018.view;

import it.polimi.se2018.utils.StringJSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            final long skip = System.in.skip(System.in.available());
        } catch (IOException e) {
            final Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, e.getMessage());
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
                    try {
                        choice = Integer.parseInt(br.readLine());
                        if(choice==-1) choice=-2;
                        isChosing = false;
                    }
                    catch (NumberFormatException e) {
                        out.println(StringJSON.printStrings("askStrings", "invalidChoice"));
                    }
                }
            } catch (IOException e) {
                final Logger logger = Logger.getLogger(this.getClass().getName());
                logger.log(Level.WARNING, e.getMessage());
            }
        }

    }

    public void stopThread() {
        this.interrupt();
    }

}
