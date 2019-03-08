package com.gzdb.supermarket.been;

import java.util.List;

public class ScreenFile {

    /**
     * type : 0
     * time : 0
     * urls : ["http://oss.0085.com/2018/0629/positems/15302658630862.jpeg","http://oss.0085.com/2018/0804/positems/15333671250398.png","http://oss.0085.com/2017/0902/positems/15043463570883.png"]
     */

    private int type;
    private int time;
    private List<String> urls;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
