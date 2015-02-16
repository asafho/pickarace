package com.example.asafh.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListActivity extends ActionBarActivity {

   // private static final String url = "https://s3-us-west-2.amazonaws.com/pickarace/contests_test.json";
    private List<Events> contestList = new ArrayList<Events>();
    private ListView listView;
    private CustomListAdapter adapter;
    private ProgressDialog pDialog;
    ArrayList<String> racesSpinnerArray =  new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final AppController globalVariable = (AppController) getApplicationContext();

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        final JSONObject  appData = network.getContestsObj();
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);


        try {
            JSONArray events = appData.getJSONArray("events");
            for(int i=0;i<events.length();i++)
            {
                JSONObject jsonEvent = events.getJSONObject(i);
                if( globalVariable.getTopic().equals(jsonEvent.getString("type")))
               {
                    Events event = new Events();
                    event.setEventName(jsonEvent.getString("name"));
                    event.setEventLocation(jsonEvent.getJSONObject("location").getString("city"));
                    event.setEventDate(jsonEvent.getString("date"));
                    event.setEventDetails(jsonEvent.getJSONObject("details").getString("row1") + jsonEvent.getJSONObject("details").getString("row2") + jsonEvent.getJSONObject("details").getString("row3") + jsonEvent.getJSONObject("details").getString("row4"));

                    event.setThumbnailUrl(globalVariable.getS3RootURL() + jsonEvent.getJSONObject("vendor").getString("name") + ".png");

                   JSONArray racesArry = jsonEvent.getJSONArray("subtype");
                   ArrayList<String> raceType = new ArrayList<String>();
                   HashMap<String, String> raceDistanceLink = new HashMap();

                   for (int j = 0; j < racesArry.length(); j++) {
                       raceType.add((String) racesArry.getJSONObject(j).getString("distance"));
                       raceDistanceLink.put(racesArry.getJSONObject(j).getString("distance"),racesArry.getJSONObject(j).getString("link"));
                   }


                   event.setRacesType(raceType);
                   event.setRaceDistanceLink(raceDistanceLink);

                    contestList.add(event);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        hidePDialog();
        adapter = new CustomListAdapter(this, contestList);
        listView.setAdapter(adapter);



        /*

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();



        JsonArrayRequest eventsReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Events event = new Events();
                                try {
                                    event.setEventName(new String(obj.getString("name").getBytes(), "UTF-8"));
                                }
                                catch (UnsupportedEncodingException e){}
                                event.setEventDate(obj.getString("date"));
                                event.setThumbnailUrl("https://s3-us-west-2.amazonaws.com/pickarace/shvoong.png");
                                contestList.add(event);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidePDialog();

            }
        });
        hidePDialog();
        AppController.getInstance().addToRequestQueue(eventsReq);
        */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                Events event =  contestList.get(position);
                //Toast.makeText(getApplicationContext(), "User Selected : " + s.toString(), Toast.LENGTH_LONG).show();
                Intent displayActivityView = new Intent(ListActivity.this, DisplayActivity.class);
                displayActivityView.putExtra("eventName",event.getEventName());
                displayActivityView.putExtra("eventDetails",event.getEventDetails());
                displayActivityView.putExtra("eventRaceSpinner",event.getRacesType());
                displayActivityView.putExtra("eventRaceDistanceLink",event.getRaceDistanceLink());
                startActivity(displayActivityView);

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
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