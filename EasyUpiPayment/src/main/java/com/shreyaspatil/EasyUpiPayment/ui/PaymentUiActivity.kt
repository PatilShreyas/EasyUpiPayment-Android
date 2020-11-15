package com.shreyaspatil.easyupipayment.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shreyaspatil.easyupipayment.R
import com.shreyaspatil.easyupipayment.Singleton
import com.shreyaspatil.easyupipayment.exception.AppNotFoundException
import com.shreyaspatil.easyupipayment.model.Payment
import com.shreyaspatil.easyupipayment.model.TransactionDetails
import com.shreyaspatil.easyupipayment.model.TransactionStatus
import java.util.*

class PaymentUiActivity : AppCompatActivity() {

	private lateinit var payment: Payment

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_upipay)

		payment = (intent.getSerializableExtra(EXTRA_KEY_PAYMENT) as Payment?)
			?: throw IllegalStateException("Unable to parse payment details")

		// Set Parameters for UPI
		val paymentUri = Uri.Builder().apply {
			with(payment) {
				scheme("upi").authority("pay")
				appendQueryParameter("pa", vpa)
				appendQueryParameter("pn", name)
				appendQueryParameter("tid", txnId)
				appendQueryParameter("mc", payeeMerchantCode)
				appendQueryParameter("tr", txnRefId)
				appendQueryParameter("tn", description)
				appendQueryParameter("am", amount)
				appendQueryParameter("cu", currency)
			}
		}.build()

		// Set Data Intent
		val paymentIntent = Intent(Intent.ACTION_VIEW).apply {
			data = paymentUri

			// Check for Default package
			payment.defaultPackage?.let { `package` = it }
		}

		// Show Dialog to user
		val appChooser = Intent.createChooser(paymentIntent, "Pay using")

		// Check if other UPI apps are exists or not.
		if (paymentIntent.resolveActivity(packageManager) != null) {
			startActivityForResult(appChooser, PAYMENT_REQUEST)
		} else {
			Toast.makeText(
				this,
				"No UPI app found! Please Install to Proceed!",
				Toast.LENGTH_SHORT).show()
			throwOnAppNotFound()
		}
	}

	public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == PAYMENT_REQUEST) {
			if (data != null) {
				// Get Response from activity intent
				val response = data.getStringExtra("response")

				if (response == null) {
					callbackTransactionCancelled()
					Log.d(TAG, "Payment Response is null")
				} else {
					runCatching {
						// Get transactions details from response.
						val transactionDetails = getTransactionDetails(response)

						// Update Listener onTransactionCompleted()
						callbackTransactionCompleted(transactionDetails)
					}.getOrElse {
						callbackTransactionCancelled()
					}
				}
			} else {
				Log.e(TAG, "Intent Data is null. User cancelled")
				callbackTransactionCancelled()
			}
			finish()
		}
	}

	// Make TransactionDetails object from response string
	@JvmSynthetic
	internal fun getTransactionDetails(response: String): TransactionDetails {
		return with(getMapFromQuery(response)) {
			TransactionDetails(
				transactionId = get("txnId"),
				responseCode = get("responseCode"),
				approvalRefNo = get("ApprovalRefNo"),
				transactionRefId = get("txnRef"),
				amount = payment.amount,
				transactionStatus = TransactionStatus.valueOf(
					get("Status")?.toUpperCase(Locale.getDefault())
						?: TransactionStatus.FAILURE.name
				)
			)
		}
	}

	@JvmSynthetic
	internal fun getMapFromQuery(queryString: String): Map<String, String> {
		val map = mutableMapOf<String, String>()
		val keyValuePairs = queryString
			.split("&")
			.map { param ->
				param.split("=").let { Pair(it[0], it[1]) }
			}
		map.putAll(keyValuePairs)
		return map
	}

	@JvmSynthetic
	internal fun throwOnAppNotFound() {
		Log.e(TAG, "No UPI app found on device.")
		throw AppNotFoundException(payment.defaultPackage)
	}

	@JvmSynthetic
	internal fun callbackTransactionCancelled() {
		Singleton.listener?.onTransactionCancelled()
	}

	@JvmSynthetic
	internal fun callbackTransactionCompleted(transactionDetails: TransactionDetails) {
		Singleton.listener?.onTransactionCompleted(transactionDetails)
	}

	companion object {
		const val TAG = "PaymentUiActivity"
		const val PAYMENT_REQUEST = 4400
		const val EXTRA_KEY_PAYMENT = "payment"
	}
}