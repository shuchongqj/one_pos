package com.gzdb.supermarket.been;

import com.gzdb.supermarket.common.XCDropDownListView;

import java.io.Serializable;

import cc.solart.turbo.ChooseBean;

public class ItemType extends ChooseBean implements XCDropDownListView.XCDropDownItem,Serializable {

    /**
     * id : 100248
     * passportId : 1927939
     * itemTypeId : 103786
     * itemTypeTitle : 饮料
     * isDelete : N
     * sort : 0
     */

    private String id;
    private String passportId;
    private String itemTypeId;
    private String itemTypeTitle;
    private String isDelete;
    private int sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeTitle() {
        return itemTypeTitle;
    }

    public void setItemTypeTitle(String itemTypeTitle) {
        this.itemTypeTitle = itemTypeTitle;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String getXCDropDownItemText() {
        return this.itemTypeTitle;
    }

    @Override
    public int getXCDropDownItemType() {
        return 1;
    }
}
