package com.example.hamchetemp;

import org.json.JSONException;
import org.json.JSONObject;

public class DataParsing {
    public String[] getParsedData(String result){
        String[] data = new String[2];
        try {
            JSONObject jsonObject=new JSONObject(result);
            String temp = jsonObject.getString("temp");
            String humi = jsonObject.getString("humi");

            data[0] = temp;
            data[1] = humi;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
