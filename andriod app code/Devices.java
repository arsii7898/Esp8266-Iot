package com.example.muhammad.homeautomationupdatedsystem;

public class Devices {
    private int _id;
    private String deviceName;
    private String deviceIp;
    private String devicepin;
    private  String maindeviceip;
    private  String deviceStatus;



    public Devices(){

    }
    public Devices(String deviceName,String deviceIp,String devicepin,String maindeviceip,String deviceStatus ) {
        this.deviceName = deviceName;
        this.deviceIp=deviceIp;
        this.devicepin=devicepin;
        this.maindeviceip=maindeviceip;
        this.deviceStatus =deviceStatus;


    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDevicePin() {
        return devicepin;
    }

    public void setDevicePin(String devicepin) {
        this.devicepin = devicepin;
    }

    public String getMaindeviceIp() {
        return maindeviceip;
    }

    public void setMainDeviceIp(String mainDeviceIp) {
        this.maindeviceip = mainDeviceIp;
    }

    public String getDeviceStatus()
    {
        return deviceStatus;
    }
    public void setDeviceStatus(String devicestatus)
    {
        this.deviceStatus =devicestatus;
    }



}
