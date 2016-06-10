package com.isen.urba.beaconapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by isen on 09/06/2016.
 */
public class MongoBeacon {

    @SerializedName("beaconName")
    private String beaconName;
    @SerializedName("beaconID")
    private String beaconId;

    public MongoBeacon(String beaconName, String beaconId) {
        this.beaconName = beaconName;
        this.beaconId = beaconId;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }
}
