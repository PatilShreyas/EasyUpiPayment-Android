package com.example.easyupipayment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

public class MainActivity extends AppCompatActivity implements PaymentStatusListener {

    private ImageView imageView;
    private TextView statusView;
    private TextView emailView;
    private TextView payNameView;
    private TextView transactionIdView;
    private TextView transactionRefIdView;
    private TextView descriptionOrNoteView;
    private TextView amountView;
    private Button payButton;
    EasyUpiPayment easyUpiPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Components
        initComponents();

        //Proceed for Payment on click
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPaymentEvent();
            }
        });
    }

    private void startPaymentEvent() {
        try {
            //Create instance of EasyUpiPayment
            easyUpiPayment = new EasyUpiPayment.Builder()
                    .with(this)
                    .setPayeeVpa(emailView.getText().toString())
                    .setPayeeName(payNameView.getText().toString())
                    .setTransactionId(transactionIdView.getText().toString())
                    .setTransactionRefId(transactionRefIdView.getText().toString())
                    .setDescription(descriptionOrNoteView.getText().toString())
                    .setAmount(amountView.getText().toString())
                    .build();

            //Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this);
            easyUpiPayment.startPayment();
        }catch (RuntimeException e){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Erro Campo invalido")
                    .setMessage(e.getMessage())
                    .setPositiveButton("Correct",null)
                    .show();
            Log.e("Erro: ", e.getMessage() );
        }

    }

    private void initComponents() {
        imageView = findViewById(R.id.imageView);
        statusView = findViewById(R.id.textView_status);
        emailView = findViewById(R.id.txt_payment_email);
        payNameView = findViewById(R.id.txt_payment_name);
        transactionIdView = findViewById(R.id.txt_payment_transaction_id);
        transactionRefIdView = findViewById(R.id.txt_payment_transaction_ref_id);
        descriptionOrNoteView = findViewById(R.id.txt_payment_description);
        amountView = findViewById(R.id.txt_payment_amount);
        payButton = findViewById(R.id.button_pay);
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString());
        statusView.setText(transactionDetails.toString());
    }

    @Override
    public void onTransactionSuccess() {
        // Payment Success
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        imageView.setImageResource(R.drawable.ic_success);
    }

    @Override
    public void onTransactionSubmitted() {
        // Payment Pending
        Toast.makeText(this, "Pending | Submitted", Toast.LENGTH_SHORT).show();
        imageView.setImageResource(R.drawable.ic_success);
    }

    @Override
    public void onTransactionFailed() {
        // Payment Failed
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        imageView.setImageResource(R.drawable.ic_failed);
    }

    @Override
    public void onTransactionCancelled() {
        // Payment Cancelled by User
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        imageView.setImageResource(R.drawable.ic_failed);
    }
}
