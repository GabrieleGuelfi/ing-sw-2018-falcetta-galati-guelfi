package it.polimi.se2018.view;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class InputThread extends Thread {

    private int choice;
    private int limit=0;
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public InputThread() {
        //scanner = new Scanner(System.in);
        choice = -2;
    }

    public int getChoice() {
        while (choice == -2) {
            if(choice==-1) return choice;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return choice;
    }

    @Override
    public void run() {

        try {
            System.in.read(new byte[System.in.available()]);
        } catch (IOException e) {
            System.err.println("Error in Sysin flush");
            e.printStackTrace();
        }

        while(choice==-2) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                choice = -1;
            }
            //System.out.println("Verifico se puo leggere");
            try {
                if(br.ready()) choice=Integer.parseInt(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void stopThread() {
        this.interrupt();
    }

}
