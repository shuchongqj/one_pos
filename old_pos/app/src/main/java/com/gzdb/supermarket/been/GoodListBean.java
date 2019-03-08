package com.gzdb.supermarket.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nongyd on 17/5/27.
 */
public class GoodListBean implements Serializable{

    private static final long serialVersionUID = 313939710755555238L;

    private List<GoodBean> datas;

    public List<GoodBean> getDatas() {
        return datas;
    }

    public void setDatas(List<GoodBean> datas) {
        this.datas = datas;
    }

}
