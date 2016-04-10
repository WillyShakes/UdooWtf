package com.wtf.udoowtf;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import junit.framework.Test;

import java.util.ArrayList;


/**
 * Created by peppe on 09/04/16.
 */
public class MainActivity extends AppCompatActivity {

    private ListView beaconsListView;
    private ArrayAdapter arrayAdapter;
    private boolean mScanning;
    private static final long SCAN_PERIOD = 10000;
    private static final String TAG = Test.class.getSimpleName();
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler =  new Handler();
        beaconList = new BeaconStorage().getBeaconList();
        // sostituiamo ListView con GridView
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new SensorAdapter(MainActivity.this, new BeaconStorage().getBeaconList(), 0));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        mLeScanCallback =
                new BluetoothAdapter.LeScanCallback() {

                    @Override
                    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                        new Thread(new Runnable() {
                            public void run() {
                                int toRemove = -1;
                                for (int i = 0; i < beaconList.size(); i++) {
                                    if (device.getAddress().equals(beaconList.get(i).getMac())) {
                                        toRemove = i;
                                    }
                                }

                                if (toRemove != -1) {
                                    beaconList.remove(toRemove);
                                }

                                if (!mScanning) {
                                    Log.d(TAG, "scaning stopped");
                                    for (int i = 0; i < beaconList.size(); i++) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.d(TAG, "startActivity");
                                                NotificationsActivity.beaconList = beaconList;
                                                Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                };
    }
    public void next(View view) {
        new Thread(new Runnable() {
            public void run() {
                // Stops scanning after a pre-defined scan period.
                final BluetoothManager bluetoothManager =
                        (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                final BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }, SCAN_PERIOD);
                mScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        }).start();




    }

    private ArrayList<BeaconDevice> beaconList;

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
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