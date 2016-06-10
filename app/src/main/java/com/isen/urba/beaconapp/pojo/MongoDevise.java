package com.isen.urba.beaconapp.pojo;

/**
 * Created by isen on 09/06/2016.
 */
public class MongoDevise extends Device{

    private MongoBeacon beaconConnected;

    public MongoDevise(String deviceName, MongoBeacon beaconConnected) {
        super(deviceName);
        this.beaconConnected = beaconConnected;
    }

    public MongoBeacon getBeaconConnected() {
        return beaconConnected;
    }

    public void setBeaconConnected(MongoBeacon beaconConnected) {
        this.beaconConnected = beaconConnected;
    }
}
