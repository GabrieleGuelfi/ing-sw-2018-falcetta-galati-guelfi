package it.polimi.se2018.controller;

import it.polimi.se2018.events.MessageChooseWP;
import it.polimi.se2018.model.Box;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.view.VirtualView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Utils {

    private static List<Integer> rand = new ArrayList<>();
    private static Map<String, Integer> windowPattern= new HashMap<>();

    /**
     * send four window pattern to player, who choose one of them
     * @param player is the player to send window pattern
     */
    static  void giveWindowPatterns(Player player){

        List<WindowPattern> patterns = new ArrayList<>();
        JSONParser parser = new JSONParser();
        List<Integer> rand = new ArrayList<>();
        Integer index;
        try {
            Object obj = parser.parse(new FileReader("./src/main/java/windowpattern/windowpattern"));
            JSONArray schemes = (JSONArray) obj;
            Random generator = new Random();
            for (int i=0; i<2; i++) {
                index = generator.nextInt(12);
                while (rand.contains(index))
                    index = generator.nextInt(12);
                rand.add(index);
                JSONArray schema = (JSONArray) schemes.get(index);
                for (int j = 0; j < 2; j++) {
                    JSONObject schema1 = (JSONObject) schema.get(j);
                    WindowPattern w = new WindowPattern((String) schema1.get("name"), (int) (long) schema1.get("difficulty"));
                    JSONArray grid = (JSONArray) schema1.get("grid");
                    createWindowPattern(grid, w);
                    patterns.add(w);
                }
            }
            //send(patterns)
        }
        catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
        catch (IOException e) {
            System.out.println("ioexception");
        }
        catch (ParseException e) {
            System.out.println("parseException");
        }

    }

    /**
     * create a window pattern from a json Object
     * @param grid is the grid from json file
     * @param w1 is the window pattern to be created
     */
    static void createWindowPattern (JSONArray grid, WindowPattern w1) {
        for (int k=0; k<WindowPattern.MAX_ROW; k++) {
            JSONArray row = (JSONArray)grid.get(k);
            boolean isNumber = true;
            int j=1;
            for (int i=0; i<10; i++){
                if (i%2==0) {
                    isNumber = "num".equals(row.get(i));
                }
                else {
                    if (isNumber) {
                        w1.setBox(new Box((int)(long)row.get(i)), k, i-j);
                    }
                    else {
                        w1.setBox(new Box(Colour.valueOf((String)row.get(i))), k, i-j);
                    }
                    j++;
                }
            }
        }
    }

    static void chooseWP(String player, VirtualView view) {

        Integer index;
        Random generator = new Random();

        for (int i = 0; i < 2; i++) {
            index = generator.nextInt(12);
            while (rand.contains(index))
                index = generator.nextInt(12);
            rand.add(index);
            windowPattern.put(player, index);
        }
        view.send(new MessageChooseWP(player, rand.get(rand.size()-2), rand.get(rand.size()-1)));
    }
}
