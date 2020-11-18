package com.shreyaspatil.easyupipayment.listener

import com.shreyaspatil.easyupipayment.model.TransactionDetails

interface PaymentStatusListener {
    fun onTransactionCompleted(transactionDetails: TransactionDetails)
    fun onTransactionCancelled()
}