package com.gzdb.supermarket.been;


import java.io.Serializable;

/**
 * Created by nongyd on 17/5/27.
 */
public class UserBean  implements Serializable{

    private static final long serialVersionUID = 9066302298419571851L;
    /**
     * realName :
     * showName : 品悦生鲜超市
     * phoneNumber : 18824906653
     * roleValue : 0
     * showPhoneNumber : 188****6653
     * headImage :
     * versionMessage : {"upgradeType":1,"title":"已是最新版本","showVersion":"V1.0"}
     * accessToken : f90ee2c243107bb025b13c7cb2c9af06
     * passportId : 100005
     */

    private String realName;
    private String showName;
    private String phoneNumber;
    private String roleValue;
    private String showPhoneNumber;
    private String headImage;
    private VersionMessageBean versionMessage;
    private String accessToken;
    private String passportId;

    /**
     * accountType : 1
     * accountTypeName : 店员
     */

    private int accountType;
    private String accountTypeName;
    /**
     * tokenType : Bearer
     * passportId : 1924001
     */

    private String tokenType;
    private int isCode;             //收款码权限
    private int couponState;        //优惠券权限
    private String code;            //收款码

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRoleValue() {
        return roleValue;
    }

    public void setRoleValue(String roleValue) {
        this.roleValue = roleValue;
    }

    public String getShowPhoneNumber() {
        return showPhoneNumber;
    }

    public void setShowPhoneNumber(String showPhoneNumber) {
        this.showPhoneNumber = showPhoneNumber;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public VersionMessageBean getVersionMessage() {
        return versionMessage;
    }

    public void setVersionMessage(VersionMessageBean versionMessage) {
        this.versionMessage = versionMessage;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getIsCode() {
        return isCode;
    }

    public void setIsCode(int isCode) {
        this.isCode = isCode;
    }

    public int getCouponState() {
        return couponState;
    }

    public void setCouponState(int couponState) {
        this.couponState = couponState;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class VersionMessageBean implements Serializable{
        private static final long serialVersionUID = -1894904999306773123L;
        /**
         * upgradeType : 1
         * title : 已是最新版本
         * showVersion : V1.0
         */

        private int upgradeType;
        private String title;
        private String showVersion;

        public int getUpgradeType() {
            return upgradeType;
        }

        public void setUpgradeType(int upgradeType) {
            this.upgradeType = upgradeType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getShowVersion() {
            return showVersion;
        }

        public void setShowVersion(String showVersion) {
            this.showVersion = showVersion;
        }
    }
}
