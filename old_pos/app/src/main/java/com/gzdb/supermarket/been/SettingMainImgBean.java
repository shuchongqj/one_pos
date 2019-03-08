package com.gzdb.supermarket.been;

import cc.solart.turbo.ChooseBean;

/**
 * Created by nongyd on 17/9/2.
 */

public class SettingMainImgBean extends ChooseBean {
    private String imgUrl;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
