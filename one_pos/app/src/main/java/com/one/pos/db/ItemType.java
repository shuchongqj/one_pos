package com.one.pos.db;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author zhumg
 */
@Entity
public class ItemType {

    public static final int ITEM_TYPE_STATUS_DEFAULT = 0;
    public static final int ITEM_TYPE_STATUS_TEMP = -1;

    private long id;
    private String name;

    //该分类的状态
    @Transient
    private int status;
    //是否被选中
    @Transient
    private boolean select;
    //该分类下的所有子
    @Transient
    private List<Item> childs;

    public ItemType() {
    }

    public ItemType(int id, String name, boolean select) {
        this.id = id;
        this.name = name;
        this.select = select;
    }

    @Generated(hash = 1750001330)
    public ItemType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void copy(ItemType itemType) {
        this.name = itemType.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public List<Item> getChilds() {
        return childs;
    }

    public void setChilds(List<Item> childs) {
        this.childs = childs;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
