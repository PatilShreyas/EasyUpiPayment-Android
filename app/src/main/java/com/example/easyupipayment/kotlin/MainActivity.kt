package com.example.easyupipayment.kotlin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.easyupipayment.R
import com.shreyaspatil.easyupipayment.EasyUpiPayment
import com.shreyaspatil.easyupipayment.exception.AppNotFoundException
import com.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import com.shreyaspatil.easyupipayment.model.PaymentApp
import com.shreyaspatil.easyupipayment.model.TransactionDetails
import com.shreyaspatil.easyupipayment.model.TransactionStatus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PaymentStatusListener {

	private lateinit var easyUpiPayment: EasyUpiPayment

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		// Setup click listener for Pay button
		button_pay.setOnClickListener { pay() }
	}

	private fun pay() {
		val payeeVpa = field_vpa.text.toString()
		val payeeName = field_name.text.toString()
		val transactionId = field_transaction_id.text.toString()
		val transactionRefId = field_transaction_ref_id.text.toString()
		val description = field_description.text.toString()
		val amount = field_amount.text.toString()
		val paymentAppChoice = radioAppChoice

		val paymentApp = when (paymentAppChoice.checkedRadioButtonId) {
			R.id.app_default -> PaymentApp.ALL
			R.id.app_amazonpay -> PaymentApp.AMAZON_PAY
			R.id.app_bhim_upi -> PaymentApp.BHIM_UPI
			R.id.app_google_pay -> PaymentApp.GOOGLE_PAY
			R.id.app_phonepe -> PaymentApp.PHONE_PE
			R.id.app_paytm -> PaymentApp.PAYTM
			else -> throw IllegalStateException("Unexpected value: " + paymentAppChoice.id)
		}

		try {
			// START PAYMENT INITIALIZATION
			easyUpiPayment = EasyUpiPayment(this) {
				this.paymentApp = paymentApp
				this.payeeVpa = payeeVpa
				this.payeeName = payeeName
				this.transactionId = transactionId
				this.transactionRefId = transactionRefId
				this.description = description
				this.amount = amount
			}
			// END INITIALIZATION

			// Register Listener for Events
			easyUpiPayment.setPaymentStatusListener(this)

			// Start payment / transaction
			easyUpiPayment.startPayment()
		} catch (exception: AppNotFoundException) {
			exception.printStackTrace()
			toast("Error:" + exception.message)
		}
	}

	override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
		// Transaction Completed
		Log.d("TransactionDetails", transactionDetails.toString())
		textView_status.text = transactionDetails.toString()

		when (transactionDetails.transactionStatus) {
			TransactionStatus.SUCCESS -> onTransactionSuccess()
			TransactionStatus.FAILURE -> onTransactionFailed()
			TransactionStatus.SUBMITTED -> onTransactionSubmitted()
		}
	}

	override fun onTransactionCancelled() {
		// Payment Cancelled by User
		toast("Cancelled by user")
		imageView.setImageResource(R.drawable.ic_failed)
	}

	private fun onTransactionSuccess() {
		// Payment Success
		toast("Success")
		imageView.setImageResource(R.drawable.ic_success)
	}

	private fun onTransactionSubmitted() {
		// Payment Pending
		toast("Pending | Submitted")
		imageView.setImageResource(R.drawable.ic_success)
	}

	private fun onTransactionFailed() {
		// Payment Failed
		toast("Failed")
		imageView.setImageResource(R.drawable.ic_failed)
	}

	private fun toast(message: String) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
	}

//    Uncomment this if you have inherited [android.app.Activity] and not [androidx.appcompat.app.AppCompatActivity]
//
//	override fun onDestroy() {
//		super.onDestroy()
//		easyUpiPayment.removePaymentStatusListener()
//	}
}