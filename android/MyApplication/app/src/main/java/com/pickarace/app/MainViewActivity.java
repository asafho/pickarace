package com.pickarace.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.pickarace.app.pickarace.R;

public class MainViewActivity extends Activity {
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        String adUnitID = getResources().getString(R.string.ad_unit_id);
        interstitial.setAdUnitId(adUnitID);

        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder().build();

        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_view, menu);
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


    public void runningButtonOnClick(View view) {
        FlurryAgent.logEvent("Pressed On Running Option");
        final AppController globalVariable = (AppController) getApplicationContext();
        globalVariable.setTopic("running");
        Log.w("AAA", "running button clicked");
        Intent openStep = new Intent(MainViewActivity.this,  ListActivity.class);
        startActivity(openStep);
        displayInterstitial();
    }

    public void swimmingButtonOnClick(View view) {
        FlurryAgent.logEvent("Pressed On swimming Option");
        final AppController globalVariable = (AppController) getApplicationContext();
        globalVariable.setTopic("swimming");
        Log.v("action", "swimming button clicked");
        Intent openStep = new Intent(MainViewActivity.this, ListActivity.class);
        Log.v("internet test: ", String.valueOf(AppController.isConnected(this.getApplicationContext())));
        startActivity(openStep);
        displayInterstitial();
    }

    public void bikingButtonOnClick(View view) {
        FlurryAgent.logEvent("Pressed On biking Option");
        final AppController globalVariable = (AppController) getApplicationContext();
        globalVariable.setTopic("biking");
        Log.v("action", "biking button clicked");
        Intent openStep = new Intent(MainViewActivity.this, ListActivity.class);
        Log.v("internet test: ", String.valueOf(AppController.isConnected(this.getApplicationContext())));
        startActivity(openStep);
        displayInterstitial();
    }

    public void triathlonButtonOnClick(View view) {
        FlurryAgent.logEvent("Pressed On triathlon Option");
        final AppController globalVariable = (AppController) getApplicationContext();
        globalVariable.setTopic("triathlon");
        Log.v("action", "triathlon button clicked");
        Intent openStep = new Intent(MainViewActivity.this, ListActivity.class);
        Log.v("internet test: ", String.valueOf(AppController.isConnected(this.getApplicationContext())));
        startActivity(openStep);
        displayInterstitial();
    }



    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    /**
     * A placeholder fragment containing a simple view. This fragment
     * would include your content.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_ad, container, false);
            return rootView;
        }
    }


    public static class AdFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }

        @Override
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);
            AdView mAdView = (AdView) getView().findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
    }

}
