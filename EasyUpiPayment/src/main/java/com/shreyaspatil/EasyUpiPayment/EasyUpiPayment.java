package com.shreyaspatil.EasyUpiPayment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.Payment;
import com.shreyaspatil.EasyUpiPayment.model.PaymentApp;
import com.shreyaspatil.EasyUpiPayment.ui.PaymentUiActivity;

/**
 * Class to implement Easy UPI Payment
 * Use {@link Builder} to create a new instance.
 */
public final class EasyUpiPayment {
    public static final String APP_NOT_FOUND = "AppNotFound";
    private Activity mActivity;
    private Payment mPayment;

    private EasyUpiPayment(@NonNull final Activity mActivity, @NonNull Payment mPayment) {
        this.mActivity = mActivity;
        this.mPayment = mPayment;
    }

    /**
     * Starts the payment Transaction. Calling this method launches the Payment Menu
     * and shows installed UPI apps in device and let user choose one of them to pay.
     */
    public void startPayment() {
        // Check whether default package exists
        if (mPayment.getDefaultPackage() != null) {
            // Check app existence
            boolean isInstalled = isPackageInstalled(mPayment.getDefaultPackage(),
                    mActivity.getPackageManager());

            // If app isn't exist, throw error and return
            if (!isInstalled) {
                Log.e(APP_NOT_FOUND, "UPI App with package '" + mPayment.getDefaultPackage() +
                        "' is not installed on this device.");

                // Listener Callback
                if (Singleton.getInstance().isListenerRegistered()) {
                    Singleton.getInstance().getListener().onAppNotFound();
                }
                return;
            }
        }

        // Create Payment Activity Intent
        Intent payIntent = new Intent(mActivity, PaymentUiActivity.class);
        payIntent.putExtra("payment", mPayment);

        // Start Payment Activity
        mActivity.startActivity(payIntent);
    }

    /**
     * Sets the PaymentStatusListener.
     *
     * @param mListener Implementation of PaymentStatusListener
     */
    public void setPaymentStatusListener(@NonNull PaymentStatusListener mListener) {
        Singleton singleton = Singleton.getInstance();
        singleton.setListener(mListener);
    }

    /**
     * Sets default payment app for payment transaction.
     *
     * @param mPaymentApp Sets default payment app from Enum of {@link PaymentApp}.
     *                    For e.g. To start payment with BHIM UPI then PaymentApp.BHIM_UPI
     *                    {@link PaymentApp#BHIM_UPI}.
     */
    public void setDefaultPaymentApp(@NonNull PaymentApp mPaymentApp) {
        boolean isInstalled = isPackageInstalled(mPaymentApp.getPackageName(),
                mActivity.getPackageManager());

        if (mPaymentApp == PaymentApp.NONE) {
            mPayment.setDefaultPackage(null);
            return;
        }
        // If app isn't exist, log error and return
        if (!isInstalled) {
            Log.e(APP_NOT_FOUND, "UPI App with package '" + mPayment.getDefaultPackage() +
                    "' is not installed on this device.");

            // Listener Callback
            if (Singleton.getInstance().isListenerRegistered()) {
                Singleton.getInstance().getListener().onAppNotFound();
            }
            // Set NONE package
            mPayment.setDefaultPackage(PaymentApp.NONE.getPackageName());

            return;
        }

        mPayment.setDefaultPackage(mPaymentApp.getPackageName());
    }

    /**
     * Removes the PaymentStatusListener which is already registered.
     */
    public void detachListener() {
        Singleton.getInstance().detachListener();
    }

    /**
     * Check Whether UPI App is installed on device or not
     *
     * @return true if app exists, otherwise false.
     */
    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Checks whether specified {@link EasyUpiPayment#setDefaultPaymentApp(PaymentApp)} default
     * app is exists on device or not. If not specified, returns false.
     *
     * @return true if app exists. Otherwise returns false.
     */
    public boolean isDefaultAppExist() {
        if (mPayment.getDefaultPackage() != null) {
            return !isPackageInstalled(mPayment.getDefaultPackage(), mActivity.getPackageManager());
        } else {
            Log.w("Unpecified", "Default app is not speified. Specify it using " +
                    "'setDefaultApp()' method of Builder class");
            return false;
        }
    }

    /**
     * Builder for {@link EasyUpiPayment}.
     */
    public static final class Builder {
        private Activity activity;
        private Payment payment;

        /**
         * Binds the Activity with Payment.
         *
         * @param activity where payment is to be implemented.
         * @return this, for chaining.
         */
        @NonNull
        public Builder with(@NonNull Activity activity) {
            this.activity = activity;
            payment = new Payment();
            return this;
        }

        /**
         * Sets the Payee VPA (e.g. example@vpa, 1234XXX@upi).
         *
         * @param vpa Payee VPA address (e.g. example@vpa, 1234XXX@upi).
         * @return this, for chaining.
         */
        @NonNull
        public Builder setPayeeVpa(@NonNull String vpa) {
            if (!vpa.contains("@")) {
                throw new IllegalStateException("Payee VPA address should be valid (For e.g. example@vpa)");
            }

            payment.setVpa(vpa);
            return this;
        }

        /**
         * Sets the Payee Name.
         *
         * @param name Payee Name.
         * @return this, for chaining.
         */
        @NonNull
        public Builder setPayeeName(@NonNull String name) {
            if (name.trim().length() == 0) {
                throw new IllegalStateException("Payee Name Should be Valid!");
            }
            payment.setName(name);
            return this;
        }

        /**
         * Sets the Merchant Code. If present it should be passed.
         *
         * @param merchantCode Payee Merchant code if present it should be passed.
         * @return this, for chaining.
         */
        @NonNull
        public Builder setPayeeMerchantCode(@NonNull String merchantCode) {
            if (merchantCode.trim().length() == 0) {
                throw new IllegalStateException("Merchant Code Should be Valid!");
            }
            payment.setPayeeMerchantCode(merchantCode);
            return this;
        }

        /**
         * Sets the Transaction ID. This field is used in Merchant Payments generated by PSPs.
         *
         * @param id field is used in Merchant Payments generated by PSPs.
         * @return this, for chaining.
         */
        @NonNull
        public Builder setTransactionId(@NonNull String id) {
            if (id.trim().length() == 0) {
                throw new IllegalStateException("Transaction ID Should be Valid!");
            }
            payment.setTxnId(id);
            return this;
        }

        /**
         * Sets the Transaction Reference ID. Transaction reference ID. This could be order number,
         * subscription number, Bill ID, booking ID, insurance renewal reference, etc.
         * Needed for merchant transactions and dynamic URL generation.
         *
         * @param refId field is used in Merchant Payments generated by PSPs.
         * @return this, for chaining.
         */
        @NonNull
        public Builder setTransactionRefId(@NonNull String refId) {
            if (refId.trim().length() == 0) {
                throw new IllegalStateException("RefId Should be Valid!");
            }
            payment.setTxnRefId(refId);
            return this;
        }

        /**
         * Sets the Description. It have to provide valid small note or description about payment.
         * for e.g. For Food
         *
         * @param description field have to provide valid small note or description about payment.
         *                    for e.g. For Food, For Payment at Shop XYZ
         * @return this, for chaining.
         */
        @NonNull
        public Builder setDescription(@NonNull String description) {
            if (description.trim().length() == 0) {
                throw new IllegalStateException("Description Should be Valid!");
            }

            payment.setDescription(description);
            return this;
        }

        /**
         * Sets the Amount in INR. (Format should be decimal e.g. 14.88)
         *
         * @param amount field takes amount in String decimal format (xx.xx) to be paid.
         *               For e.g. 90.88 will pay Rs. 90.88.
         * @return this, for chaining.
         */
        @NonNull
        public Builder setAmount(@NonNull String amount) {
            if (!amount.contains(".")) {
                throw new IllegalStateException("Amount should be in decimal format XX.XX (For e.g. 100.00)");
            }

            payment.setAmount(amount);
            return this;
        }

        /**
         * Build the {@link EasyUpiPayment} object.
         */
        @NonNull
        public EasyUpiPayment build() {
            if (activity == null) {
                throw new IllegalStateException("Activity must be specified using with() call begore build()");
            }

            if (payment == null) {
                throw new IllegalStateException("Payment Details must be initialized before build()");
            }

            if (payment.getVpa() == null) {
                throw new IllegalStateException("Must call setPayeeVpa() before build().");
            }

            if (payment.getTxnId() == null) {
                throw new IllegalStateException("Must call setTransactionId() before build");
            }

            if (payment.getTxnRefId() == null) {
                throw new IllegalStateException("Must call setTransactionRefId() before build");
            }

            if (payment.getName() == null) {
                throw new IllegalStateException("Must call setPayeeName() before build().");
            }

            if (payment.getAmount() == null) {
                throw new IllegalStateException("Must call setAmount() before build().");
            }

            if (payment.getDescription() == null) {
                throw new IllegalStateException("Must call setDescription() before build().");
            }

            return new EasyUpiPayment(activity, payment);
        }
    }
}
