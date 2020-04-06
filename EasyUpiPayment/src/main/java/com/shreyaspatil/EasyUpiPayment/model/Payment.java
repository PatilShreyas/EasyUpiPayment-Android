package com.shreyaspatil.EasyUpiPayment.model;

import java.io.Serializable;

public class Payment implements Serializable {
    private final String currency = "INR";
    private String vpa;
    private String name;
    private String payeeMerchantCode;
    private String txnId;
    private String txnRefId;
    private String description;
    private String amount;
    private String defaultPackage;

    public String getVpa() {
        return vpa;
    }

    public void setVpa(String vpa) {
        this.vpa = vpa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayeeMerchantCode() {
        return payeeMerchantCode;
    }

    public void setPayeeMerchantCode(String payeeMerchantCode) {
        this.payeeMerchantCode = payeeMerchantCode;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getTxnRefId() {
        return txnRefId;
    }

    public void setTxnRefId(String txnRefId) {
        this.txnRefId = txnRefId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDefaultPackage() {
        return defaultPackage;
    }

    public void setDefaultPackage(String defaultPackage) {
        this.defaultPackage = defaultPackage;
    }
}
