package com.gzdb.vaservice.event;

public class UpdateCpuLoadEvent {
    private String voucherNo;

    public UpdateCpuLoadEvent(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
}
