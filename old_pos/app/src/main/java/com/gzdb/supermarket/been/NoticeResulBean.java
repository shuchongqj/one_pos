package com.gzdb.supermarket.been;

import java.util.List;

/**
 * Created by nongyd on 17/6/10.
 */

public class NoticeResulBean {

    private List<DatasBean> datas;

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * id : 1
         * title : 6月VIP商户POS机使用补贴政策
         * content : <ol class=" list-paddingleft-2" style="list-style-type: decimal;"><li><p>POS收银订单量补贴（最高可补贴450元）
         商家使用pos系统收银。支付宝或微信扫码收款订单，每笔补贴0.1元，1个月最高返300元/商户。现金收款订单，每笔补贴0.05元，1个月最高返150元/商户。</p></li></ol><p>&nbsp; &nbsp; &nbsp;2. 商家余额下货补贴（补贴不设上限）
         商家用pos机支付宝或微信扫码收款收到“商家余额”的钱在1号平台下货，享受下货顶多拿总金额1%的补贴（补贴计算方式可与充值方式或满减方式叠加，但须去除非正常收款部分金额）。 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
         &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</p>
         * isDelete : N
         * isDisplay : Y
         * createTime : 1496975465000
         * updateTime : 1496988886000
         * createPassport : 100164
         */

        private int id;
        private String title;
        private String content;
        private String isDelete;
        private String isDisplay;
        private long createTime;
        private long updateTime;
        private int createPassport;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public String getIsDisplay() {
            return isDisplay;
        }

        public void setIsDisplay(String isDisplay) {
            this.isDisplay = isDisplay;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getCreatePassport() {
            return createPassport;
        }

        public void setCreatePassport(int createPassport) {
            this.createPassport = createPassport;
        }
    }
}
