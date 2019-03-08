package com.gzdb.sale.event;

import com.gzdb.sale.bean.SelectProduct;

import java.util.List;

public class SelectProductEvent {

    public SelectProductEvent(List<SelectProduct> products) {
        this.products = products;
    }

    public List<SelectProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SelectProduct> products) {
        this.products = products;
    }

    private List<SelectProduct> products;

}
