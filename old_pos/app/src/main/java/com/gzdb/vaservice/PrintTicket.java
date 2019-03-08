package com.gzdb.vaservice;

import com.gzdb.vaservice.bean.YctRecordDetail;

public class PrintTicket {

    private int type;
    private YctRecordDetail yctRecordDetail;

    public PrintTicket(YctRecordDetail yctRecordDetail, int type) {
        this.yctRecordDetail = yctRecordDetail;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public YctRecordDetail getYctRecordDetail() {
        return yctRecordDetail;
    }

    public void setYctRecordDetail(YctRecordDetail yctRecordDetail) {
        this.yctRecordDetail = yctRecordDetail;
    }
}
