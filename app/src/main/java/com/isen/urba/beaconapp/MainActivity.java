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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    ListView beaconListView;
    BeaconsAdapter adapter = null;

    List<com.isen.urba.beaconapp.pojo.Beacon> beacons = null;
    List<com.isen.urba.beaconapp.pojo.Beacon> beaconsAuthorized;
    Collection<Beacon> beaconsFind = null;

    BeaconManager beaconManager;

    SharedPreferences sharedPreferences;

    Boolean popupOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconsAuthorized = new LinkedList<>();

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
        editor.clear();
        editor.commit();
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedList<com.isen.urba.beaconapp.pojo.Beacon>>() {}.getType();
        String json = gson.toJson(beaconsAuthorized, type);

        editor.putString(String.valueOf(R.string.beacoon_key), json);
        editor.commit();
    }

    private void getFromPreferences(){
        if (this.sharedPreferences.contains(String.valueOf(R.string.beacoon_key))){
            Gson gson = new Gson();
            Type type = new TypeToken<LinkedList<com.isen.urba.beaconapp.pojo.Beacon>>() {}.getType();

            String beaconsList = this.sharedPreferences.getString(String.valueOf(R.string.beacoon_key), String.valueOf(R.string.beacoon_key));

            this.beaconsAuthorized = gson.fromJson(beaconsList, type);
            if(this.beaconsAuthorized == null){
                this.beaconsAuthorized = new LinkedList<>();
            }
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

                            String id = ((EditText) popup.findViewById(R.id.edt_id)).getText().toString();
                            String name = ((EditText) popup.findViewById(R.id.edt_name)).getText().toString();
                            if(!id.isEmpty() && !name.isEmpty()){
                                Beacon finded = searchById(beaconsFind, id);
                                com.isen.urba.beaconapp.pojo.Beacon alreadyAdded = searchById(beaconsAuthorized, id);
                                if((finded != null) && (alreadyAdded == null)){
                                    beaconsAuthorized.add(new com.isen.urba.beaconapp.pojo.Beacon(name, finded.getBluetoothName(), finded.getBluetoothAddress(), finded.getRssi()));
                                    popupWindow.dismiss();
                                    popupOpen = false;
                                }else{
                                    Toast.makeText(getApplicationContext(), "Beacon not found", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Id or Name empty", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Button close = (Button) popup.findViewById(R.id.btn_popup_close);
                    close.setOnClickListener(new Button.OnClickListener(){
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

    private Beacon searchById(Collection<Beacon> beacons, String id){
        for (Beacon beacon : beacons){
            if(beacon.getBluetoothName().equals(id)){
                return beacon;
            }
        }
        return null;
    }
    private com.isen.urba.beaconapp.pojo.Beacon searchById(List<com.isen.urba.beaconapp.pojo.Beacon> beacons, String id){
        for (com.isen.urba.beaconapp.pojo.Beacon beacon : beacons){
            if(beacon.getBluetoothName().equals(id)){
                return beacon;
            }
        }
        return null;
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
                beaconsFind = beaconsNotif;
                if((beaconsNotif != null && beaconsAuthorized != null) && (beaconsNotif.size() > 0 && beaconsAuthorized.size() > 0)){
                    beacons = new LinkedList<com.isen.urba.beaconapp.pojo.Beacon>();
                    for(Beacon beacon : beaconsNotif){
                        com.isen.urba.beaconapp.pojo.Beacon beaconTmp = new com.isen.urba.beaconapp.pojo.Beacon(beacon.getBluetoothName(), beacon.getBluetoothAddress(), beacon.getRssi());
                        if(beaconsAuthorized.contains(beaconTmp)){
                            com.isen.urba.beaconapp.pojo.Beacon beaconToAdd= searchById(beaconsAuthorized, beacon.getBluetoothName());
                            beaconToAdd.setRssi(beacon.getRssi());
                            beacons.add(beaconToAdd);
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

        Collections.sort(beacons);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.addAll(beacons);
            }
        });

    }
}
