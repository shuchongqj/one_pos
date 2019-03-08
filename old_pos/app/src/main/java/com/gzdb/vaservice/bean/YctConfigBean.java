package com.gzdb.vaservice.bean;

public class YctConfigBean {


    /**
     * pkiCardNumber : 11
     * terminalNumber : 11
     * psamCardNumber : 22
     * uploadPassword : 0000000000000000
     * merchantNumber : 0880100000000023
     * isState : 1
     * host : 121.32.31.2
     * port : 5010
     * secret : 11223344556677888877665544332211
     */

    private String pkiCardNumber;
    private String terminalNumber;
    private String psamCardNumber;
    private String uploadPassword;
    private String merchantNumber;
    private int isState;
    private String host;
    private int port;
    private String secret;

    public String getPkiCardNumber() {
        return pkiCardNumber;
    }

    public void setPkiCardNumber(String pkiCardNumber) {
        this.pkiCardNumber = pkiCardNumber;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public String getPsamCardNumber() {
        return psamCardNumber;
    }

    public void setPsamCardNumber(String psamCardNumber) {
        this.psamCardNumber = psamCardNumber;
    }

    public String getUploadPassword() {
        return uploadPassword;
    }

    public void setUploadPassword(String uploadPassword) {
        this.uploadPassword = uploadPassword;
    }

    public String getMerchantNumber() {
        return merchantNumber;
    }

    public void setMerchantNumber(String merchantNumber) {
        this.merchantNumber = merchantNumber;
    }

    public int getIsState() {
        return isState;
    }

    public void setIsState(int isState) {
        this.isState = isState;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
