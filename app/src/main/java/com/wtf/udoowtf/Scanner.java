package com.wtf.udoowtf;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import junit.framework.Test;

import java.util.ArrayList;

/**
 * Created by alessandromorelli on 09/04/16.
 */
public class Scanner {


    private static final String TAG = Test.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 5000;
    private ArrayList<BeaconDevice> beaconList;
    private Activity activity;
    public Scanner(Activity context, Handler h, BluetoothAdapter ba) {
        beaconList = new BeaconStorage().getBeaconList();
        mHandler = h;
        mBluetoothAdapter = ba;
        this.activity = context;
    }

    public void BluetoothIsUp() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, 1);
        }
    }


    public void BluethootScan() {
        // Stops scanning after a pre-defined scan period.
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

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    int toRemove = -1;
                    for (int i = 0; i < beaconList.size(); i++) {
                        if (device.getAddress().equals(beaconList.get(i).getMac())) {
                            toRemove = i;
                        }
                    }

                    if (toRemove != -1) {
                        beaconList.remove(toRemove);
                    }

                    if(!mScanning){
                        Log.d(TAG, "scaning stopped");
                        Intent intent = new Intent(activity, NotificationsActivity.class);
                        activity.startActivity(intent);
                        for(int i=0; i<beaconList.size(); i++){
                            //TODO send an intent for the next activity.
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "startActivity");
                                    NotificationsActivity.beaconList = beaconList;
                                    Intent intent = new Intent(activity, NotificationsActivity.class);
                                    activity.startActivity(intent);
                                }
                            });
                        }
                    }
                }
            };
}
