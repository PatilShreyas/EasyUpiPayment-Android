package com.shreyaspatil.EasyUpiPayment.ui;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shreyaspatil.EasyUpiPayment.R;
import com.shreyaspatil.EasyUpiPayment.Singleton;
import com.shreyaspatil.EasyUpiPayment.model.Payment;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shreyaspatil.EasyUpiPayment.EasyUpiPayment.APP_NOT_FOUND;

public final class PaymentUiActivity extends AppCompatActivity {
    private static final String TAG = "PaymentUiActivity";
    public static final int PAYMENT_REQUEST = 4400;
    private Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upipay);

        // Get instance of Singleton class
        singleton = Singleton.getInstance();

        //Get Payment Information
        Intent intent = getIntent();
        Payment payment = (Payment) intent.getSerializableExtra("payment");

        // Set Parameters for UPI
        Uri.Builder payUri = new Uri.Builder();

        payUri.scheme("upi").authority("pay");
        payUri.appendQueryParameter("pa", payment.getVpa());
        payUri.appendQueryParameter("pn", payment.getName());
        payUri.appendQueryParameter("tid", payment.getTxnId());

        if(payment.getPayeeMerchantCode() != null) {
            payUri.appendQueryParameter("mc", payment.getPayeeMerchantCode());
        }

        payUri.appendQueryParameter("tr", payment.getTxnRefId());
        payUri.appendQueryParameter("tn", payment.getDescription());
        payUri.appendQueryParameter("am", payment.getAmount());
        payUri.appendQueryParameter("cu", payment.getCurrency());

        //Build URI
        Uri uri = payUri.build();

        // Set Data Intent
        Intent paymentIntent = new Intent(Intent.ACTION_VIEW);
        paymentIntent.setData(uri);

        // Check for Default package
        if (payment.getDefaultPackage() != null) {
            paymentIntent.setPackage(payment.getDefaultPackage());
            //startActivityForResult(intent, PAYMENT_REQUEST);
        }

        // Check if other UPI apps are exists or not.
        if(paymentIntent.resolveActivity(getPackageManager()) != null) {
            List<ResolveInfo> intentList = getPackageManager()
                    .queryIntentActivities(paymentIntent, 0);
            showApps(intentList, paymentIntent);
        } else {
            Toast.makeText(this, "No UPI app found! Please Install to Proceed!",
                    Toast.LENGTH_SHORT).show();
            callbackOnAppNotFound();
        }
    }

    private void showApps(List<ResolveInfo> appsList, final Intent intent) {
        //Listener to know about cancellation of payment
        View.OnClickListener onCancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackTransactionCancelled();
                finish();
            }
        };

        AppsBottomSheet appsBottomSheet = new AppsBottomSheet(appsList, intent, onCancelListener);
        appsBottomSheet.show(getSupportFragmentManager(), "Pay Using");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PAYMENT_REQUEST) {
            if (data != null) {
                // Get Response from activity intent
                String response = data.getStringExtra("response");

                if(response == null) {
                    callbackTransactionCancelled();
                    Log.d(TAG, "Response is null");

                } else {

                    TransactionDetails transactionDetails = getTransactionDetails(response);

                    //Update Listener onTransactionCompleted()
                    callbackTransactionComplete(transactionDetails);

                    //Check if success, submitted or failed
                    try {
                        if (transactionDetails.getStatus().toLowerCase().equals("success")) {
                            callbackTransactionSuccess();
                        } else if (transactionDetails.getStatus().toLowerCase().equals("submitted")) {
                            callbackTransactionSubmitted();
                        } else {
                            callbackTransactionFailed();
                        }
                    } catch (Exception e) {
                        callbackTransactionCancelled();
                        callbackTransactionFailed();
                    }
                }
            } else {
                Log.e(TAG, "Intent Data is null. User cancelled");
                callbackTransactionCancelled();
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

    // Make TransactionDetails object from response string
    private TransactionDetails getTransactionDetails(String response) {
        Map<String, String> map = getQueryString(response);

        String transactionId = map.get("txnId");
        String responseCode = map.get("responseCode");
        String approvalRefNo = map.get("ApprovalRefNo");
        String status = map.get("Status");
        String transactionRefId = map.get("txnRef");

        return new TransactionDetails(
                transactionId,
                responseCode,
                approvalRefNo,
                status,
                transactionRefId
        );
    }

    // Checks whether listener is registered
    private boolean isListenerRegistered() {
        return (Singleton.getInstance().isListenerRegistered());
    }

    private void callbackOnAppNotFound() {
        Log.e(APP_NOT_FOUND, "No UPI app found on device.");

        if (isListenerRegistered()) {
            singleton.getListener().onAppNotFound();
        }
        finish();
    }

    private void callbackTransactionSuccess() {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionSuccess();
        }
    }

    private void callbackTransactionSubmitted() {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionSubmitted();
        }
    }

    private void callbackTransactionFailed() {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionFailed();
        }
    }

    private void callbackTransactionCancelled() {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionCancelled();
        }
    }

    private void callbackTransactionComplete(TransactionDetails transactionDetails) {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionCompleted(transactionDetails);
        }
    }
}
