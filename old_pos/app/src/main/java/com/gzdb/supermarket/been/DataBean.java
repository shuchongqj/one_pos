package com.gzdb.supermarket.been;

import java.util.List;

/**
 * Created by liubolin on 2017/12/6.
 *  支持现在框架  datas
 */

public class DataBean<T> {

    private List<T> datas;

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
