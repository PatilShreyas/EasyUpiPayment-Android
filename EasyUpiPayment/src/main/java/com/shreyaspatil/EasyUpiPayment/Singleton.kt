package com.shreyaspatil.easyupipayment

import com.shreyaspatil.easyupipayment.listener.PaymentStatusListener

internal object Singleton {
    @set:JvmSynthetic
    @get:JvmSynthetic
    internal var listener: PaymentStatusListener? = null
}