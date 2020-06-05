package com.shreyaspatil.EasyUpiPayment.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TransactionDetails {
    private String transactionId;
    private String responseCode;
    private String approvalRefNo;
    private String status;
    private String transactionRefId;
    private String amount;

    public TransactionDetails(
            String transactionId,
            String responseCode,
            String approvalRefNo,
            String status,
            String transactionRefId,
            String amount
    ) {
        this.transactionId = transactionId;
        this.responseCode = responseCode;
        this.approvalRefNo = approvalRefNo;
        this.status = status;
        this.transactionRefId = transactionRefId;
        this.amount = amount;
    }

    @Nullable
    public String getTransactionId() {
        return transactionId;
    }

    @Nullable
    public String getResponseCode() {
        return responseCode;
    }

    @Nullable
    public String getApprovalRefNo() {
        return approvalRefNo;
    }

    @Nullable
    public String getStatus() {
        return status;
    }

    @Nullable
    public String getTransactionRefId() {
        return transactionRefId;
    }

    @Nullable
    public String getAmount() {
        return amount;
    }

    @NonNull
    @Override
    public String toString() {
        return "transactionId:" + transactionId +
                ", responseCode:" + responseCode +
                ", transactionRefId:" + transactionRefId +
                ", approvalRefNo:" + approvalRefNo +
                ", status:" + status +
                ", amount:" + amount;
    }
}
