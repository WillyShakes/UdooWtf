package com.wtf.udoowtf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Umberto on 09/04/2016.
 */
public class SensorAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<BeaconDevice> beaconList;
    private final int type;
    public SensorAdapter(Context context, ArrayList<BeaconDevice> beaconList, int type) {
        this.context=context;
        this.beaconList = beaconList;
        this.type=type;
    }

    @Override
    public int getCount() {
        return beaconList.size();
    }

    @Override
    public Object getItem(int position) {
        return beaconList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BeaconDevice b;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;
        if(type==0) {
            v = inflater.inflate(R.layout.sensor_card_small, null);
        } else  {
            v = inflater.inflate(R.layout.sensor_card_big, null);
        }
        TextView wtfSensorName = (TextView) v.findViewById(R.id.wtf_sensor_name);
        b = beaconList.get(position);
        wtfSensorName.setText(b.getDevice_name());
        TextView wtfSensorRoom= (TextView) v.findViewById(R.id.wtf_sensor_room);
        wtfSensorRoom.setText(b.getRoom_name());
        ImageView wtfImage = (ImageView) v.findViewById(R.id.wtf_sensor_image);
        wtfImage.setImageResource(b.getImage());
        return v;
    }
}
