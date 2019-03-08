package com.gzdb.quickbuy.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by liubolin on 2018/1/31.
 */

public class AddressBean implements Serializable {

    /*
    *
country: "中华人民共和国",
adcode: "",
city: "",
latitude: 22.979006,
sex: 0,
streetNum: "",
addressAlias: "正店",
type: 0,
phoneNumber: "17605556677",
province: "",
createTime: 1507343810000,
street: "",
district: "",
name: "小菜",
detailAddress: "番禺区天安科技创业中心",
id: 10417,
passportId: 1924001,
longitude: 113.370094,
status: 2
    * */


    private Long id;

    private Long passportId;

    private String name;

    private String alias;

    private String country;

    private String adcode;

    private String city;

    private int sex;

    private String streetNum;

    private String addressAlias;

    private int type;

    private String province;

    private String createTime;

    private String street;

    private String district;

    private String phoneNumber;

    private String detailAddress;

    private double longitude;

    private double latitude;

    private int status;

    private String address;

    private String localAddress;

    public String getAddress() {
        if (!TextUtils.isEmpty(address)) {
            return address;
        } else {
            refreshAddress();
        }
        return address;
    }

    public String getLocalAddress() {
        if (!TextUtils.isEmpty(localAddress)) {
            return localAddress;
        } else {
            refreshLocalAddress();
        }
        return localAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void refreshLocalAddress() {
        StringBuilder sb = new StringBuilder();
        if (province != null) {
            sb.append(province).append(" ");
        }
        if (city != null) {
            sb.append(city).append(" ");
        }
        if (district != null) {
            sb.append(district).append(" ");
        }
        if (street != null) {
            sb.append(street);
        }
        if (streetNum != null) {
            sb.append(streetNum);
        }
        if (detailAddress != null) {
            sb.append(detailAddress);
        }
        localAddress = sb.toString();
    }

    public void refreshAddress() {
        StringBuilder sb = new StringBuilder();
        if (province != null) {
            sb.append(province).append(" ");
        }
        if (city != null) {
            sb.append(city).append(" ");
        }
        if (district != null) {
            sb.append(district).append(" ");
        }
        if (street != null) {
            sb.append(street);
        }
        if (streetNum != null) {
            sb.append(streetNum);
        }
        if (detailAddress != null) {
            sb.append(detailAddress);
        }
        address = sb.toString();
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    public String getAddressAlias() {
        return addressAlias;
    }

    public void setAddressAlias(String addressAlias) {
        this.addressAlias = addressAlias;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
