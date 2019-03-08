package com.gzdb.supermarket.event;

import com.gzdb.vip.VipUserInfo;

public class VipPayEvent {

    private String code;
    private VipUserInfo vipUserInfo;

    public VipPayEvent(String code, VipUserInfo userInfo) {
        this.code = code;
        this.vipUserInfo = userInfo;
    }

    public VipUserInfo getVipUserInfo() {
        return vipUserInfo;
    }

    public String getCode() {
        return code;
    }
}
