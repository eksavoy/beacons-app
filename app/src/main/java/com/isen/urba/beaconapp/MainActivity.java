package com.isen.urba.beaconapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.isen.urba.beaconapp.adapter.BeaconsAdapter;
import com.isen.urba.beaconapp.pojo.Device;
import com.isen.urba.beaconapp.pojo.MongoBeacon;
import com.isen.urba.beaconapp.pojo.MongoDevise;
import com.isen.urba.beaconapp.pojo.ResponseActionMongoDevise;
import com.isen.urba.beaconapp.service.DeviseService;
import com.isen.urba.beaconapp.utils.BeaconsUtils;
import com.isen.urba.beaconapp.utils.SharedPreferencesUtils;
import com.isen.urba.beaconapp.utils.ViewUtils;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    ListView beaconListView;
    BeaconsAdapter adapter = null;

    List<com.isen.urba.beaconapp.pojo.Beacon> beacons = null;
    List<com.isen.urba.beaconapp.pojo.Beacon> beaconsAuthorized;
    Collection<Beacon> beaconsFind = null;

    BeaconManager beaconManager;

    SharedPreferences sharedPreferences;

    Activity currentActivity;

    Device device = new Device();

    public String API = "http://10.134.15.12:4000/";

    Retrofit retrofit;

    DeviseService service;

    String oldPosition = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentActivity = this;

        beaconsAuthorized = new LinkedList<>();

        requestToTurnOnBluetooth();

        adapter = ViewUtils.instantiateAdapter(this);
        beaconListView =ViewUtils.instantiateViews(this, adapter);

        initiatBeaconManager();

        this.sharedPreferences = SharedPreferencesUtils.initiatSharedPref(getBaseContext());

        beaconsAuthorized = SharedPreferencesUtils.getBeaconsFromPreferences(this.sharedPreferences);
        device = SharedPreferencesUtils.getDeviceFromPreferences(this.sharedPreferences);
        if(!SharedPreferencesUtils.getBackAddressFromPreferences(this.sharedPreferences).isEmpty()){
            API = SharedPreferencesUtils.getBackAddressFromPreferences(this.sharedPreferences);
        }
        if(device == null || API.isEmpty()){
            device =  new Device();
            LinearLayout li = new LinearLayout(findViewById(R.id.activity_main).getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            li.setOrientation(LinearLayout.VERTICAL);
            li.setLayoutParams(params);

            final EditText ed = new EditText(findViewById(R.id.activity_main).getContext());
            TextView edLable = new TextView(findViewById(R.id.activity_main).getContext());
            edLable.setText("Add name");

            final EditText backAddr = new EditText(findViewById(R.id.activity_main).getContext());
            backAddr.setText(API);
            TextView bacAddrLabel = new TextView(findViewById(R.id.activity_main).getContext());
            bacAddrLabel.setText("Add back address");

            li.addView(edLable);
            li.addView(ed, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            li.addView(bacAddrLabel);
            li.addView(backAddr, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            AlertDialog.Builder alertdialog = new AlertDialog.Builder(findViewById(R.id.activity_main).getContext());
            alertdialog.setTitle("Add unknow information");
            alertdialog.setView(li);
            alertdialog
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(device == null){
                                device = new Device();
                            }
                            device.setDeviceName(ed.getText().toString());
                            API = backAddr.getText().toString();
                            SharedPreferencesUtils.saveDeviceInPreferences(sharedPreferences, device);
                            SharedPreferencesUtils.saveBackAddressInPreferences(sharedPreferences, API);
                            setRestService();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertdialog.create().show();
        }else{
            this.setRestService();
        }


    }

    private void setRestService(){
        retrofit = new Retrofit.Builder().baseUrl(API).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(DeviseService.class);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
        SharedPreferencesUtils.saveInPreferences(this.sharedPreferences, beaconsAuthorized);
        SharedPreferencesUtils.saveDeviceInPreferences(sharedPreferences, device);
        SharedPreferencesUtils.saveBackAddressInPreferences(this.sharedPreferences, API);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        beaconsAuthorized = SharedPreferencesUtils.getBeaconsFromPreferences(this.sharedPreferences);
        device = SharedPreferencesUtils.getDeviceFromPreferences(this.sharedPreferences);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idSelect = item.getItemId();

        switch (idSelect){
            case R.id.action_add:

                if(device != null || device.getDeviceName() != null){
                    if(beaconsFind != null){
                        Intent intent = new Intent(this, AddBeaconActivity.class);
                        ArrayList<Beacon> beaconsIntent = new ArrayList<>(beaconsFind);
                        intent.putParcelableArrayListExtra("Beacons", beaconsIntent);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getBaseContext(),"Not beacons find Try again", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getBaseContext(),"Choose device name", Toast.LENGTH_LONG).show();
                }
                return true;
            case  R.id.action_less:
                if(device != null || device.getDeviceName() != null){
                    SharedPreferencesUtils.deleteSharedPrefs(sharedPreferences);
                    this.beaconsAuthorized = new LinkedList<>();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.clear();
                        }
                    });
                }else{
                    Toast.makeText(getBaseContext(), "Choose device name", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_name:
                LinearLayout li = new LinearLayout(findViewById(R.id.activity_main).getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                li.setOrientation(LinearLayout.VERTICAL);
                li.setLayoutParams(params);

                final EditText ed = new EditText(findViewById(R.id.activity_main).getContext());
                if(device != null){
                    ed.setText(device.getDeviceName());
                }
                TextView edLable = new TextView(findViewById(R.id.activity_main).getContext());
                edLable.setText("Add name");

                final EditText backAddr = new EditText(findViewById(R.id.activity_main).getContext());
                backAddr.setText(API);
                TextView bacAddrLabel = new TextView(findViewById(R.id.activity_main).getContext());
                bacAddrLabel.setText("Add back address");

                li.addView(edLable);
                li.addView(ed, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                li.addView(bacAddrLabel);
                li.addView(backAddr, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(findViewById(R.id.activity_main).getContext());
                alertdialog.setTitle("Add Information");
                alertdialog.setView(li);
                alertdialog
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                device.setDeviceName(ed.getText().toString());
                                API = backAddr.getText().toString();
                                SharedPreferencesUtils.saveDeviceInPreferences(sharedPreferences, device);
                                SharedPreferencesUtils.saveBackAddressInPreferences(sharedPreferences, API);
                                setRestService();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertdialog.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


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
                        com.isen.urba.beaconapp.pojo.Beacon beaconTmp = new com.isen.urba.beaconapp.pojo.Beacon(beacon.getBluetoothName(), beacon.getBluetoothAddress(), -beacon.getRssi());
                        if(beaconsAuthorized.contains(beaconTmp)){
                            com.isen.urba.beaconapp.pojo.Beacon beaconToAdd= BeaconsUtils.searchById(beaconsAuthorized, beacon.getBluetoothName());
                            beaconToAdd.setRssi(-beacon.getRssi());
                            beacons.add(beaconToAdd);
                        }
                    }
                    Collections.sort(beacons);
                    try {
                        if(oldPosition == null){
                            if(device != null && device.getDeviceName() != null){
                                Call<List<MongoDevise>> find = service.devise(device.getDeviceID());
                                find.enqueue(new Callback<List<MongoDevise>>() {
                                    @Override
                                    public void onResponse(Call<List<MongoDevise>> call, Response<List<MongoDevise>> response) {
                                        if(response.body().size() == 0){
                                            Call<ResponseActionMongoDevise> insert = service.insertDevise(new MongoDevise(device.getDeviceName(), new MongoBeacon( beacons.get(0).getName(),  beacons.get(0).getBluetoothName())));
                                            insert.enqueue(new Callback<ResponseActionMongoDevise>() {
                                                @Override
                                                public void onResponse(Call<ResponseActionMongoDevise> call, Response<ResponseActionMongoDevise> response) {
                                                    oldPosition = beacons.get(0).getName();
                                                    Log.i("Retrofit", "Devise insert");
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseActionMongoDevise> call, Throwable t) {
                                                    Log.e("Retrofit", t.toString());
                                                }
                                            });
                                        }else{
                                            Call<ResponseActionMongoDevise> update = service.updateDevise(device.getDeviceID(), new MongoDevise(device.getDeviceName(), new MongoBeacon( beacons.get(0).getName(),  beacons.get(0).getBluetoothName())));
                                            update.enqueue(new Callback<ResponseActionMongoDevise>() {
                                                @Override
                                                public void onResponse(Call<ResponseActionMongoDevise> call, Response<ResponseActionMongoDevise> response) {
                                                    oldPosition = beacons.get(0).getName();
                                                    Log.i("Retrofit", "Devise update");
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseActionMongoDevise> call, Throwable t) {
                                                    Log.e("Retrofit", t.toString());
                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<List<MongoDevise>> call, Throwable t) {
                                        Log.e("Retrofit", t.toString());
                                    }
                                });
                            }
                            ViewUtils.updateListViewBeacons(currentActivity, adapter, beacons);
                        }else if(oldPosition != null && !oldPosition.equals(beacons.get(0).getName())){
                            if(device != null && device.getDeviceName() != null){
                                Call<List<MongoDevise>> find = service.devise(device.getDeviceID());
                                find.enqueue(new Callback<List<MongoDevise>>() {
                                    @Override
                                    public void onResponse(Call<List<MongoDevise>> call, Response<List<MongoDevise>> response) {
                                        if(response.body().size() == 0){
                                            Call<ResponseActionMongoDevise> insert = service.insertDevise(new MongoDevise(device.getDeviceName(), new MongoBeacon( beacons.get(0).getName(),  beacons.get(0).getBluetoothName())));
                                            insert.enqueue(new Callback<ResponseActionMongoDevise>() {
                                                @Override
                                                public void onResponse(Call<ResponseActionMongoDevise> call, Response<ResponseActionMongoDevise> response) {
                                                    oldPosition = beacons.get(0).getName();
                                                    Log.i("Retrofit", "Devise insert");
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseActionMongoDevise> call, Throwable t) {
                                                    Log.e("Retrofit", t.toString());
                                                }
                                            });
                                        }else{
                                            Call<ResponseActionMongoDevise> update = service.updateDevise(device.getDeviceID(), new MongoDevise(device.getDeviceName(), new MongoBeacon( beacons.get(0).getName(),  beacons.get(0).getBluetoothName())));
                                            update.enqueue(new Callback<ResponseActionMongoDevise>() {
                                                @Override
                                                public void onResponse(Call<ResponseActionMongoDevise> call, Response<ResponseActionMongoDevise> response) {
                                                    oldPosition = beacons.get(0).getName();
                                                    Log.i("Retrofit", "Devise update");
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseActionMongoDevise> call, Throwable t) {
                                                    Log.e("Retrofit", t.toString());
                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<List<MongoDevise>> call, Throwable t) {
                                        Log.e("Retrofit", t.toString());
                                    }
                                });
                            }
                            ViewUtils.updateListViewBeacons(currentActivity, adapter, beacons);
                        }
                    }catch (Exception e){
                        Log.e("General error", e.toString());
                    }
                }
            }
        });
        try{
            beaconManager.startRangingBeaconsInRegion(new Region("Urbawood", null, null, null));
        }catch (RemoteException e){
            Log.e("RemoteException", e.toString());
        }
    }


}
