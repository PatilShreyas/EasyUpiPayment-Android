package dev.shreyaspatil.easyupipayment.listener

import dev.shreyaspatil.easyupipayment.model.TransactionDetails

interface PaymentStatusListener {
	fun onTransactionCompleted(transactionDetails: TransactionDetails)
	fun onTransactionCancelled()
}