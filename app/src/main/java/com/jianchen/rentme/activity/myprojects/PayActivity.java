package com.jianchen.rentme.activity.myprojects;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jianchen.rentme.R;
import com.jianchen.rentme.helper.Constants;
import com.jianchen.rentme.helper.delegator.OnPaidListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

/**
 * Created by emerald on 6/20/2017.
 */
public class PayActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnPay;
    private EditText editAmount;

    private PayPalConfiguration payConfig;

    private int amount;
    private static OnPaidListener listener;

    public static void navigate(AppCompatActivity activity, OnPaidListener l, int amount) {
        listener = l;
        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra("AMOUNT", amount);
        ActivityCompat.startActivity(activity, intent, null);
    }

    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_payment);

        btnPay = (Button)findViewById(R.id.btn_payment_pay);
        btnPay.setOnClickListener(this);

        editAmount = (EditText)findViewById(R.id.edit_payment_amount);

        amount = getIntent().getIntExtra("AMOUNT", -1);
        if (amount != -1) {
            editAmount.setText(Integer.toString(amount));
            editAmount.setEnabled(false);
        }

        payConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(Constants.PAYPAL_CLIENT_ID);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payConfig);

        startService(intent);
    }

    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void doPayment() {
        String am;
        if (!editAmount.isEnabled())
            am = Integer.toString(amount);
        else
            am = editAmount.getText().toString();

        if (am.equals(""))
            return;

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(am)), "USD", "Simplified Coding Fee", PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payConfig);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, Constants.PAYPAL_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == Constants.PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        /*
                        startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount));
                        */
                        if (listener != null)
                            listener.onPaid(paymentDetails);
                        finish();
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_payment_pay:
                doPayment();
                break;
        }
    }
}
