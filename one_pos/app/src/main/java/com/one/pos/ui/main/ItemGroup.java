package com.one.pos.ui.main;

import com.one.pos.db.Item;

import java.util.List;

/**
 * @author zhumg
 */
public class ItemGroup {

    private List<Item> items;
    //0未有内容，1加载中，10加载完成
    private int status;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getStatus() {
        return status;
    }

    public boolean isNullItems() {
        return status == 0 || items == null || items.size() < 1;
    }

    public boolean isLoading() {
        return status == 1;
    }

    public boolean isLoadSuccess() {
        return status == 10;
    }

    public void setStatusToDefault() {
        status = 0;
    }

    public void setStatusToLoading() {
        status = 1;
    }

    public void setStatusToLoadSuccess() {
        status = 10;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
