# Getting transaction status

You'll need to register for callback events to get transaction status or details.

`PaymentStatusListener` needs to be set with `EasyUpiPayment` before initiating payment.

## Payment Status Listener callback methods

- `onTransactionCompleted(TransactionDetails)` - Invoked when transaction is over. You'll get `TransactionDetails` instance which will include transaction details. See [this](pages/transaction-details) for more information.
- `onTransactionCancelled()` - Invoked when transaction is cancelled by the user.

<!-- tabs:start -->

#### ** Kotlin **

```kotlin
override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
    Log.d("TransactionDetails", transactionDetails.toString())
}

override fun onTransactionCancelled() {
    toast("Cancelled by user")
}
```

#### ** Java **

```java
@Override
public void onTransactionCompleted(TransactionDetails transactionDetails) {
    Log.d("TransactionDetails", transactionDetails.toString());
    }
}

@Override
public void onTransactionCancelled() {
    toast("Cancelled by user");
}
```

<!-- tabs:end -->

## Set payment status listener

<!-- tabs:start -->

#### ** Kotlin **

```kotlin
easyUpiPayment.setPaymentStatusListener(this)
```

#### ** Java **

```java
easyUpiPayment.setPaymentStatusListener(this);
```

<!-- tabs:end -->

?> Here `this` is passed to `setPaymentStatusListener()` means it's implemented in current `Activity` class.

## Remove payment status listener (Optional)

If you've implemented _EasyUpiPayment_ in `AppCompatActivity` then you won't need to remove listener manually. It's handled internally by this library. But if you are still using `Activity` then you can remove listener in `onDestroy()` lifecycle method.

<!-- tabs:start -->

#### ** Kotlin **

```kotlin
	override fun onDestroy() {
		super.onDestroy()
		easyUpiPayment.removePaymentStatusListener()
	}
```

#### ** Java **

```java
    @Override
    protected void onDestroy() {
        super.onDestroy();
        easyUpiPayment.removePaymentStatusListener();
    }
```

<!-- tabs:end -->
