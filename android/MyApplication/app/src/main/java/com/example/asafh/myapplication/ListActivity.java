package com.example.asafh.myapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends ActionBarActivity {

    ArrayList<String> contestList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        network.setContests();
        JSONObject  appData = network.getContestsObj();
        setContentView(R.layout.activity_list);

        try {
            JSONArray events = appData.getJSONArray("events");
            for(int i=0;i<events.length();i++)
            {
                JSONObject event = events.getJSONObject(i);
                Log.v("json",event.getString("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView animalList=(ListView)findViewById(R.id.listView);


        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, contestList);
        // Set The Adapter
        animalList.setAdapter(arrayAdapter);
        // register onClickListener to handle click events on each item
        animalList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {

                String selectedAnimal=contestList.get(position);
                Toast.makeText(getApplicationContext(), "User Selected : "+selectedAnimal,   Toast.LENGTH_LONG).show();
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

    void getAnimalNames()
    {
        contestList.add("מירוץ ראשון לציון");
        contestList.add("מירוץ רחובות");
        contestList.add("כלשהו שקר כלזהו");
    }

   }
