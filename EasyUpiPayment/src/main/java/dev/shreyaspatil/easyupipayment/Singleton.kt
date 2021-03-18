package dev.shreyaspatil.easyupipayment

import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener

internal object Singleton {
	@set:JvmSynthetic
	@get:JvmSynthetic
	internal var listener: PaymentStatusListener? = null
}