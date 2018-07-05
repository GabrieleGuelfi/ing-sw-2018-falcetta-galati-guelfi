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
    private static JSONArray schemes;

    /**
     * hide the public constructor, no one can create an instance of this class
     */
    private HandleJSON() {
        throw new IllegalStateException("utility class");
    }

    /**
     * reset the chosen Window Pattern of the previous match
     */
    public static void newGame() {
        rand = new ArrayList<>();
        InputStream in = HandleJSON.class.getResourceAsStream("/fileutils/windowpattern");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(reader);
            schemes = (JSONArray) obj;
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
    }

    /**
     * create a Window Pattern from JSON file
     * @param nickname of the player to verify he chose a correct Window Pattern
     * @param firstIndex of the card in the file
     * @param secondIndex specify if it is the front or the back of the card
     * @return the WindowPattern or null if there was same errors
     */
    public static WindowPattern createWindowPattern (String nickname, int firstIndex, int secondIndex) {
        if (nickname!=null && !windowPattern.containsKey(nickname)) {
            out.println("nickname non valido in json "+nickname);
            return null;
        }
        else if (nickname!=null && windowPattern.get(nickname).get(0)!=firstIndex && windowPattern.get(nickname).get(1)!=firstIndex) {
            out.println("windowPattern non valida");
            return null;
        }

        JSONArray card = (JSONArray) schemes.get(firstIndex);
        JSONObject schema = (JSONObject) card.get(secondIndex);
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

    /**
     * @param nickname of the player who had to choose Window Pattern
     * @return two index of the two card chosen randomly
     */
    public static List<Integer> chooseWP(String nickname) {

        Integer index;
        Random generator = new Random();
        List<Integer> choice = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            index = generator.nextInt(schemes.size());
            while (rand.contains(index))
                index = generator.nextInt(schemes.size());
            rand.add(index);
            choice.add(index);
        }
        windowPattern.put(nickname, choice);
        return choice;
    }

    /**
     * create a Private Objective from JSON file
     * @param shade of the Private Objective
     * @return a pPrivate Objective
     */
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

    /**
     * @param number of the Tool to create from JSON
     * @return name and description of the Tool
     */
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

    public static String readFile(String file) throws FileNotFoundException {
        try (InputStream in = new FileInputStream(file)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONArray custom = (JSONArray) obj;
            if (!verifyWP(custom))
                return null;
            return custom.toJSONString();
        }
        catch (FileNotFoundException e) {
            throw e;
        }
        catch (IOException e) {
            out.println("ioexception");
        }
        catch (ParseException e) {
            out.println("parseException");
        }
        catch (ClassCastException e) {
            out.println("incorrect file");
        }
        return null;
    }

    public static void addWP(String file) {
        JSONParser parser = new JSONParser();
        try {
            JSONArray custom = (JSONArray) parser.parse(file);
            for (int i=0; i<custom.size(); i++)
                schemes.add(custom.get(i));
            System.out.println(schemes.toJSONString());
        } catch (ParseException e) {
            out.println("parseException in addWP");
        }
    }

    private static boolean verifyWP(JSONArray wp) {
        try {
            for (int a=0; a<wp.size(); a++) {
                JSONArray singleWP = (JSONArray) wp.get(a);
                if (singleWP.size()!=2)
                    return false;
                for (int b=0; b<2; b++) {
                    JSONObject schema = (JSONObject) singleWP.get(b);
                    if (schema.get("name")==null || schema.get("difficulty")==null || schema.get("grid")==null)
                        return false;
                    JSONArray grid = (JSONArray) schema.get("grid");
                    for (int k=0; k<WindowPattern.MAX_ROW; k++) {
                        JSONArray row = (JSONArray)grid.get(k);
                        boolean isNumber = true;
                        for (int i=0; i<10; i++){
                            if (i%2==0) {
                                isNumber = "num".equals(row.get(i));
                            }
                            else {
                                if (isNumber) {
                                    if ((int)(long) row.get(i)<0 || (int)(long)row.get(i)>6)
                                        return false;
                                }
                                else {
                                    if(!"col".equals(row.get(i-1)))
                                        return false;
                                    try {
                                        Colour.valueOf((String)row.get(i));
                                        if (Colour.WHITE.equals(row.get(i)))
                                            return false;
                                    }catch (IllegalArgumentException e) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
        return true;
    }

}
