package com.wtf.udoowtf;

import java.util.ArrayList;

/**
 * Created by Alessandro on 09/04/2016.
 */
public class BeaconStorage {
    private ArrayList<BeaconDevice> beaconList = new ArrayList<BeaconDevice>();

    public BeaconStorage(){
        beaconList.add(new BeaconDevice("D7:B3:84:F8:C2:4B","A","room1"));
        beaconList.add(new BeaconDevice("EC:FD:86:8A:92:28","B","room2"));
    }

    public ArrayList<BeaconDevice> getBeaconList(){

        return beaconList;
    }
}
