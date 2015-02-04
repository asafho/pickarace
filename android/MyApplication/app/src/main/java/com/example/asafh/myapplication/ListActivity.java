package com.example.asafh.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends ActionBarActivity {

    static final String URL = "http://api.androidhive.info/music/music.xml";
    // XML node keys
    static final String KEY_SONG = "song"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_THUMB_URL = "thumb_url";
    final general globalVariable = (general) getApplicationContext();
    ArrayList<HashMap<String, String>> eventData = new ArrayList<HashMap<String, String>>();

    ListView list;
    LazyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        JSONObject appData = network.getContestsObj();

        try {
            JSONArray events = appData.getJSONArray("events");
            for(int i=0;i<events.length();i++)
            {
                JSONObject event = events.getJSONObject(i);
                if( globalVariable.getTopic().equals(event.getString("type")))
                {
                    HashMap<String, String> datum = new HashMap<String, String>();
                    datum.put("name", event.getString("name"));
                    datum.put("date", event.getString("date"));
                    eventData.add(datum);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        list=(ListView)findViewById(R.id.list);

        // Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, eventData);
        list.setAdapter(adapter);

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });
    }



    /*
    ArrayList<String> contestList = new ArrayList<String>();
    List<Map<String, String>> eventData = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final general globalVariable = (general) getApplicationContext();

        JSONObject  appData = network.getContestsObj();
        setContentView(R.layout.activity_list);

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, contestList);
        ListView animalList=(ListView)findViewById(R.id.listView);

        try {
            JSONArray events = appData.getJSONArray("events");
            for(int i=0;i<events.length();i++)
            {
                JSONObject event = events.getJSONObject(i);
                if( globalVariable.getTopic().equals(event.getString("type")))
                {
                    Map<String, String> datum = new HashMap<String, String>(2);
                    datum.put("name", event.getString("name"));
                    datum.put("date", event.getString("date"));
                    eventData.add(datum);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set The Adapter

        SimpleAdapter adapter = new SimpleAdapter(this, eventData,
                android.R.layout.two_line_list_item,
                new String[] {"name", "date"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});

        animalList.setAdapter(adapter);
        //animalList.setAdapter(arrayAdapter);
        // register onClickListener to handle click events on each item
        animalList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                Map s =  eventData.get(position);
                Toast.makeText(getApplicationContext(), "User Selected : "+ s.toString(),   Toast.LENGTH_LONG).show();
                Intent displayActivityView = new Intent(ListActivity.this, DisplayActivity.class);
                startActivity(displayActivityView);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    */
}