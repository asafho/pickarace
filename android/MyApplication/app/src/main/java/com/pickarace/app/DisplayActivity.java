package com.pickarace.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.pickarace.app.pickarace.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DisplayActivity extends Activity {

    private List<String> spinnerArray =  new ArrayList<String>();
    private String selectedRaceDistance,selectedRaceLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        TextView EventName = (TextView) findViewById(R.id.EventName);
        TextView EventDetails = (TextView) findViewById(R.id.EventDetails);
        //TextView EventRacesSpinner = (TextView) findViewById(R.id.RacesSpinner);

        String dataEventName             = getIntent().getStringExtra("eventName");
        String dataEventDetails          = getIntent().getStringExtra("eventDetails");
        ArrayList<String> racesTypeArray = getIntent().getStringArrayListExtra("eventRaceSpinner");


        EventName.setText(dataEventName);
        EventDetails.setText(dataEventDetails);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, racesTypeArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.RacesSpinner);
        sItems.setAdapter(adapter);
        sItems.setOnItemSelectedListener(new MyonItemSelectedListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
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

    public class MyonItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            selectedRaceDistance = parent.getItemAtPosition(pos).toString();
            HashMap<String, String> hashMap = (HashMap<String, String>)getIntent().getSerializableExtra("eventRaceDistanceLink");
            selectedRaceLink = hashMap.get(selectedRaceDistance).toString();
           // Toast.makeText(parent.getContext(), "distance: " + selectedRaceDistance + " link: " + hashMap.get(selectedRaceDistance).toString(), Toast.LENGTH_LONG).show();
        }

        public void onNothingSelected(AdapterView parent){
        }
    }

    public void openWebView(View view)
    {
        Intent intent = new Intent(this.getApplicationContext(), webViewActivity.class);
        intent.putExtra("eventLink",selectedRaceLink);
        startActivity(intent);
    }


}
