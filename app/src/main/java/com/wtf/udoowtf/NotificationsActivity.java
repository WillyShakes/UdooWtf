package com.wtf.udoowtf;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_notifications);

        ListView listView = (ListView) findViewById(R.id.wtf_notifications_list);
        listView.setAdapter(new SensorAdapter(this, new BeaconStorage(), 1 ));

    }
}
