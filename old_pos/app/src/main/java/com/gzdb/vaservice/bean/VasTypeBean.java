package com.gzdb.vaservice.bean;

import cc.solart.turbo.ChooseBean;

/**
 * Created by nongyd on 17/5/3.
 */

public class VasTypeBean extends ChooseBean {
    public  String title;
    public String type;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
