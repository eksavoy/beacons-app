package com.isen.urba.beaconapp.utils;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.isen.urba.beaconapp.R;
import com.isen.urba.beaconapp.adapter.BeaconsAdapter;
import com.isen.urba.beaconapp.adapter.AddBeacons.AddBeaconsAdapter;
import com.isen.urba.beaconapp.pojo.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by romain on 26/05/2016.
 */
public class ViewUtils {

    public static ListView instantiateViews(Activity activity, BeaconsAdapter adapter) {
        ListView beaconListView;
        beaconListView = (ListView) activity.findViewById(R.id.beaconsList);
        beaconListView.setAdapter(adapter);
        return beaconListView;
    }

    public static BeaconsAdapter instantiateAdapter(Activity activity){
        return new BeaconsAdapter(activity, R.layout.beacon_list_item);
    }

    public static void updateListViewBeacons(Activity activity, final BeaconsAdapter adapter, final List<Beacon> beacons) {
        if(adapter != null && adapter.getCount() > 0){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.clear();
                }
            });
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adapter != null && adapter.getCount() > 0){
                    adapter.clear();
                }
                adapter.addAll(beacons);
            }
        });

    }
}
