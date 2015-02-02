package com.example.asafh.myapplication;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JSONAdapter extends BaseAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;
    public JSONAdapter (Activity activity, JSONArray jsonArray) {
        assert activity != null;
        assert jsonArray != null;

        this.jsonArray = jsonArray;
        this.activity = activity;
    }


    @Override public int getCount() {
        if(null==jsonArray)
            return 0;
        else
            return jsonArray.length();
    }

    @Override public JSONObject getItem(int position) {
        if(null==jsonArray) return null;
        else
            return jsonArray.optJSONObject(position);
    }

    @Override public long getItemId(int position) {
        JSONObject jsonObject = getItem(position);

        return jsonObject.optLong("id");
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.row, null);



        TextView text =(TextView)convertView.findViewById(R.id.txtAlertText);
        ImageView image =(ImageView)convertView.findViewById(R.id.vendorImage);

        JSONObject json_data = getItem(position);
        if(null!=json_data ){
            try {
                String jj = json_data.getString("name");
                //urlpath + jsonEvent.getJSONObject("vendor").getString("name") + ".png"
                String urlpath = "https://s3-us-west-2.amazonaws.com/pickarace/";
                Uri imageURI = Uri.parse(urlpath + json_data.getJSONObject("vendor").getString("name") + ".png");
                text.setText(jj);
                image.setImageURI(imageURI);
            }
            catch(JSONException e)
            {

            }
        }

        return convertView;
    }
}