package com.anlib.refresh;

public enum RefreshLoaderUiTypeEnum {

    DEFAULT(0, "默认"),
    LOAD(1, "加载"),
    EMPTY(2, "空"),
    OVER(3, "结束"),
    ERROR(4, "异常"),
    HIDE(5, "隐藏"),
    ;

    private int type;
    private String name;

    RefreshLoaderUiTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
