package com.gzdb.supermarket.been;

import com.gzdb.supermarket.common.XCDropDownListView;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import cc.solart.turbo.ChooseBean;

/**
 * Created by nongyd on 17/5/28.
 */
@Entity
public class GoodTypesBean extends ChooseBean implements XCDropDownListView.XCDropDownItem,Serializable{


    private static final long serialVersionUID = -1782119124375525495L;
    /**
     * id : 11000
     * title : 零食糖果
     * type_count : 0
     */
    @Id(autoincrement = true)
    private Long autoId;

    private String id;
    private String title;
    private int typeCount;
    private String itemTypeId;


    @Generated(hash = 200342167)
    public GoodTypesBean(Long autoId, String id, String title, int typeCount, String itemTypeId) {
        this.autoId = autoId;
        this.id = id;
        this.title = title;
        this.typeCount = typeCount;
        this.itemTypeId = itemTypeId;
    }

    @Generated(hash = 2049574678)
    public GoodTypesBean() {
    }


    public String getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
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

    public int getTypeCount() {
        return typeCount;
    }

    public void setTypeCount(int type_count) {
        this.typeCount = type_count;
    }

    @Override
    public String getXCDropDownItemText() {
        return this.title;
    }

    @Override
    public int getXCDropDownItemType() {
        return 1;
    }

    public Long getAutoId() {
        return this.autoId;
    }

    public void setAutoId(Long autoId) {
        this.autoId = autoId;
    }
}