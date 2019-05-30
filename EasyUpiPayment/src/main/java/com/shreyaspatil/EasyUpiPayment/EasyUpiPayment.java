package com.shreyaspatil.EasyUpiPayment;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.Payment;
import com.shreyaspatil.EasyUpiPayment.ui.PaymentUiActivity;

/**
 * Class to implement Payment with UPI.
 *
 * Use {@link Builder} to create a new instance.
 */
public final class EasyUpiPayment {
    private Activity mActivity;
    private Payment mPayment;

    private EasyUpiPayment(@NonNull final Activity mActivity, @NonNull Payment mPayment) {
        this.mActivity = mActivity;
        this.mPayment = mPayment;
    }

    public void startPayment() {
        Intent payIntent  = new Intent(mActivity, PaymentUiActivity.class);
        payIntent.putExtra("payment", mPayment);
        mActivity.startActivity(payIntent);
    }

    public void setPaymentStatusListener(@NonNull PaymentStatusListener mListener) {
        Singleton singleton = Singleton.getInstance();
        singleton.setListener(mListener);
    }

    /**
     * Builder for {@link EasyUpiPayment}.
     */
    public static final class Builder {
        private Activity activity;
        private Payment payment;

        @NonNull
        public Builder with(@NonNull Activity activity) {
            this.activity = activity;
            payment = new Payment();
            return this;
        }

        @NonNull
        public Builder setPayeeVpa(@NonNull String vpa) {
            payment.setVpa(vpa);
            return this;
        }

        @NonNull
        public Builder setPayeeName(@NonNull String name) {
            payment.setName(name);
            return this;
        }

        @NonNull
        public Builder setPayeeMerchantCode(@NonNull String merchantCode) {
            payment.setPayeeMerchantCode(merchantCode);
            return this;
        }

        @NonNull
        public Builder setTransactionId(@NonNull String id) {
            payment.setTxnRefId(id);
            return this;
        }

        @NonNull
        public Builder setTransactionRefId(@NonNull String refId) {
            payment.setTxnRefId(refId);
            return this;
        }

        @NonNull
        public Builder setDescription(@NonNull String description) {
            payment.setDescription(description);
            return this;
        }

        @NonNull
        public Builder setAmount(@NonNull String amount) {
            if (!amount.contains(".")) {
                throw new IllegalStateException("Amount should be in decimal format XX.XX");
            }

            payment.setAmount(amount);
            return this;
        }

        @NonNull
        public EasyUpiPayment build() {
            if(activity == null) {
                throw new IllegalStateException("Activity must be specified using with() call.");
            }

            if (payment == null) {
                throw new IllegalStateException("Payment Details must be initialized!");
            }

            if (payment.getVpa() == null) {
                throw new IllegalStateException("Must call setPayeeVpa() before build().");
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
