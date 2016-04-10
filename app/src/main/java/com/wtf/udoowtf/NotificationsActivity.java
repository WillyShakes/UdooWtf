package com.wtf.udoowtf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    public static ArrayList<BeaconDevice> beaconList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_notifications);

        ListView listView = (ListView) findViewById(R.id.wtf_notifications_list);
        listView.setAdapter(new SensorAdapter(this, beaconList, 1));

    }
}
