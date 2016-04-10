package com.wtf.udoowtf;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Umberto on 09/04/2016.
 */
public class SensorAdapter extends BaseAdapter {

    private final Context context;
    private final BeaconStorage beaconStorage;
    private final int type;
    public SensorAdapter(Context context, BeaconStorage beaconStorage, int type) {
        this.context=context;
        this.beaconStorage = beaconStorage;
        this.type=type;
    }

    @Override
    public int getCount() {
        return beaconStorage.getBeaconList().size();
    }

    @Override
    public Object getItem(int position) {
        return beaconStorage.getBeaconList().get(position);
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
        b = beaconStorage.getBeaconList().get(position);
        wtfSensorName.setText(b.getDevice_name());
        TextView wtfSensorRoom= (TextView) v.findViewById(R.id.wtf_sensor_room);
        wtfSensorRoom.setText(b.getRoom_name());
        ImageView wtfImage = (ImageView) v.findViewById(R.id.wtf_sensor_image);
        wtfImage.setImageResource(b.getImage());
        return v;
    }
}
