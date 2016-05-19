package com.isen.urba.beaconapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.isen.urba.beaconapp.adapter.BeaconsAdapter;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    ListView beaconListView;
    BeaconsAdapter adapter = null;

    List<com.isen.urba.beaconapp.pojo.Beacon> beacons = null;

    BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestToTurnOnBluetooth();

        instantiateViews();

        initiatBeaconManager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    private void instantiateViews () {
        beaconListView = (ListView) findViewById(R.id.beaconsList);
        adapter = new BeaconsAdapter(this, R.layout.beacon_list_item);
        beaconListView.setAdapter(adapter);
    }

    private void initiatBeaconManager() {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    private void requestToTurnOnBluetooth() {
        // Request to put on the Bluetooth :
        int REQUEST_ENABLE_BT=1;
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            return;
        }
        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beaconsNotif, Region region) {
                if(beaconsNotif.size() > 0){
                    beacons = new LinkedList<com.isen.urba.beaconapp.pojo.Beacon>();
                    for(Beacon beacon : beaconsNotif){
                        beacons.add(new com.isen.urba.beaconapp.pojo.Beacon("Test", beacon.getBluetoothName(), beacon.getBluetoothAddress(), beacon.getId1(), beacon.getRssi()));
                    }
                    updateListViewBeacons(beacons);
                }
            }
        });
        try{
            beaconManager.startRangingBeaconsInRegion(new Region("Urbawood", null, null, null));
        }catch (RemoteException e){
            Log.e("RemoteException", e.toString());
        }
    }

    private void updateListViewBeacons(final List<com.isen.urba.beaconapp.pojo.Beacon> beacons) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.addAll(beacons);
            }
        });

    }
}
