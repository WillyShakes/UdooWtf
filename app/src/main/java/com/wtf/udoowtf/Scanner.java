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
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;

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
    private BluetoothLeScanner scanner;

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
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                scanner.stopScan(scanCallback);
            }
        }, SCAN_PERIOD);
        mScanning = true;
        startScanner();
    }

    private void startScanner() {
        BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(activity.BLUETOOTH_SERVICE);
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

                if(!mScanning){
                    Log.d(TAG, "scaning stopped");
                    for(int i=0; i<beaconList.size(); i++){
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
        }
    };





}
