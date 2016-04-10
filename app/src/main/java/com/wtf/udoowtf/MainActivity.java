package com.wtf.udoowtf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by peppe on 09/04/16.
 */
public class MainActivity extends AppCompatActivity {

    private ListView beaconsListView;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // sostituiamo ListView con GridView
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new SensorAdapter(MainActivity.this, new BeaconStorage(), 0));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void next(View view) {
        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
    }


}



//    public static ArrayList<String> getBeaconsNames()
//    {
//        ArrayList<BeaconDevice> beacons;
//        beacons= new BeaconStorage().getBeaconList();
//        ArrayList<String>beaconsResult=new ArrayList<>();
//
//        for (BeaconDevice b:beacons)
//        {
//            beaconsResult.add(b.getDevice_name());
//        }
//        return  beaconsResult;
//    }

