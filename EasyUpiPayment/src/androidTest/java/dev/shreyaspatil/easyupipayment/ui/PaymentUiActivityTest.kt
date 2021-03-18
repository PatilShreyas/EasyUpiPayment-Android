package com.shreyaspatil.easyupipayment.ui

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.dx.mockito.inline.extended.ExtendedMockito.spyOn
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import dev.shreyaspatil.easyupipayment.testutils.paymentWith
import dev.shreyaspatil.easyupipayment.ui.PaymentUiActivity
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class PaymentUiActivityTest {

	@Test
	fun shouldNotThrowIllegalStateException_whenPaymentDetailsProvided() {
		val intent = getPaymentIntent()
		launchActivity<PaymentUiActivity>(intent).onActivity {
			assertTrue(it.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
		}
	}

	@Test
	fun shouldFinishActivity_whenTransactionIsOver() {
		launchActivity<PaymentUiActivity>(getPaymentIntent()).onActivity {
			val responseIntent = Intent().apply { putExtra("response", "") }

			// Give transaction result
			it.onActivityResult(PaymentUiActivity.PAYMENT_REQUEST, Activity.RESULT_OK, responseIntent)

			// Verify Activity is destroyed or not
			assertTrue(it.lifecycle.currentState.isAtLeast(Lifecycle.State.DESTROYED))
		}
	}

	@Test
	fun shouldGiveCancelledCallback_whenTransactionIsCancelled() {
		launchActivity<PaymentUiActivity>(getPaymentIntent()).onActivity {
			spyOn(it)

			it.onActivityResult(
				PaymentUiActivity.PAYMENT_REQUEST,
				Activity.RESULT_CANCELED,
				null
			)

			verify(it).callbackTransactionCancelled()
		}
	}

	@Test
	fun shouldGiveCompletedCallback_whenTransactionIsFailed() {
		launchActivity<PaymentUiActivity>(getPaymentIntent()).onActivity {
			spyOn(it)

			val transactionDetails = it.getTransactionDetails(RESPONSE_TRANSACTION_FAILURE)

			it.onActivityResult(
				PaymentUiActivity.PAYMENT_REQUEST,
				Activity.RESULT_OK,
				getResponseIntent(RESPONSE_TRANSACTION_FAILURE)
			)

			verify(it).callbackTransactionCompleted(transactionDetails)
		}
	}

	@Test
	fun shouldGiveCompletedCallback_whenTransactionIsSuccessful() {
		launchActivity<PaymentUiActivity>(getPaymentIntent()).onActivity {
			spyOn(it)

			val transactionDetails = it.getTransactionDetails(RESPONSE_TRANSACTION_SUCCESS)

			it.onActivityResult(
				PaymentUiActivity.PAYMENT_REQUEST,
				Activity.RESULT_OK,
				getResponseIntent(RESPONSE_TRANSACTION_SUCCESS)
			)

			verify(it).callbackTransactionCompleted(transactionDetails)
		}
	}

	@Test
	fun shouldGiveCompletedCallback_whenTransactionIsSubmitted() {
		launchActivity<PaymentUiActivity>(getPaymentIntent()).onActivity {
			spyOn(it)

			val transactionDetails = it.getTransactionDetails(RESPONSE_TRANSACTION_SUBMITTED)

			it.onActivityResult(
				PaymentUiActivity.PAYMENT_REQUEST,
				Activity.RESULT_OK,
				getResponseIntent(RESPONSE_TRANSACTION_SUBMITTED)
			)

			verify(it).callbackTransactionCompleted(transactionDetails)
		}
	}

	private fun getPaymentIntent(paymentApp: PaymentApp? = null) = Intent(
		ApplicationProvider.getApplicationContext(),
		PaymentUiActivity::class.java
	).apply {
		putExtra(PaymentUiActivity.EXTRA_KEY_PAYMENT, paymentWith(paymentApp))
	}

	private fun getResponseIntent(response: String) = Intent().apply {
		putExtra("response", response)
	}

	companion object {
		const val RESPONSE_TRANSACTION_SUCCESS = "txnId=abcdefghijklmnopqrstuvwxyz123456789&responseCode=00&ApprovalRefNo=122321&Status=SUCCESS&txnRef=6655443322"
		const val RESPONSE_TRANSACTION_SUBMITTED = "txnId=abcdefghijklmnopqrstuvwxyz123456789&responseCode=ZM&ApprovalRefNo=122321&Status=SUBMITTED&txnRef=6655443322"
		const val RESPONSE_TRANSACTION_FAILURE = "txnId=abcdefghijklmnopqrstuvwxyz123456789&responseCode=Y1&ApprovalRefNo=122321&Status=FAILURE&txnRef=6655443322"
	}
}

