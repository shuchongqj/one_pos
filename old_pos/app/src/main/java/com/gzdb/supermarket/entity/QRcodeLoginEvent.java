package com.gzdb.supermarket.entity;

import com.gzdb.supermarket.been.UserBean;

public class QRcodeLoginEvent {
    private UserBean userBean;

    public QRcodeLoginEvent(UserBean userBean) {
        this.userBean = userBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
