package com.shreyaspatil.EasyUpiPayment;

import androidx.annotation.NonNull;

import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;


public final class Singleton {
    private static Singleton instance = null;

    private PaymentStatusListener listener;

    public static Singleton getInstance() {
        if(instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    @NonNull
    public PaymentStatusListener getListener() {
        return listener;
    }

    public void setListener(@NonNull PaymentStatusListener listener) {
        this.listener = listener;
    }

    public boolean isListenerRegistered() {
        return (listener != null);
    }
}
