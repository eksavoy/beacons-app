package com.isen.urba.beaconapp.pojo;

import org.altbeacon.beacon.Identifier;

/**
 * Created by romain on 19/05/2016.
 */

public class Beacon implements Comparable<Beacon>{

    private String name;
    private String bluetoothName;
    private String bluetoothAdress;
    private Identifier UUID;
    private int rssi;

    public Beacon() {
        this.name = "Inconnu";
        this.bluetoothAdress = "0:0:0:0:0:0";
        this.bluetoothName = "BEACON";
        this.rssi = 0;
        this.UUID = null;
    }

    public Beacon(String name, String bluetoothName, String bluetoothAdress, Identifier UUID, int rssi) {
        this.name = name;
        this.bluetoothName = bluetoothName;
        this.bluetoothAdress = bluetoothAdress;
        this.UUID = UUID;
        this.rssi = rssi;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public String getBluetoothAdress() {
        return bluetoothAdress;
    }

    public void setBluetoothAdress(String bluetoothAdress) {
        this.bluetoothAdress = bluetoothAdress;
    }

    public Identifier getUUID() {
        return UUID;
    }

    public void setUUID(Identifier UUID) {
        this.UUID = UUID;
    }

    @Override
    public int compareTo(Beacon another) {
        return this.getName().compareToIgnoreCase(another.getName());
    }
}
