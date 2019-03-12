package com.one.pos.event;

/**
 * @author zhumg
 */
public class UsbStatusChangeEvent {

    int status;
    int code;

    public UsbStatusChangeEvent(int status, int code) {
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
