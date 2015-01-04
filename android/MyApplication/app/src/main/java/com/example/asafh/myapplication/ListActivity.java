package com.example.asafh.myapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ListActivity extends ActionBarActivity {

    ArrayList<String> animalsNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JSONObject  appData = network.getContests();
        setContentView(R.layout.activity_list);

        ListView animalList=(ListView)findViewById(R.id.listView);

        animalsNameList = new ArrayList<String>();


        Iterator<String> iter = appData.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                Object value = appData.get(key);
            } catch (JSONException e) {
                // Something went wrong!
            }
        }


        getAnimalNames();

        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, animalsNameList);
        // Set The Adapter
        animalList.setAdapter(arrayAdapter);
        // register onClickListener to handle click events on each item
        animalList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {

                String selectedAnimal=animalsNameList.get(position);
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

    public void setTableContent(){


    }

    void getAnimalNames()
    {
        animalsNameList.add("מירוץ ראשון לציון");
        animalsNameList.add("מירוץ רחובות");
        animalsNameList.add("כלשהו שקר כלזהו");
    }

   }
