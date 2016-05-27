package com.isen.urba.beaconapp.listeners;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.isen.urba.beaconapp.utils.SharedPreferencesUtils;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romain on 27/05/2016.
 */
public class AddNameToBeaconListener implements DialogInterface.OnClickListener {
    Beacon beacon;
    EditText userInput;
    Context context;

    public AddNameToBeaconListener(Context context, Beacon beacon, EditText userInput) {
        this.beacon = beacon;
        this.userInput = userInput;
        this.context = context;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        SharedPreferences sharedPreferences = SharedPreferencesUtils.initiatSharedPref(context);
        List<com.isen.urba.beaconapp.pojo.Beacon> beaconsAuthorized = SharedPreferencesUtils.getFromPreferences(sharedPreferences);
        beaconsAuthorized.add(new com.isen.urba.beaconapp.pojo.Beacon(userInput.getText().toString(), beacon.getBluetoothName(),beacon.getBluetoothAddress(),beacon.getRssi()));
        SharedPreferencesUtils.saveInPreferences(sharedPreferences, beaconsAuthorized);
    }
}
