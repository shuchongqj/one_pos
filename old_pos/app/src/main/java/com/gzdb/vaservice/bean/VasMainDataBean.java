package com.gzdb.vaservice.bean;

import java.util.List;

/**
 * Created by nongyd on 17/5/10.
 */

public class VasMainDataBean {
    List<VasPareBean> menuList;
    VASPhoneBean phoneInfo;

    public List<VasPareBean> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<VasPareBean> menuList) {
        this.menuList = menuList;
    }

    public VASPhoneBean getPhoneInfo() {
        return phoneInfo;
    }

    public void setPhoneInfo(VASPhoneBean phoneInfo) {
        this.phoneInfo = phoneInfo;
    }
}
