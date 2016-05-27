package com.isen.urba.beaconapp.adapter.AddBeacons;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.isen.urba.beaconapp.R;

/**
 * Created by romain on 26/05/2016.
 */
public class AddBeaconViewHolder extends RecyclerView.ViewHolder{

    public TextView bluetoothAdress;
    public TextView bluetoothName;
    CardView cv;
    LinearLayout ln;

    public AddBeaconViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.add_beacon_cardView);
        ln = (LinearLayout) itemView.findViewById(R.id.layout_btn_add_beacon);
        bluetoothAdress = (TextView)itemView.findViewById(R.id.add_beacon_bluetooth_address);
        bluetoothName = (TextView) itemView.findViewById(R.id.add_beacon_bluetooth_name);
    }
}
