package com.isen.urba.beaconapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isen.urba.beaconapp.R;
import com.isen.urba.beaconapp.pojo.Beacon;
import com.isen.urba.beaconapp.pojo.Device;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by romain on 26/05/2016.
 */
public class SharedPreferencesUtils {

    public static Gson gson = new Gson();
    public  static Type BeaconsType = new TypeToken<LinkedList<Beacon>>() {}.getType();
    public static Type deviceType = new TypeToken<Device>() {}.getType();

    public static SharedPreferences initiatSharedPref(Context context) {
        return context.getSharedPreferences(String.valueOf(R.string.shared_pref), Context.MODE_PRIVATE);
    }

    public static void saveInPreferences(SharedPreferences sharedPreferences, List<Beacon> beacons){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        String json = gson.toJson(beacons, BeaconsType);

        editor.putString(String.valueOf(R.string.beacoon_key), json);
        editor.commit();
    }

    public static List<Beacon> getBeaconsFromPreferences(SharedPreferences sharedPreferences){
        List<Beacon> beacons = new LinkedList<>();
        if (sharedPreferences.contains(String.valueOf(R.string.beacoon_key))){
            String beaconsList = sharedPreferences.getString(String.valueOf(R.string.beacoon_key), String.valueOf(R.string.beacoon_key));
            beacons = gson.fromJson(beaconsList, BeaconsType);
            if(beacons == null){
                beacons = new LinkedList<>();
            }
        }
        return beacons;

    }

    public static void deleteSharedPrefs(SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(String.valueOf(R.string.beacoon_key));
        editor.commit();
    }

    public static Device getDeviceFromPreferences(SharedPreferences sharedPreferences) {
        Device device = new Device();
        if(sharedPreferences.contains(String.valueOf(R.string.device_key))){
            device.setDeviceName(sharedPreferences.getString(String.valueOf(R.string.device_key), null));
        }
        if(device.getDeviceName() == null){
            device = null;
        }

        return device;
    }

    public static void saveDeviceInPreferences(SharedPreferences sharedPreferences, Device device){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(String.valueOf(R.string.device_key), device.getDeviceName());
        editor.commit();
    }
}
