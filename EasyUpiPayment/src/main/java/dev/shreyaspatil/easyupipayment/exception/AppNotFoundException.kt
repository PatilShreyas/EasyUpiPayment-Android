package dev.shreyaspatil.easyupipayment.exception

class AppNotFoundException(appPackage: String?) : Exception("""
    No UPI app${appPackage?.let { " with package name '$it'" } ?: ""} exists on this device to perform this transaction.
""".trimIndent())