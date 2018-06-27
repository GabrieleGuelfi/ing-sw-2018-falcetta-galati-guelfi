package it.polimi.se2018.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class StringJSON {

    private StringJSON() {
        throw new IllegalStateException("utility class");
    }

    public static String printStrings(String type, String field) {
        InputStream in = StringJSON.class.getResourceAsStream("/fileutils/gamestringseng");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        JSONObject strings = (JSONObject) obj;
        JSONObject typeStrings = (JSONObject) strings.get(type);
        return (String) typeStrings.get(field);
    }
}
