package com.shreyaspatil.easyupipayment.model

@Suppress("SpellCheckingInspection")
enum class PaymentApp(val packageName: String) {
	ALL(Package.ALL),
	AMAZON_PAY(Package.AMAZON_PAY),
	BHIM_UPI(Package.BHIM_UPI),
	GOOGLE_PAY(Package.GOOGLE_PAY),
	PAYTM(Package.PAYTM),
	PHONE_PE(Package.PHONE_PE);

	private object Package {
		const val ALL = "ALL"
		const val AMAZON_PAY = "in.amazon.mShop.android.shopping"
		const val BHIM_UPI = "in.org.npci.upiapp"
		const val GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user"
		const val PHONE_PE = "com.phonepe.app"
		const val PAYTM = "net.one97.paytm"
	}
}