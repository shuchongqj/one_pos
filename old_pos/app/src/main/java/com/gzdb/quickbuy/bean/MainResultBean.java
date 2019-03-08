package com.gzdb.quickbuy.bean;

import java.util.List;

/**
 * Created by nongyd on 17/6/2.
 */

public class MainResultBean {
    List<QuickBuyItem> preferentialList;
    List<BuyOutsideBean> externalList;
    List<OneBuyTypeBean> menutypeList;
    private String warehouseId;

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public List<QuickBuyItem> getPreferentialList() {
        return preferentialList;
    }

    public void setPreferentialList(List<QuickBuyItem> preferentialList) {
        this.preferentialList = preferentialList;
    }

    public List<BuyOutsideBean> getExternalList() {
        return externalList;
    }

    public void setExternalList(List<BuyOutsideBean> externalList) {
        this.externalList = externalList;
    }

    public List<OneBuyTypeBean> getMenutypeList() {
        return menutypeList;
    }

    public void setMenutypeList(List<OneBuyTypeBean> menutypeList) {
        this.menutypeList = menutypeList;
    }
}
