package com.one.pos.ui.main;

import com.one.pos.db.Item;

/**
 * 购物车的模型
 * @author zhumg
 */
public class CartModel {

    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
