package com.gzdb.quickbuy.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhumg on 12/8.
 */
public class QuickBuyItem implements Serializable {

    private int standardInventory;
    private String warnInventory;
    private String menuTypeId;   //商品类型ID
    private String repertory;
    private String barcode;
    private List<QuickItem> items;

    private static final Map<Integer,List<QuickBuyItem>> itemListMap = new HashMap<>();

    public String getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId(String menuTypeId) {
        this.menuTypeId = menuTypeId;
    }

    //选中标记，默认全选
    private boolean select = true;
    //购买数量
    private int count;
    //选中的子
    private QuickItem selectItem;
    //推荐购买数量
    private int modernNum;

    public int getModernNum() {
        return modernNum;
    }

    public void setModernNum(int modernNum) {
        this.modernNum = modernNum;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public void add() {
        this.count++;
    }

    public void del() {
        this.count--;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        if(this.modernNum == 0) {
            this.modernNum = this.count;
        }
    }

    public QuickItem getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(QuickItem selectItem) {
        this.selectItem = selectItem;
        this.count = this.selectItem.getDefaultPurchase();
    }

    public int getStandardInventory() {
        return standardInventory;
    }

    public void setStandardInventory(int standardInventory) {
        this.standardInventory = standardInventory;
    }

    public String getWarnInventory() {
        return warnInventory;
    }

    public void setWarnInventory(String warnInventory) {
        this.warnInventory = warnInventory;
    }

    public String getRepertory() {
        return repertory;
    }

    public void setRepertory(String repertory) {
        this.repertory = repertory;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<QuickItem> getItems() {
        return items;
    }

    public void setItems(List<QuickItem> items) {
        this.items = items;
    }

    public static class QuickItem implements Serializable {
        private String standard;
        private String image;
        private String unit;
        private int defaultPurchase;//默认购买数量
        private String price;
        private String name;
        private String id;
        private String buyRate;//进价毛利率
        private String saleRate;//售价毛利率
        private int stock; //库存

        public String getBuyRate() {
            return buyRate;
        }

        public void setBuyRate(String buyRate) {
            this.buyRate = buyRate;
        }

        public String getSaleRate() {
            return saleRate;
        }

        public void setSaleRate(String saleRate) {
            this.saleRate = saleRate;
        }

        public String getStandard() {
            return standard;
        }

        public void setStandard(String standard) {
            this.standard = standard;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getDefaultPurchase() {
            return defaultPurchase;
        }

        public void setDefaultPurchase(int defaultPurchase) {
            this.defaultPurchase = defaultPurchase;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }
    /**
    *  根据商品类型ID 获取一堆商品
     */
    public static List<QuickBuyItem> gets(int menuTypeId){
        synchronized (itemListMap){
            return itemListMap.get(menuTypeId);
        }
    }
}
