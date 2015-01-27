package com.example.asafh.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ListActivity extends ActionBarActivity {


    private static final String TAG = ListActivity.class.getSimpleName();
    public static JSONObject contestsObj=new JSONObject();
    // Movies json url
    private static final String url = "https://s3-us-west-2.amazonaws.com/pickarace/contests.json";
    private static final String urlpath = "https://s3-us-west-2.amazonaws.com/pickarace/";
    private ProgressDialog pDialog;
    private List<Events> eventList = new ArrayList<Events>();
    private ListView listView;
    private CustomListAdapter adapter;
    DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
    private static final Date dummyDate = null;

    ArrayList<String> contestList = new ArrayList<String>();
    List<Map<String, String>> eventData = new ArrayList<Map<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final general globalVariable = (general) getApplicationContext();

        JSONObject  appData = network.getContestsObj();
        setContentView(R.layout.activity_main);
        ListView animalList=(ListView)findViewById(R.id.list);

        try {
            JSONArray events = appData.getJSONArray("events");
            for(int i=0;i<events.length();i++)
            {
                JSONObject jsonEvent = events.getJSONObject(i);
                if( globalVariable.getTopic().equals(jsonEvent.getString("type")))
                {
                   /*
                    Map<String, String> datum = new HashMap<String, String>(2);
                    datum.put("name", event.getString("name"));
                    datum.put("date", event.getString("date"));
                    eventData.add(datum);
                    */
                    Events event = new Events();
                    event.setEventName(jsonEvent.getString("name"));
                    event.setThumbnailUrl(urlpath + jsonEvent.getJSONObject("vendor").getString("name") + ".png");
                    event.setEventLocation(jsonEvent.getJSONObject("location").getString("city"));
                    try{
                        event.setEventDate((Date)df.parse(jsonEvent.getString("date")));
                    }catch(ParseException e){
                        event.setEventDate(dummyDate);
                    }
                    // adding movie to movies array
                    eventList.add(event);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new CustomListAdapter(this, eventList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));

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

    public JSONObject getData()
    {
        return contestsObj;
    }

    public void setData()
    {

        System.out.println("AAA");
        new Thread(new Runnable() {
            public void run(){
                try {
                    System.out.println("AAAA");
                    URL url = new URL("https://s3-us-west-2.amazonaws.com/pickarace/contests.json");
                    final URL urlBucket = new URL("https://s3-us-west-2.amazonaws.com/pickarace/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() != 200) {
                        System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
                        return;
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    String outputString="";
                    String line="";
                    while ((line = br.readLine()) != null) {
                        outputString = outputString+line;
                    }
                    conn.disconnect();
                    contestsObj=new JSONObject(outputString);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
