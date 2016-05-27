package com.isen.urba.beaconapp.adapter.AddBeacons;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.isen.urba.beaconapp.R;
import com.isen.urba.beaconapp.adapter.ViewHolder;
import com.isen.urba.beaconapp.listeners.AddBeaconBtnListeners;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

/**
 * Created by romain on 26/05/2016.
 */
public class AddBeaconsAdapter extends RecyclerView.Adapter<AddBeaconViewHolder> {

    ArrayList<Beacon> beacons;
    Context context;

    public AddBeaconsAdapter(ArrayList<Beacon> beacons) {
        this.beacons = beacons;
    }

    @Override
    public AddBeaconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.add_beacon_list_item, parent, false);
        AddBeaconViewHolder viewHolder = new AddBeaconViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddBeaconViewHolder holder, int position) {
        Beacon beacon = beacons.get(position);
        holder.bluetoothName.setText(beacon.getBluetoothName());
        holder.bluetoothAdress.setText(beacon.getBluetoothAddress());
        Button btn = new Button(context);
        btn.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setText("ADD");
        btn.setId(position);
        btn.setOnClickListener(new AddBeaconBtnListeners(beacons));
        holder.ln.addView(btn);
    }

    @Override
    public int getItemCount() {
        return beacons.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
