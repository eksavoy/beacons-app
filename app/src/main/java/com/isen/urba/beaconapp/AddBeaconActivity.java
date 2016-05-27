package com.isen.urba.beaconapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.isen.urba.beaconapp.adapter.AddBeacons.AddBeaconsAdapter;
import com.isen.urba.beaconapp.utils.BeaconsUtils;
import com.isen.urba.beaconapp.utils.SharedPreferencesUtils;
import com.isen.urba.beaconapp.utils.ViewUtils;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AddBeaconActivity extends AppCompatActivity {

    RecyclerView beaconsList;
    AddBeaconsAdapter adapter;

    ArrayList<Beacon> beacons;
    List<com.isen.urba.beaconapp.pojo.Beacon> beaconsAuthorized = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beacon);

        beacons = getIntent().getParcelableArrayListExtra("Beacons");

        SharedPreferences sharedPreferences = SharedPreferencesUtils.initiatSharedPref(getBaseContext());
        beaconsAuthorized = SharedPreferencesUtils.getFromPreferences(sharedPreferences);

        beacons = this.removeBeaconAlreadyAdded(beaconsAuthorized, beacons);

        beaconsList = (RecyclerView) findViewById(R.id.add_beacon_recycleView);
        beaconsList.setHasFixedSize(true);
        LinearLayoutManager ln = new LinearLayoutManager(getApplicationContext());
        ln.setOrientation(LinearLayoutManager.VERTICAL);
        beaconsList.setLayoutManager(ln);
        adapter = new AddBeaconsAdapter(beacons);
        beaconsList.setAdapter(adapter);

    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }

    private ArrayList<Beacon> removeBeaconAlreadyAdded(List<com.isen.urba.beaconapp.pojo.Beacon> beaconsAuthorized, ArrayList<Beacon> beacons){
        ArrayList<Beacon> beaconsReturns = new ArrayList<>(beacons);
        for(com.isen.urba.beaconapp.pojo.Beacon beacon : beaconsAuthorized){
            Beacon beaconFind = BeaconsUtils.searchById(beacons, beacon.getBluetoothName());
            if(beaconFind != null){
                beaconsReturns.remove(beaconFind);
            }
        }
        return  beaconsReturns;
    }
}
