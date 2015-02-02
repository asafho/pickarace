package com.example.asafh.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adig on 2/2/15.
 */
public class testList extends Activity {

    ListView lstTest;
    List<Map<String, String>> eventData = new ArrayList<Map<String, String>>();
    //Array Adapter that will hold our ArrayList and display the items on the ListView
    public JSONAdapter jSONAdapter ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        final general globalVariable = (general) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Initialize ListView
        lstTest = (ListView) findViewById(R.id.lstText);

        try {

            JSONObject appData = network.getContestsObj();
            JSONArray events = appData.getJSONArray("events");
            for (int i = 0; i < events.length(); i++) {
                JSONObject event = events.getJSONObject(i);
                if (globalVariable.getTopic().equals(event.getString("type"))) {
                    Map<String, String> datum = new HashMap<String, String>(2);
                    datum.put("name", event.getString("name"));
                    datum.put("date", event.getString("date"));
                    eventData.add(datum);
                }

            }
            JSONArray jArray = new JSONArray();
            jArray = events;

            jSONAdapter = new JSONAdapter(testList.this, jArray);
            //Set the above adapter as the adapter of choice for our list
            lstTest.setAdapter(jSONAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
