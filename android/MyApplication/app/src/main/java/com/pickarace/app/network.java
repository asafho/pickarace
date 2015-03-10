package com.pickarace.app;

import android.os.AsyncTask;
import android.util.Log;

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
    public List<Events> eventList = new ArrayList<Events>();


    @Override
    protected Long doInBackground(URL... params) {


        try {

            URL url = new URL("https://s3-eu-west-1.amazonaws.com/com.pickarace.app/contestsTest.json");
            final URL urlBucket = new URL("https://s3-eu-west-1.amazonaws.com/com.pickarace.app/");


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) {
                Log.e("AAA", "http error: " + conn.getErrorStream().toString());
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String outputString="";
            StringBuilder builder = new StringBuilder();
            Log.w("AAA", "Reading File");

            for (String line = null; (line = br.readLine()) != null;) {
                builder.append(line).append("\n");
            }

            conn.disconnect();
            contestsObj=new JSONObject(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        return null;
    }

    public static void setContests() {
/*
        new Thread(new Runnable() {
            public void run(){


                try {

                    URL url = new URL("https://s3-eu-west-1.amazonaws.com/com.pickarace.app/contestsTest.json");
                    final URL urlBucket = new URL("https://s3-eu-west-1.amazonaws.com/com.pickarace.app/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() != 200) {
                        Log.w("AAA",conn.getResponseMessage());
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
                    Log.w("AAA","****  contests obj");
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

        }
        ).start();
        */
    }

    public static JSONObject getContestsObj()
    {
        return contestsObj;
    }
}
