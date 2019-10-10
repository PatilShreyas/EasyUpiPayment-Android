package com.example.easyupipayment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity { // implements PaymentStatusListener {

    private EditText amount, note, name, upiID;
    private ImageView imageView;
    private TextView statusView;
    private Button payButton;

    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amounttxt=amount.getText().toString();
                String notetxt=note.getText().toString();
                String nametxt=name.getText().toString();
                String upiIDtxt=upiID.getText().toString();

                payUsingUPI(amounttxt,notetxt,nametxt,upiIDtxt);

            }
        });


    }
    private void payUsingUPI(String amounttxt,String noteTxt , String nametxt , String upiIDtxt){
        Uri uri = Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa",upiIDtxt)
                .appendQueryParameter("pn",nametxt)
                .appendQueryParameter("tn",noteTxt)
                .appendQueryParameter("am",amounttxt)
                .appendQueryParameter("cu","INR").build();

        Intent upi_payment = new Intent(Intent.ACTION_VIEW);
        upi_payment.setData(uri);
        Intent chooser = Intent.createChooser(upi_payment,"Pay with");
        if(null!=chooser.resolveActivity(getPackageManager())){
            startActivityForResult(chooser,UPI_PAYMENT);

        }
        else{
            Toast.makeText(this,"NO UPI APP FOUND",Toast.LENGTH_SHORT).show();
        }


    }

    private void initializeComponents() {
        payButton = findViewById(R.id.button_pay);
        name = findViewById(R.id.name);
        note=findViewById(R.id.note);
        amount= findViewById(R.id.amount);
        upiID=findViewById(R.id.upi_id);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case UPI_PAYMENT:
                if(RESULT_OK==resultCode || (resultCode==11)){
                    if(data!=null){
                        String txt = data.getStringExtra("response");
                        Log.d("UPI","OnActivityResult:" +txt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("Nothing");
                        upipaymentDataOperation(dataList);

                    }
                    else{
                        Log.d("UPI", "OnAtivityResult:"+ "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("Nothing");
                        upipaymentDataOperation(dataList);
                    }
                }
                else{
                    Log.d("UPI", "OnAtivityResult:"+ "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("Nothing");
                    upipaymentDataOperation(dataList);

                }
                break;
        }
    }

    private void upipaymentDataOperation(ArrayList<String> data) {
        if(isConnectionAvailable(MainActivity.this)){
            String str = data.get(0);
            Log.d("UPIPAY" ,"upipaymentoption "+ str);
            String paymentCancel = "";
            if(str==null)str ="discard";
            String status ="";
            String aprovalref="";
            String response[] = str.split("&");
            for(int i=0;i<response.length;i++){
                String equalStr[] = response[i].split("=");
                if(equalStr.length >=2){
                    if(equalStr[0].toLowerCase().equals("Status".toLowerCase())){
                        status=equalStr[1].toLowerCase();
                    }
                else if(equalStr[0].toLowerCase().equals("approval Ref".toLowerCase())||
                    equalStr[0].toLowerCase().equals("txnRef".toLowerCase())){
                    aprovalref=equalStr[1];

                    }
                }
                else{
                    paymentCancel = "Payment Cancel by User";
                    if(status.equals("success")){
                        Toast.makeText(MainActivity.this,"Trancation Success",Toast.LENGTH_LONG).show();
                        imageView.setImageResource(R.drawable.ic_success);
                    Log.d("UPI" ,"responsestr:" + aprovalref);
                    }
                    else if("Payment Cancel by User".equals(paymentCancel)) {
                        Toast.makeText(MainActivity.this,"Payment Cancel by User",Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.ic_failed);
                    }
                    else{
                        Toast.makeText(MainActivity.this,"Trancation Failed" , Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.ic_failed);
                    }
                }


            }


        }

    }

    private boolean isConnectionAvailable(MainActivity context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null){
            NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();
            if(networkInfo!= null && networkInfo.isConnected()&&networkInfo.isConnectedOrConnecting()&& networkInfo.isAvailable()){
                return true;
            }


        }
        return false;
    }
}
