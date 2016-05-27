package com.isen.urba.beaconapp.utils;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;
import java.util.List;

/**
 * Created by romain on 26/05/2016.
 */
public class BeaconsUtils {

    public static Beacon searchById(Collection<Beacon> beacons, String id){
        for (Beacon beacon : beacons){
            if(beacon.getBluetoothName().equals(id)){
                return beacon;
            }
        }
        return null;
    }
    public static com.isen.urba.beaconapp.pojo.Beacon searchById(List<com.isen.urba.beaconapp.pojo.Beacon> beacons, String id){
        for (com.isen.urba.beaconapp.pojo.Beacon beacon : beacons){
            if(beacon.getBluetoothName().equals(id)){
                return beacon;
            }
        }
        return null;
    }
}
