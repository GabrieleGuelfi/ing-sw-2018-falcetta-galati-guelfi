package it.polimi.se2018.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class StringJSON {

    /**
     * hide the public constructor, no one can create an instance of this class
     */
    private StringJSON() {
        throw new IllegalStateException("utility class");
    }

    /**
     * show strings to users rred from JSON file
     * @param type of the strings
     * @param field specify the single string
     * @return the string searched
     */
    public static String printStrings(String type, String field) {
        InputStream in = StringJSON.class.getResourceAsStream("/fileutils/gamestringseng");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(reader);
        } catch (IOException | ParseException e) {
            final Logger logger = Logger.getLogger(StringJSON.class.getName());
            logger.log(Level.WARNING, e.getMessage());
        }
        JSONObject strings = (JSONObject) obj;
        JSONObject typeStrings = (JSONObject) strings.get(type);
        return (String) typeStrings.get(field);
    }
}
