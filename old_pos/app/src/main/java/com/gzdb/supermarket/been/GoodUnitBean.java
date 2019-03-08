package com.gzdb.supermarket.been;

import com.gzdb.supermarket.common.XCDropDownListView;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by nongyd on 17/5/28.
 */

@Entity
public class GoodUnitBean implements XCDropDownListView.XCDropDownItem,Serializable{
    private static final long serialVersionUID = 8471011471068837718L;
    /**
     * id : 100000
     * title : 包
     * status : 0
     */
    @Id(autoincrement = true)
    private Long autoID;

    @Unique
    private String id;
    private String title;
    private int status;

    @Generated(hash = 1498510103)
    public GoodUnitBean(Long autoID, String id, String title, int status) {
        this.autoID = autoID;
        this.id = id;
        this.title = title;
        this.status = status;
    }

    @Generated(hash = 1131240970)
    public GoodUnitBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getXCDropDownItemText() {
        return this.title;
    }

    @Override
    public int getXCDropDownItemType() {
        return 1;
    }

    public Long getAutoID() {
        return this.autoID;
    }

    public void setAutoID(Long autoID) {
        this.autoID = autoID;
    }
}