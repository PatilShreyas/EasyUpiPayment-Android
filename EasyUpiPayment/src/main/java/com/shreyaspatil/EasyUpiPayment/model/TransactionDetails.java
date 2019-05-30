package com.shreyaspatil.EasyUpiPayment.model;

import androidx.annotation.NonNull;

public class TransactionDetails {
    private String transactionId;
    private String responseCode;
    private String approvalRefNo;
    private String status;
    private String transactionRefId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getApprovalRefNo() {
        return approvalRefNo;
    }

    public void setApprovalRefNo(String approvalRefNo) {
        this.approvalRefNo = approvalRefNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionRefId() {
        return transactionRefId;
    }

    public void setTransactionRefId(String transactionRefId) {
        this.transactionRefId = transactionRefId;
    }

    @NonNull
    @Override
    public String toString() {
        return "transactionId:" + transactionId +
                ", responseCode:" + responseCode +
                ", transactionRefId:" + transactionRefId +
                ", approvalRefNo:" + approvalRefNo +
                ", status:" + status;

    }
}
