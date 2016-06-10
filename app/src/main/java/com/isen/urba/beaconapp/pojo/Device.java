package com.isen.urba.beaconapp.pojo;

import android.os.Build;

import com.google.gson.annotations.SerializedName;

/**
 * Created by romain on 03/06/2016.
 */
public class Device {

    @SerializedName("deviseID")
    private String deviceID = Build.ID;
    @SerializedName("deviseName")
    private String deviceName = null;

    public Device(String deviceName) {
        this.deviceName = deviceName;
    }

    public Device() {
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceID() {
        return deviceID;
    }
}
