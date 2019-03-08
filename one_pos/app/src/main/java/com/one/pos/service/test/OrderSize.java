package com.one.pos.service.test;

/**
 * @author zhumg
 */
public class OrderSize {

    private int refundSize;
    private int newOrderSize;
    private int acceptSize;
    private int distributionSize;
    private int evaluateSize;
    private long refundLastRefreshTime;
    private long newLastRefreshTime;
    private long acceptLastRefreshTime;
    private long distributionLastRefreshTime;
    private long evaluateLastRefreshTime;

    public long getRefundLastRefreshTime() {
        return refundLastRefreshTime;
    }

    public void setRefundLastRefreshTime(long refundLastRefreshTime) {
        this.refundLastRefreshTime = refundLastRefreshTime;
    }

    public long getNewLastRefreshTime() {
        return newLastRefreshTime;
    }

    public void setNewLastRefreshTime(long newLastRefreshTime) {
        this.newLastRefreshTime = newLastRefreshTime;
    }

    public long getAcceptLastRefreshTime() {
        return acceptLastRefreshTime;
    }

    public void setAcceptLastRefreshTime(long acceptLastRefreshTime) {
        this.acceptLastRefreshTime = acceptLastRefreshTime;
    }

    public long getDistributionLastRefreshTime() {
        return distributionLastRefreshTime;
    }

    public void setDistributionLastRefreshTime(long distributionLastRefreshTime) {
        this.distributionLastRefreshTime = distributionLastRefreshTime;
    }

    public long getEvaluateLastRefreshTime() {
        return evaluateLastRefreshTime;
    }

    public void setEvaluateLastRefreshTime(long evaluateLastRefreshTime) {
        this.evaluateLastRefreshTime = evaluateLastRefreshTime;
    }

    public int getRefundSize() {
        return refundSize;
    }

    public void setRefundSize(int refundSize) {
        this.refundSize = refundSize;
    }

    public int getNewOrderSize() {
        return newOrderSize;
    }

    public void setNewOrderSize(int newOrderSize) {
        this.newOrderSize = newOrderSize;
    }

    public int getAcceptSize() {
        return acceptSize;
    }

    public void setAcceptSize(int acceptSize) {
        this.acceptSize = acceptSize;
    }

    public int getDistributionSize() {
        return distributionSize;
    }

    public void setDistributionSize(int distributionSize) {
        this.distributionSize = distributionSize;
    }

    public int getEvaluateSize() {
        return evaluateSize;
    }

    public void setEvaluateSize(int evaluateSize) {
        this.evaluateSize = evaluateSize;
    }
}
