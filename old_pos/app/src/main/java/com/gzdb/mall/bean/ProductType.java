package com.gzdb.mall.bean;

public class ProductType {

    /**
     * classification_icons : http://oss.0085.com/2018/0810/mall_img/15338949770839.png
     * create_time : 1533894978000
     * id : 7
     * sort : 2
     * title : 食材
     * type : 0
     * passport_id : 0
     * is_delete : N
     */

    private String classification_icons;
    private long create_time;
    private int id;
    private int sort;
    private String title;
    private int type;
    private int passport_id;
    private String is_delete;

    public String getClassification_icons() {
        return classification_icons;
    }

    public void setClassification_icons(String classification_icons) {
        this.classification_icons = classification_icons;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPassport_id() {
        return passport_id;
    }

    public void setPassport_id(int passport_id) {
        this.passport_id = passport_id;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }
}
