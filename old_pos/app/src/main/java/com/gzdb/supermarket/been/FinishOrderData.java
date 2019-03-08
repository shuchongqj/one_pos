package com.gzdb.supermarket.been;

import com.gzdb.basepos.greendao.DaoSession;
import com.gzdb.basepos.greendao.FinishOrderDataDao;
import com.gzdb.basepos.greendao.ItemSnapshotsBeanDao;
import com.gzdb.supermarket.util.Arith;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by dianba on 2016/5/12.
 */
@Entity
public class FinishOrderData {


    /**
     * sequenceNumber : 90820170601171756315765126
     * actualPrice : 1.0
     * totalPrice : 1.0
     * refundStatus : 0
     * receiptPhone :
     * type : 7
     * deliverStatus : 0
     * paymentType : -1
     * itemSnapshots : [{"itemUnitName":"","marketPrice":0,"userMark":"","introductionPage":"","orderId":"100552","totalPrice":1,"itemBarcode":"88588","itemCode":"","costPrice":1,"returnPrice":0,"itemTemplateId":100084,"itemId":10027,"itemBatches":0,"itemName":"旅途","normalPrice":1,"itemUnitId":"0","itemTypeName":"零食糖果","id":"100758","itemTypeId":"11000","normalQuantity":1}]
     * transType : 1
     * createTime : 1496308676000
     * partnerUserId : 100005
     * id : 100552
     * partnerId : xlb908100000
     * paymentTime : 1496308680000
     * cancelLogger :
     * status : 8
     */
    @Id(autoincrement = true)
    private Long autoId;

    private String passportId;//商家ID，离线订单用到
    private double discount;//优惠，打印小票用
    private double change;//找零，打印小票用

    private String sequenceNumber;
    private double actualPrice;//实收
    private double totalPrice;//应收总价
    private double discountPrice;
    private double membershipPrice;
    private int refundStatus;
    private String receiptPhone;
    private int type;
    private int deliverStatus;
    private String paymentType;
    private String transType;
    private long createTime;
    private String partnerUserId;
    private String id;
    private String partnerId;
    private long paymentTime;
    private String cancelLogger;
    private int status;
    private String rewardAmount;
    private String mobilePhone;


    @ToMany(referencedJoinProperty = "OffLineOrderId")
    private List<ItemSnapshotsBean> itemSnapshots;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 85013076)
    private transient FinishOrderDataDao myDao;



    @Keep
    @Generated(hash = 601865480)
    public FinishOrderData(Long autoId, String passportId, double discount, double change, String sequenceNumber, double actualPrice, double totalPrice, double discountPrice, double membershipPrice, int refundStatus, String receiptPhone, int type, int deliverStatus, String paymentType, String transType, long createTime, String partnerUserId, String id, String partnerId,
                           long paymentTime, String cancelLogger, int status, String rewardAmount,String mobilePhone) {
        this.autoId = autoId;
        this.passportId = passportId;
        this.discount = discount;
        this.change = change;
        this.sequenceNumber = sequenceNumber;
        this.actualPrice = actualPrice;
        this.totalPrice = totalPrice;
        this.discountPrice = discountPrice;
        this.membershipPrice = membershipPrice;
        this.refundStatus = refundStatus;
        this.receiptPhone = receiptPhone;
        this.type = type;
        this.deliverStatus = deliverStatus;
        this.paymentType = paymentType;
        this.transType = transType;
        this.createTime = createTime;
        this.partnerUserId = partnerUserId;
        this.id = id;
        this.partnerId = partnerId;
        this.paymentTime = paymentTime;
        this.cancelLogger = cancelLogger;
        this.status = status;
        this.rewardAmount = rewardAmount;
        this.mobilePhone = mobilePhone;
    }

    @Generated(hash = 1510961146)
    public FinishOrderData() {
    }


    public Long getAutoId() {
        return this.autoId;
    }

    public void setAutoId(Long autoId) {
        this.autoId = autoId;
    }

    public String getPassportId() {
        return this.passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public double getActualPrice() {
        return this.actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getMembershipPrice() {
        return membershipPrice;
    }

    public void setMembershipPrice(double membershipPrice) {
        this.membershipPrice = membershipPrice;
    }

    public int getRefundStatus() {
        return this.refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getReceiptPhone() {
        return this.receiptPhone;
    }

    public void setReceiptPhone(String receiptPhone) {
        this.receiptPhone = receiptPhone;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDeliverStatus() {
        return this.deliverStatus;
    }

    public void setDeliverStatus(int deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public String getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransType() {
        return this.transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getPartnerUserId() {
        return this.partnerUserId;
    }

    public void setPartnerUserId(String partnerUserId) {
        this.partnerUserId = partnerUserId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartnerId() {
        return this.partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public long getPaymentTime() {
        return this.paymentTime;
    }

    public void setPaymentTime(long paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getCancelLogger() {
        return this.cancelLogger;
    }

    public void setCancelLogger(String cancelLogger) {
        this.cancelLogger = cancelLogger;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 227064650)
    public List<ItemSnapshotsBean> getItemSnapshots() {
        if (itemSnapshots == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ItemSnapshotsBeanDao targetDao = daoSession.getItemSnapshotsBeanDao();
            List<ItemSnapshotsBean> itemSnapshotsNew = targetDao._queryFinishOrderData_ItemSnapshots(autoId);
            synchronized (this) {
                if (itemSnapshots == null) {
                    itemSnapshots = itemSnapshotsNew;
                }
            }
        }
        return itemSnapshots;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 681739923)
    public synchronized void resetItemSnapshots() {
        itemSnapshots = null;
    }

    public void setItemSnapshots(List<ItemSnapshotsBean> itemSnapshots) {
        this.itemSnapshots = itemSnapshots;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public double getChange() {
        return this.change > 0 ? this.change : Arith.del(actualPrice, totalPrice) < 0 ? 0.0 : Arith.del(actualPrice, totalPrice);
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getDiscount() {
        return this.discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getRewardAmount() {
        return this.rewardAmount;
    }

    public void setRewardAmount(String rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 925320349)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFinishOrderDataDao() : null;
    }
}
