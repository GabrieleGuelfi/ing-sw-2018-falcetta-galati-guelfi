package it.polimi.se2018.utils;

import it.polimi.se2018.model.Box;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.PrivateObjective;
import it.polimi.se2018.model.WindowPattern;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

import static java.lang.System.*;

public final class HandleJSON {

    private static List<Integer> rand;
    private static Map<String, List<Integer>> windowPattern = new HashMap<>();

    private HandleJSON() {
        throw new IllegalStateException("utility class");
    }

    public static void newGame() {
        rand = new ArrayList<>();
    }

    public static WindowPattern createWindowPattern (String nickname, int firstIndex, int secondIndex) {
        if (nickname!=null && !windowPattern.containsKey(nickname)) {
            out.println("nickname non valido in json "+nickname);
            return null;
        }
        else if (nickname!=null && windowPattern.get(nickname).get(0)!=firstIndex && windowPattern.get(nickname).get(1)!=firstIndex) {
            out.println("windowPattern non valida");
            return null;
        }

        InputStream in = HandleJSON.class.getResourceAsStream("/fileutils/windowpattern");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(reader);
            JSONArray schemes = (JSONArray) obj;
            schemes = (JSONArray) schemes.get(firstIndex);
            JSONObject schema = (JSONObject) schemes.get(secondIndex);
            WindowPattern w = new WindowPattern((String) schema.get("name"), (int) (long) schema.get("difficulty"));
            JSONArray grid = (JSONArray) schema.get("grid");
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
                            w.setBox(new Box((int)(long)row.get(i)), k, i-j);
                        }
                        else {
                            w.setBox(new Box(Colour.valueOf((String)row.get(i))), k, i-j);
                        }
                        j++;
                    }
                }
            }
            return w;
        }
        catch (FileNotFoundException e) {
            out.println("file not found");
        }
        catch (IOException e) {
            out.println("ioexception");
        }
        catch (ParseException e) {
            out.println("parseException");
        }
        return null;
    }

    public static List<Integer> chooseWP(String nickname) {

        Integer index;
        Random generator = new Random();
        List<Integer> choice = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            index = generator.nextInt(12);
            while (rand.contains(index))
                index = generator.nextInt(12);
            rand.add(index);
            choice.add(index);
        }
        windowPattern.put(nickname, choice);
        return rand;
    }

    public static PrivateObjective createPrivateObjective(Colour shade) {

        InputStream in = HandleJSON.class.getResourceAsStream("/fileutils/privateObjective");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        JSONParser parser = new JSONParser();
        Object obj;
        try {
            obj = parser.parse(reader);
        } catch (IOException | ParseException e) {
            out.println("Failed to load resource privateObjective");
            return null;
        }

        JSONObject descriptions = (JSONObject) obj;

        return new PrivateObjective(shade, descriptions.get(shade.toString()).toString());

    }

    public static List<String> createTool(String number) {
        List<String> tool = new ArrayList<>();
        InputStream in = HandleJSON.class.getResourceAsStream("/fileutils/tools");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        JSONParser parser = new JSONParser();
        Object obj;
        try {
            obj = parser.parse(reader);
        } catch (IOException | ParseException e) {
            out.println("Failed to load resource tools");
            return tool;
        }

        JSONObject tools = (JSONObject) obj;
        JSONArray string = (JSONArray) tools.get(number);

        tool.add((String) string.get(0));
        tool.add((String) string.get(0));

        return tool;
    }

}
