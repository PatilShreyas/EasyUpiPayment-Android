package com.example.easyupipayment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

public class MainActivity extends AppCompatActivity implements PaymentStatusListener {

    private ImageView imageView;
    private TextView statusView;
    private Button payButton;
    private EditText Vpa,Pname,Tid,Trefid,Desc,Amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Components
        imageView = findViewById(R.id.imageView);
        statusView = findViewById(R.id.textView_status);
        payButton = findViewById(R.id.button_pay);
        Vpa=findViewById(R.id.edittext_vpa);
        Pname=findViewById(R.id.edittext_pname);
        Tid=findViewById(R.id.edittext_tid);
        Trefid=findViewById(R.id.edittext_trefid);
        Desc=findViewById(R.id.edittext_desc);
        Amt=findViewById(R.id.edittext_amt);
        int vn = Integer.parseInt(Vpa.getText().toString());
        //Create instance of EasyUpiPayment
        @SuppressLint("ResourceType") final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                .with(this)
                .setPayeeVpa(Vpa.getText().toString())
                .setPayeeName(Pname.getText().toString())
                .setTransactionId(Tid.getText().toString())
                .setTransactionRefId(Trefid.getText().toString())
                .setDescription(Desc.getText().toString())
                .setAmount(Amt.getText().toString())
                .build();

        //Register Listener for Events
        easyUpiPayment.setPaymentStatusListener(this);

        //Proceed for Payment on click
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyUpiPayment.startPayment();
            }
        });
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
