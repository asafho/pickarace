package com.example.asafh.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends ActionBarActivity {

    private static final String url = "https://s3-us-west-2.amazonaws.com/pickarace/contests.json";
    private List<Events> contestList = new ArrayList<Events>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final general globalVariable = (general) getApplicationContext();


        JSONObject  appData = network.getContestsObj();
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, contestList);
        listView.setAdapter(adapter);

        /*
try {
    JsonArrayRequest movieReq = new JsonArrayRequest(url,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {


                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
        }
    });

}catch(JSONException d){

}
*/

        try {
            JSONArray events = appData.getJSONArray("events");
            for(int i=0;i<events.length();i++)
            {
                JSONObject jsonEvent = events.getJSONObject(i);
                if( globalVariable.getTopic().equals(jsonEvent.getString("type")))
                {
                    Events event = new Events();
                    event.setEventName("asdfasdf");
                    event.setEventDate("asdf");
                    contestList.add(event);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //animalList.setAdapter(arrayAdapter);
        // register onClickListener to handle click events on each item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                Events s =  contestList.get(position);
                Toast.makeText(getApplicationContext(), "User Selected : " + s.toString(), Toast.LENGTH_LONG).show();
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



}