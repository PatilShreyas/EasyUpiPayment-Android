package com.shreyaspatil.EasyUpiPayment.listener;

import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

public interface PaymentStatusListener {
    void onTransactionCompleted(TransactionDetails transactionDetails);
    void onTransactionSuccess();
    void onTransactionFailed();
    void onTransactionCancelled();
}
