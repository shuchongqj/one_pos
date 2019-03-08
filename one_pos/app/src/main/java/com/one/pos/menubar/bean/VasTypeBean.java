package com.one.pos.menubar.bean;


import cc.solart.turbo.ChooseBean;

/**
 * Author: even
 * Date:   2019/3/5
 * Description:
 */
public class VasTypeBean extends ChooseBean {
    public String title;
    public String type;
    public boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
