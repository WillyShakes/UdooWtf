package com.wtf.udoowtf;

/**
 * Created by Alessandro on 09/04/2016.
 */
public class BeaconDevice {
    private String mac;
    private String device_name;
    private String room_name;

    public BeaconDevice(String mac, String device_name, String room_name){
        this.mac = mac;
        this.device_name = device_name;
        this.room_name = room_name;
    }

    public String getMac(){
        return mac;
    }

    public String getDevice_name() {
        return device_name;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }


}
