package com.gzdb.supermarket.been;

import java.io.Serializable;

/**
 * Author: even
 * Date:   2019/3/1
 * Description:
 */
public class BottonItem implements Serializable {
    private String isClick;//判断是否点击
    private String name;


    public BottonItem(String name) {
        this.name = name;
    }

    public String getIsClick() {
        return isClick;
    }

    public void setIsClick(String isClick) {
        this.isClick = isClick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
