package com.example.easyupipayment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

public class MainActivity extends AppCompatActivity implements PaymentStatusListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create instance of EasyUpiPayment
        EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                .with(this)
                .setPayeeVpa("9420788384@upi")
                .setPayeeName("Sharad Shankar Patil")
                .setDescription("Just Testing API!")
                .setAmount("10.00")
                .build();

        //Register Listener for Events
        easyUpiPayment.setPaymentStatusListener(this);

        //Proceed for Payment
        easyUpiPayment.startPayment();
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString());
    }

    @Override
    public void onTransactionSuccess() {
        // Payment Success
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionFailed() {
        // Payment Failed
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCancelled() {
        // Payment Cancelled by User
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
    }
}
