package com.isen.urba.beaconapp.listeners;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.isen.urba.beaconapp.AddBeaconActivity;
import com.isen.urba.beaconapp.R;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

/**
 * Created by romain on 27/05/2016.
 */
public class AddBeaconBtnListeners implements View.OnClickListener {

    ArrayList<Beacon> beacons;

    public AddBeaconBtnListeners(ArrayList<Beacon> beacons) {
        this.beacons = beacons;
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button){
            Button btn = (Button) v;
            Beacon beacon = beacons.get(btn.getId());

            LinearLayout li = new LinearLayout(v.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            li.setOrientation(LinearLayout.VERTICAL);
            li.setLayoutParams(params);

            EditText ed = new EditText(v.getContext());
            li.addView(ed, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            AlertDialog.Builder alertdialog = new AlertDialog.Builder(v.getContext());
            alertdialog.setTitle("Add Beacons");
            alertdialog.setView(li);
            alertdialog
                    .setPositiveButton("OK", new AddNameToBeaconListener(v.getContext(),beacon, ed))
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertdialog.create().show();
        }
    }
}
