package com.isen.urba.beaconapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isen.urba.beaconapp.R;
import com.isen.urba.beaconapp.pojo.Beacon;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by romain on 26/05/2016.
 */
public class SharedPreferencesUtils {

    public static Gson gson = new Gson();
    public  static Type type = new TypeToken<LinkedList<Beacon>>() {}.getType();

    public static SharedPreferences initiatSharedPref(Context context) {
        return context.getSharedPreferences(String.valueOf(R.string.shared_pref), Context.MODE_PRIVATE);
    }

    public static void saveInPreferences(SharedPreferences sharedPreferences, List<Beacon> beacons){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        String json = gson.toJson(beacons, type);

        editor.putString(String.valueOf(R.string.beacoon_key), json);
        editor.commit();
    }

    public static List<Beacon> getFromPreferences(SharedPreferences sharedPreferences){
        List<Beacon> beacons = new LinkedList<>();
        if (sharedPreferences.contains(String.valueOf(R.string.beacoon_key))){
            String beaconsList = sharedPreferences.getString(String.valueOf(R.string.beacoon_key), String.valueOf(R.string.beacoon_key));
            beacons = gson.fromJson(beaconsList, type);
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

}
