package com.gzdb.vip;

import java.io.Serializable;

/**
 * @author zhumg
 */
public class VipUserInfo implements Serializable {

    private int code;
    private int balance;
    private String pay_member_type;
    private String real_name;
    private String phone_number;
    private String pay_member_time;
    private int surplus_integral;
    private long passport_id;
    private String recharge_ordinance;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getPay_member_type() {
        return pay_member_type;
    }

    public void setPay_member_type(String pay_member_type) {
        this.pay_member_type = pay_member_type;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPay_member_time() {
        return pay_member_time;
    }

    public void setPay_member_time(String pay_member_time) {
        this.pay_member_time = pay_member_time;
    }

    public int getSurplus_integral() {
        return surplus_integral;
    }

    public void setSurplus_integral(int surplus_integral) {
        this.surplus_integral = surplus_integral;
    }

    public long getPassport_id() {
        return passport_id;
    }

    public void setPassport_id(long passport_id) {
        this.passport_id = passport_id;
    }

    public String getRecharge_ordinance() {
        return recharge_ordinance;
    }

    public void setRecharge_ordinance(String recharge_ordinance) {
        this.recharge_ordinance = recharge_ordinance;
    }
}
