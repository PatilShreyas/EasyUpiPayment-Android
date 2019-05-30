package com.shreyaspatil.EasyUpiPayment.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.shreyaspatil.EasyUpiPayment.R;
import com.shreyaspatil.EasyUpiPayment.Singleton;
import com.shreyaspatil.EasyUpiPayment.model.Payment;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
import java.util.HashMap;
import java.util.Map;


public final class PaymentUiActivity extends AppCompatActivity {
    private static final String TAG = "PaymentUiActivity";
    private static final int PAYMENT_REQUEST = 4400;
    private Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upipay);

        singleton = Singleton.getInstance();

        //Get Payment Information
        Intent intent = getIntent();
        Payment payment = (Payment) intent.getSerializableExtra("payment");

        // Set Parameters for UPI
        Uri.Builder payUri = new Uri.Builder();

        payUri.scheme("upi").authority("pay");
        payUri.appendQueryParameter("pa", payment.getVpa());
        payUri.appendQueryParameter("pn", payment.getName());

        if(payment.getPayeeMerchantCode() != null) {
            payUri.appendQueryParameter("mc", payment.getPayeeMerchantCode());
        }
        if(payment.getTxnId() != null) {
            payUri.appendQueryParameter("tid", payment.getTxnId());
        }
        if(payment.getTxnRefId() != null) {
            payUri.appendQueryParameter("tr", payment.getTxnRefId());
        }

        payUri.appendQueryParameter("tn", payment.getDescription());
        payUri.appendQueryParameter("am", payment.getAmount());
        payUri.appendQueryParameter("cu", "INR");

        Uri uri = payUri.build();

        // Set Data Intent
        Intent paymentIntent = new Intent(Intent.ACTION_VIEW);
        paymentIntent.setData(uri);

        // Show Dialog to user
        Intent appChooser = Intent.createChooser(paymentIntent, "Pay");

        // Check if app is installed or not
        if(appChooser.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(appChooser, PAYMENT_REQUEST);
        } else {
            Toast.makeText(this,"No UPI app found! Please Install to Proceed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PAYMENT_REQUEST) {
            if ((resultCode == RESULT_OK) || (resultCode == 11)) {
                if (data != null) {
                    String response = data.getStringExtra("response");
                    TransactionDetails transactionDetails = getTransactionDetails(response);

                    //Update Listener onTransactionCompleted()
                    singleton.getListener().onTransactionCompleted(transactionDetails);

                    //Check if success or failed
                    if (transactionDetails.getStatus().toLowerCase().equals("success")) {
                        singleton.getListener().onTransactionSuccess();
                    } else {
                        singleton.getListener().onTransactionFailed();
                    }
                } else {
                    Log.e(TAG, "Data is null");
                    singleton.getListener().onTransactionFailed();
                }
            } else {
                Log.e(TAG, "Transaction Cancelled by User");
                singleton.getListener().onTransactionCancelled();
            }
            finish();
        }
    }

    private Map<String, String> getQueryString(String url) {
        String[] params = url.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    //Make TransactionDetails object from response string
    private TransactionDetails getTransactionDetails(String response) {
        TransactionDetails transactionDetails = new TransactionDetails();
        Map<String, String> map = getQueryString(response);

        transactionDetails.setTransactionId(map.get("txnId"));
        transactionDetails.setResponseCode(map.get("responseCode"));
        transactionDetails.setApprovalRefNo(map.get("ApprovalRefNo"));
        transactionDetails.setStatus(map.get("Status"));
        transactionDetails.setTransactionRefId(map.get("txnRef"));

        return transactionDetails;
    }


}
