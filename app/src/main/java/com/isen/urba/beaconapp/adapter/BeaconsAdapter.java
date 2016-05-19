package com.isen.urba.beaconapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.isen.urba.beaconapp.R;
import com.isen.urba.beaconapp.pojo.Beacon;

/**
 * Created by romain on 19/05/2016.
 */

public class BeaconsAdapter extends ArrayAdapter<Beacon>{

    private final int beaconItemLayoutRessource;

    public BeaconsAdapter(Context context, int resource) {
        super(context, resource);
        this.beaconItemLayoutRessource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final Beacon beacon = getItem(position);

        viewHolder.name.setText(beacon.getName());
        viewHolder.bluetoothName.setText(beacon.getBluetoothName());
        viewHolder.bluetoothAdress.setText(beacon.getBluetoothAdress());
        viewHolder.rssi.setText(String.valueOf(beacon.getRssi()));
        viewHolder.uuid.setText(beacon.getUUID().toString());

        return view;
    }

    private ViewHolder getViewHolder(View view) {
        final Object tag = view.getTag();
        ViewHolder viewHolder = null;
        if(tag == null || !(tag instanceof ViewHolder)){
            viewHolder = new ViewHolder();

            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.bluetoothName = (TextView) view.findViewById(R.id.beaconBluetoothName);
            viewHolder.bluetoothAdress = (TextView) view.findViewById(R.id.beaconBluetoothAddress);
            viewHolder.rssi = (TextView) view.findViewById(R.id.beaconRSSI);
            viewHolder.uuid = (TextView) view.findViewById(R.id.beaconUUID);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) tag;
        }
        return viewHolder;

    }

    private View getWorkingView(final View convertView){
        View workingView;

        if(convertView == null){
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            workingView = inflater.inflate(beaconItemLayoutRessource, null);
        }else{
            workingView = convertView;
        }
        return workingView;
    }

}
