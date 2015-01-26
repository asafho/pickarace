package com.example.asafh.myapplication;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class network extends AsyncTask<URL, Integer, Long>{
    public static JSONObject contestsObj=new JSONObject();
    private static final String TAG = network.class.getSimpleName();
    public List<Events> eventList = new ArrayList<Events>();


    @Override
    protected Long doInBackground(URL... params) {
        return null;
    }

    public static void setContests() {

        new Thread(new Runnable() {
            public void run(){
                try {

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

    public static JSONObject getContestsObj()
    {
        return contestsObj;
    }
}
