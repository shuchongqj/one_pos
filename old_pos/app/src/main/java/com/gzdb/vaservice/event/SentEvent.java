package com.gzdb.vaservice.event;

public class SentEvent {

    private byte type;

    public SentEvent(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
