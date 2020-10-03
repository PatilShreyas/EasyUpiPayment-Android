# Paying with Specific App (Optional)

If you ***don't want to give user a choice*** to pay using one of the multiple available UPI apps then you can set specific payment app for transaction.

?> In such cases, payment will only be possible if that specific app is installed on user's device.

### Set default app for payment

- You'll have to set default app while initializing `EasyUpiPayment`.
- Here are UPI apps available currently in *EasyUpiPayment*. You'll need to provide one among the following defined constants to pay with.

#### UPI Apps Enum constants

- `PaymentApp.ALL` *(Default) - Choice will be displayed*.
- `PaymentApp.AMAZON_PAY`
- `PaymentApp.BHIM_UPI`
- `PaymentApp.GOOGLE_PAY`
- `PaymentApp.PAYTM`
- `PaymentApp.PHONE_PE`


For e.g. If you want to pay with [BHIM UPI](https://www.bhimupi.org.in/) only, then code will look like...

<!-- tabs:start -->

#### ** Kotlin **

```kotlin
val easyUpiPayment = EasyUpiPayment(this) {
    paymentApp = PaymentApp.BHIM_UPI

    // Other fields initialization
}
```

#### ** Java **

```java
EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(this)
        .with(PaymentApp.BHIM_UPI)

        // Other fields initialization
```

<!-- tabs:end -->

!> If defined app is not installed in user's device then ***`AppNotFoundException`*** will be thrown.