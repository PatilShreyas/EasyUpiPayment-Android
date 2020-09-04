package com.shreyaspatil.easyupipayment

import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.shreyaspatil.easyupipayment.model.PaymentApp
import com.shreyaspatil.easyupipayment.testutils.*
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EasyUpiPaymentTest {

	@Test
	fun `Should create instance when all valid fields are passed to the builder`() {
		val easyUpiPayment = EasyUpiPayment(mock()) {
			validEasyUpiPaymentBuilderFixture(this)
		}

		assertNotNull(easyUpiPayment)
	}

	@Test(expected = IllegalStateException::class)
	fun `Should throw Exception when invalid payee VPA is passed to builder`() {
		EasyUpiPayment(mock()) {
			validEasyUpiPaymentBuilderFixture(this)
			payeeVpa = invalidVpa
		}
	}

	@Test(expected = IllegalStateException::class)
	fun `Should throw Exception when invalid payee name is passed to builder`() {
		EasyUpiPayment(mock()) {
			validEasyUpiPaymentBuilderFixture(this)
			payeeName = invalidName
		}
	}

	@Test(expected = IllegalStateException::class)
	fun `Should throw Exception when invalid transaction ID is passed to builder`() {
		EasyUpiPayment(mock()) {
			validEasyUpiPaymentBuilderFixture(this)
			transactionId = invalidTransactionId
		}
	}

	@Test(expected = IllegalStateException::class)
	fun `Should throw Exception when invalid transaction reference ID is passed to builder`() {
		EasyUpiPayment(mock()) {
			validEasyUpiPaymentBuilderFixture(this)
			transactionRefId = invalidTransactionId
		}
	}

	@Test(expected = IllegalStateException::class)
	fun `Should throw Exception when invalid description is passed to builder`() {
		EasyUpiPayment(mock()) {
			validEasyUpiPaymentBuilderFixture(this)
			description = invalidDescription
		}
	}

	@Test(expected = IllegalStateException::class)
	fun `Should throw Exception when negative amount is passed to builder`() {
		EasyUpiPayment(mock()) {
			validEasyUpiPaymentBuilderFixture(this)
			amount = invalidAmount1
		}
	}

	@Test(expected = IllegalStateException::class)
	fun `Should throw Exception when only integral amount is passed to builder`() {
		EasyUpiPayment(mock()) {
			validEasyUpiPaymentBuilderFixture(this)
			amount = invalidAmount2
		}
	}

	@Test(expected = IllegalStateException::class)
	fun `Should throw Exception when only decimal part of amount is passed to builder`() {
		EasyUpiPayment(mock()) {
			validEasyUpiPaymentBuilderFixture(this)
			amount = invalidAmount3
		}
	}

	@Test
	fun `Should check application installed or not when payment application is provided`() {
		val activity: Activity = mock()
		val packageManager: PackageManager = mock()

		`when`(activity.packageManager).thenReturn(packageManager)
		`when`(packageManager.getPackageInfo("net.one97.paytm", 0)).thenAnswer { }


		val builder = EasyUpiPayment.Builder(activity)
			.with(PaymentApp.PAYTM)
			.apply {
				validEasyUpiPaymentBuilderFixture(this)
			}

		val spyBuilder = spy(builder)
		spyBuilder.build()

		verify(spyBuilder).isPackageInstalled("net.one97.paytm")
	}

	@Test
	fun `Should throw exception when payment application is provided and not available`() {
		val activity: Activity = mock()
		val packageManager: PackageManager = mock()

		`when`(activity.packageManager).thenReturn(packageManager)
		`when`(packageManager.getPackageInfo("net.one97.paytm", 0)).thenThrow(PackageManager.NameNotFoundException::class.java)


		EasyUpiPayment(activity) {
			paymentApp = PaymentApp.PAYTM
			apply {
				validEasyUpiPaymentBuilderFixture(this)
			}
		}
	}

	@Test
	fun `Should set and remove listener in Singleton when payment status listener is set and removed`() {
		val listener = paymentStatusListener()
		val easyUpiPayment = EasyUpiPayment(mock()) {
			apply { validEasyUpiPaymentBuilderFixture(this) }
		}

		// Set listener
		easyUpiPayment.setPaymentStatusListener(listener)

		// Check if it's set or not
		assertNotNull(Singleton.listener)
		assertThat(Singleton.listener, equalTo(listener))

		// Remove listener
		easyUpiPayment.removePaymentStatusListener()

		// Check if it's removed or not
		assertNull(Singleton.listener)
	}

	@Test
	fun `Should register lifecycle observer when instance is initialized with AppCompatActivity`() {
		val activity: AppCompatActivity = mock()
		val lifecycle: Lifecycle = mock()

		`when`(activity.lifecycle).thenReturn(lifecycle)

		val easyUpiPayment = EasyUpiPayment(activity) {
			apply { validEasyUpiPaymentBuilderFixture(this) }
		}

		verify(lifecycle).addObserver(easyUpiPayment.activityLifecycleObserver)
	}

	@Test
	fun `Should not register lifecycle observer when instance is initialized with Activity`() {
		val activity: Activity = mock()

		EasyUpiPayment(activity) { apply { validEasyUpiPaymentBuilderFixture(this) } }

		verifyNoInteractions(activity)
	}
}
