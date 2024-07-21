package com.example.finalproject;

import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import java.util.ArrayList;
        import java.util.List;
public class JsonParser {
    public static ArrayList<String> getObjectFromJson(String json) {
        ArrayList<String> pizzaTypes;
        try {
            JSONObject jsonO = new JSONObject(json);
            pizzaTypes = new ArrayList<>();
            JSONArray types = jsonO.getJSONArray("types");
            for(int counter=0;counter<types.length();counter++){
                pizzaTypes.add(types.getString(counter));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return pizzaTypes;
    }
}
