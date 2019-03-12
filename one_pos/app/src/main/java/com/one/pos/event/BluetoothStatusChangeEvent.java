package com.one.pos.event;

public class BluetoothStatusChangeEvent {

    int status;
    int code;

    public BluetoothStatusChangeEvent(int status, int code) {
        this.status = status;
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }
}
