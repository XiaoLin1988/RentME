package com.android.jianchen.rentme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.jianchen.rentme.Task.APIRequester;
import com.android.jianchen.rentme.Utils.Constants;
import com.android.jianchen.rentme.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by emerald on 6/13/2017.
 */
public class ForgotActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
    private String code;

    private EditText editEmail;
    private Button btnForgot;

    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_forgot);

        initVariables();
        initViewVariables();
    }

    private void initViewVariables() {
        editEmail = (EditText)findViewById(R.id.edit_forgot_email);

        btnForgot = (Button)findViewById(R.id.btn_forgot_request);
        btnForgot.setOnClickListener(this);
    }

    private void initVariables() {
        code = "";
    }

    private void sendForgotRequest(String email) {
        String url = Constants.API_ROOT_URL + Constants.API_USER_FORGOT;
        code = Utils.getSaltString();
        code = code.substring(3, 9);
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("code", code);

        APIRequester requester = new APIRequester(Request.Method.POST, url, params, this, this);
        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forgot_request:
                if (editEmail.getText().toString().equals("")) {
                    Toast.makeText(ForgotActivity.this, "Please input your email", Toast.LENGTH_LONG).show();
                }

                sendForgotRequest(editEmail.getText().toString());
                break;
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            if (response.getBoolean("status"))
                Toast.makeText(ForgotActivity.this, "Please check your email", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(ForgotActivity.this, "User not exists", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
