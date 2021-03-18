package dev.shreyaspatil.easyupipayment.model

data class TransactionDetails(
	val transactionId: String?,
	val responseCode: String?,
	val approvalRefNo: String?,
	val transactionStatus: TransactionStatus,
	val transactionRefId: String?,
	val amount: String?
)

enum class TransactionStatus {
	FAILURE, SUCCESS, SUBMITTED
}