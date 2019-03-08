package com.gzdb.printer;

public class DeviceEntity {

    //硬件类型，0=蓝牙，1=USB
    private int type;
    private String name;
    private String mac;
    private String info;
    //目标三件源
    private Object srcDevice;
    //0默认，未联接
    //1连接中
    //10已连接
    private int status;

    public DeviceEntity() {
    }

    public DeviceEntity(String name, String mac) {
        this.name = name;
        this.mac = mac;
    }

    public DeviceEntity(int type, String name, String mac, String info) {
        this.name = name;
        this.mac = mac;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getSrcDevice() {
        return srcDevice;
    }

    public void setSrcDevice(Object srcDevice) {
        this.srcDevice = srcDevice;
    }
}
