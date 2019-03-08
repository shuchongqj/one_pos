package com.gzdb.supermarket.event;

public class SearchOnEvent {
    private boolean search;

    public SearchOnEvent() {
    }

    public SearchOnEvent(boolean search) {
        this.search = search;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }
}
