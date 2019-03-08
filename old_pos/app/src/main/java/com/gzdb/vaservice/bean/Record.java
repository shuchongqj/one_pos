package com.gzdb.vaservice.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Record {

    @Id(autoincrement = true)
    private long id;
    private  String order_id;
    private  String type;
    private  String head;
    private  String shid;
    private  String serial_no;
    private  String pki_no;
    private  String sys_order_no;
    private  String physics_no;
    private  String logic_no;
    private  String status;
    private  String auth_code;
    private  String card_back_no;
    private  String reader_back_no;
    private  String voucher_no;
    private  String money;
    private  String balance;
    private  String money_lower;
    private  String money_upper;
    private  String card_info;
    private  String rfu;

    @Generated(hash = 236421542)
    public Record(long id, String order_id, String type, String head, String shid,
            String serial_no, String pki_no, String sys_order_no, String physics_no,
            String logic_no, String status, String auth_code, String card_back_no,
            String reader_back_no, String voucher_no, String money, String balance,
            String money_lower, String money_upper, String card_info, String rfu) {
        this.id = id;
        this.order_id = order_id;
        this.type = type;
        this.head = head;
        this.shid = shid;
        this.serial_no = serial_no;
        this.pki_no = pki_no;
        this.sys_order_no = sys_order_no;
        this.physics_no = physics_no;
        this.logic_no = logic_no;
        this.status = status;
        this.auth_code = auth_code;
        this.card_back_no = card_back_no;
        this.reader_back_no = reader_back_no;
        this.voucher_no = voucher_no;
        this.money = money;
        this.balance = balance;
        this.money_lower = money_lower;
        this.money_upper = money_upper;
        this.card_info = card_info;
        this.rfu = rfu;
    }

    @Generated(hash = 477726293)
    public Record() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getShid() {
        return shid;
    }

    public void setShid(String shid) {
        this.shid = shid;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getPki_no() {
        return pki_no;
    }

    public void setPki_no(String pki_no) {
        this.pki_no = pki_no;
    }

    public String getSys_order_no() {
        return sys_order_no;
    }

    public void setSys_order_no(String sys_order_no) {
        this.sys_order_no = sys_order_no;
    }

    public String getPhysics_no() {
        return physics_no;
    }

    public void setPhysics_no(String physics_no) {
        this.physics_no = physics_no;
    }

    public String getLogic_no() {
        return logic_no;
    }

    public void setLogic_no(String logic_no) {
        this.logic_no = logic_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public String getCard_back_no() {
        return card_back_no;
    }

    public void setCard_back_no(String card_back_no) {
        this.card_back_no = card_back_no;
    }

    public String getReader_back_no() {
        return reader_back_no;
    }

    public void setReader_back_no(String reader_back_no) {
        this.reader_back_no = reader_back_no;
    }

    public String getVoucher_no() {
        return voucher_no;
    }

    public void setVoucher_no(String voucher_no) {
        this.voucher_no = voucher_no;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getMoney_lower() {
        return money_lower;
    }

    public void setMoney_lower(String money_lower) {
        this.money_lower = money_lower;
    }

    public String getMoney_upper() {
        return money_upper;
    }

    public void setMoney_upper(String money_upper) {
        this.money_upper = money_upper;
    }

    public String getCard_info() {
        return card_info;
    }

    public void setCard_info(String card_info) {
        this.card_info = card_info;
    }

    public String getRfu() {
        return rfu;
    }

    public void setRfu(String rfu) {
        this.rfu = rfu;
    }
}
