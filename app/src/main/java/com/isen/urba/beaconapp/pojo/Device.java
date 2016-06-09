package com.isen.urba.beaconapp.pojo;

import android.os.Build;

/**
 * Created by romain on 03/06/2016.
 */
public class Device {

    private String deviceID = Build.ID;
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
