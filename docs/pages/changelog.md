# Changelog

You can see [GitHub releases](https://github.com/PatilShreyas/EasyUpiPayment-Android/releases) where this is officially released.

---

## *v3.0.0* (2020-09-XX)

- Moved to [symantic version](https://semver.org/) from this release.
- // TODO add more info

## *v2.2* (2020-05-06)

**Features of this release:**

[[#34](https://github.com/PatilShreyas/EasyUpiPayment-Android/issues/34)] - Getting transaction amount from `TransactionDetails` instance.
When the transaction is completed then you can retrieve transaction amount as below.
```java
    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        transactionDetails.getAmount()
    }
```

[[#35](https://github.com/PatilShreyas/EasyUpiPayment-Android/issues/35)] - Implemented system default app chooser to select the UPI app to pay with. In previous versions, it's using custom chooser implemented with `BottomSheetDialog`.

## *v2.1* (2020-04-06)

This version fixes

- App crashing when paying using BHIM UPI app ([#21](https://github.com/PatilShreyas/EasyUpiPayment-Android/issues/21)).

## *v2.0* (2019-12-09)

This is a major release containing new features and bug fixes.

**New features:**

- Added support for app-specific payment ([#13](https://github.com/PatilShreyas/EasyUpiPayment-Android/issues/13)).
Now, payment can be done with a specific app like BHIM UPI, PhonePe, AmazonPe, PayTM, etc.
- Added Sample Demo App ([#8](https://github.com/PatilShreyas/EasyUpiPayment-Android/issues/8)).

## *v1.1* (2019-07-15)

This Release fixes a few bugs and has UI and other minor improvements.

## *v1.0* (2019-06-03)

This is a stable release with some fixes.

## *v0.1-beta* (2019-5-30)

This is the first release.
