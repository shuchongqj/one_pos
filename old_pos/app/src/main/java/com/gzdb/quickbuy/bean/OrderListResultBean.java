package com.gzdb.quickbuy.bean;

import java.util.List;

/**
 * Created by nongyudi on 2017/6/4.
 */

public class OrderListResultBean {

        private int total;
        private int pageSize;
        private int pageNum;
        private List<OrderDetailBean> datas;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public List<OrderDetailBean> getDatas() {
            return datas;
        }

        public void setDatas(List<OrderDetailBean> datas) {
            this.datas = datas;
        }


}
