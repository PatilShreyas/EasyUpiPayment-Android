package com.example.easyupipayment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

public class MainActivity extends AppCompatActivity implements PaymentStatusListener {

    private ImageView imageView;
    private Button payButton;
    private MaterialEditText amount, upiid, note, payee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Components
        imageView = findViewById(R.id.imageView);
        amount = findViewById(R.id.amount);
        upiid = findViewById(R.id.UpiId);
        note = findViewById(R.id.Note);
        payee = findViewById(R.id.PayeeName);
        payButton = findViewById(R.id.button_pay);

        //Proceed for Payment on click
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String AMOUNT = amount.getText().toString();
                final String UPI = upiid.getText().toString();
                final String NOTE = note.getText().toString();
                final String PayeeName = payee.getText().toString();

                //Create instance of EasyUpiPayment
                if(AMOUNT.isEmpty() || UPI.isEmpty() || NOTE.isEmpty() || PayeeName.isEmpty()){
                    Toast.makeText(MainActivity.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                }else {
                    double amt = Double.parseDouble(AMOUNT);
                    final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                            .with(MainActivity.this)
                            .setPayeeVpa(UPI)
                            .setDescription(NOTE)
                            .setAmount(String.valueOf(amt))
                            .setPayeeName(PayeeName)
                            .setTransactionId("TRANSACTION_ID")
                            .setTransactionRefId("TRANSACTION_REF_ID")
                            .build();

                    //Register Listener for Events
                    easyUpiPayment.setPaymentStatusListener(MainActivity.this);

                    easyUpiPayment.startPayment();
                }
            }
        });
    }

    String TranscationDetails;

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString());
        TranscationDetails =  transactionDetails.toString();
    }

    @Override
    public void onTransactionSuccess() {
        // Payment Success
        new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.ic_success).setTitle("Transaction Successful")
                .setMessage(TranscationDetails)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }

    @Override
    public void onTransactionSubmitted() {
        // Payment Pending
        new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.ic_success).setTitle("Transaction Pending or Submitted")
                .setMessage(TranscationDetails)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }

    @Override
    public void onTransactionFailed() {
        // Payment Failed
        new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.ic_failed).setTitle("Transaction Failed")
                .setMessage(TranscationDetails)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }

    @Override
    public void onTransactionCancelled() {
        // Payment Cancelled by User
        new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.ic_failed).setTitle("Transaction Cancelled.")
                .setMessage(TranscationDetails)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }
}
