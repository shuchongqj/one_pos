package com.gzdb.mall.bean;

import java.util.List;

/**
 * Created by liubolin on 2017/12/6.
 *  支持现在框架  datas
 */

public class BaseBean<T> {

    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
