package com.isen.urba.beaconapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isen.urba.beaconapp.adapter.BeaconsAdapter;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    ListView beaconListView;
    BeaconsAdapter adapter = null;

    List<com.isen.urba.beaconapp.pojo.Beacon> beacons = null;
    List<com.isen.urba.beaconapp.pojo.Beacon> beaconsAuthorized = new LinkedList<>();
    Collection<Beacon> beaconsFind = null;

    BeaconManager beaconManager;

    SharedPreferences sharedPreferences;

    Boolean popupOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestToTurnOnBluetooth();

        instantiateViews();

        initiatBeaconManager();

        initiatSharedPref();

        getFromPreferences();
    }

    private void initiatSharedPref() {
        this.sharedPreferences = getSharedPreferences(String.valueOf(R.string.shared_pref), MODE_PRIVATE);
    }

    private void saveInPreferences(List<com.isen.urba.beaconapp.pojo.Beacon> beacons){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedList<com.isen.urba.beaconapp.pojo.Beacon>>() {}.getType();
        String json = gson.toJson(beacons, type);

        editor.putString(String.valueOf(R.string.beacoon_key), json);
        editor.commit();
    }

    private void getFromPreferences(){
        if (this.sharedPreferences.contains(String.valueOf(R.string.beacoon_key))){
            Gson gson = new Gson();
            Type type = new TypeToken<LinkedList<com.isen.urba.beaconapp.pojo.Beacon>>() {}.getType();

            String beaconsList = this.sharedPreferences.getString(String.valueOf(R.string.beacoon_key), String.valueOf(R.string.beacoon_key));

            this.beaconsAuthorized = gson.fromJson(beaconsList, type);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
        saveInPreferences(beacons);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idSelect = item.getItemId();

        switch (idSelect){
            case R.id.action_add:
                if(!popupOpen){
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View popup = layoutInflater.inflate(R.layout.popup, null);
                    final PopupWindow popupWindow = new PopupWindow(
                            popup,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    Button add = (Button) popup.findViewById(R.id.btn_popup_add);
                    add.setOnClickListener(new Button.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            popupOpen = false;
                        }
                    });
                    popupWindow.setFocusable(true);
                    popupWindow.update();
                    popupWindow.showAsDropDown(findViewById(R.id.action_add));
                    popupOpen = true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


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
                    beaconsFind = beaconsNotif;
                    beacons = new LinkedList<com.isen.urba.beaconapp.pojo.Beacon>();
                    for(Beacon beacon : beaconsNotif){
                        com.isen.urba.beaconapp.pojo.Beacon beaconTmp = new com.isen.urba.beaconapp.pojo.Beacon("Test", beacon.getBluetoothName(), beacon.getBluetoothAddress(), beacon.getRssi());
                        if(beaconsAuthorized.contains(beaconTmp)){
                            beacons.add(beaconTmp);
                        }
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
        if(adapter != null && adapter.getCount() > 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.clear();
                }
            });
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.addAll(beacons);
            }
        });

    }
}
