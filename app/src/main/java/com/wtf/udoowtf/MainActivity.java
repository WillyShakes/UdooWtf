package com.wtf.udoowtf;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import android.support.design.widget.AppBarLayout;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;




/**
 * Created by peppe on 09/04/16.
 */
public class MainActivity extends AppCompatActivity {

    private ListView beaconsListView;
    private ArrayAdapter arrayAdapter;
    private boolean mScanning;
    private static final long SCAN_PERIOD = 5000;
    private static final String TAG = Test.class.getSimpleName();
    Handler mHandler;
    private BluetoothLeScanner scanner;
    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();
        beaconList = new BeaconStorage().getBeaconList();
        // sostituiamo ListView con GridView
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new SensorAdapter(MainActivity.this, new BeaconStorage().getBeaconList(), 0));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_new);
        return true;
    }
    public void next(View view) {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            // Stops scanning after a pre-defined scan period.
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            final BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else {
                scan();
            }
        }
    }

    private void scan() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "handler stop");
                mScanning = false;
                Log.d(TAG, "scaning stopped");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "startActivity");
                        NotificationsActivity.beaconList = beaconList;
                        Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
                        startActivity(intent);
                    }
                });
                scanner.stopScan(scanCallback);
            }
        }, SCAN_PERIOD);
        mScanning = true;
        startScanner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode== Activity.RESULT_OK)
            {
                scan();
            }
        }

    }

    /*public void next(View view) {

                // Stops scanning after a pre-defined scan period.
                final BluetoothManager bluetoothManager =
                        (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                final BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "handler stop");
                mScanning = false;
                Log.d(TAG, "scaning stopped");
                for(int i=0; i<beaconList.size(); i++){
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
                scanner.stopScan(scanCallback);
            }
        }, SCAN_PERIOD);
        mScanning = true;
        startScanner();


    }*/

    private void startScanner() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(MainActivity.BLUETOOTH_SERVICE);
        BluetoothAdapter adapter = bluetoothManager.getAdapter();
        scanner = adapter.getBluetoothLeScanner();
        ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).build();
        // scanner.startScan(scanFilters(), settings, scanCallback);
        scanner.startScan(scanCallback);
        System.out.println("Scanner started");
    }

    private List<ScanFilter> scanFilters() {
        ScanFilter filter;
        List<ScanFilter> list = new ArrayList<ScanFilter>(new BeaconStorage().getBeaconList().size());
        for (BeaconDevice d: new BeaconStorage().getBeaconList()) {
            filter = new ScanFilter.Builder().setDeviceAddress(d.getMac()).build();
            list.add(filter);
        }
        return list;
    }



    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d(TAG, "scaning stopped with error" + errorCode);
            mScanning = false;
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            Log.d(TAG, "scaning finished: "+device);
            if (device != null) {
                int toRemove = -1;
                for (int i = 0; i < beaconList.size(); i++) {
                    if (device.getAddress().equals(beaconList.get(i).getMac())) {
                        toRemove = i;
                    }
                }

                if (toRemove != -1) {
                    beaconList.remove(toRemove);

                }


            }
        }
    };

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