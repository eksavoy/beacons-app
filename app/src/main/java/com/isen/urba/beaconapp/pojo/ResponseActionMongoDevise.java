package com.isen.urba.beaconapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by isen on 10/06/2016.
 */
public class ResponseActionMongoDevise {

    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
