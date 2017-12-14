package com.android.jianchen.rentme.activity.root;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jianchen.rentme.model.rentme.ObjectModel;
import com.android.jianchen.rentme.model.rentme.UserModel;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.helper.network.retrofit.RestClient;
import com.android.jianchen.rentme.helper.network.retrofit.UserClient;
import com.android.jianchen.rentme.helper.Constants;
import com.android.jianchen.rentme.helper.utils.DialogUtil;
import com.android.jianchen.rentme.helper.utils.Utils;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by emerald on 5/29/2017.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {
    private TextView txtForgot;
    private Button btnSignIn;
    private Button btnSignUp;

    private EditText editName;
    private EditText editPassword;

    @BindString(R.string.error_load)
    String errLoad;

    @BindString(R.string.error_network)
    String errNetwork;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        UserModel curUser = Utils.retrieveUserInfo(getApplicationContext());
        if (curUser != null) {
            curUser.setLoginMode(Constants.LOGINMODE_EMAIL);
            Utils.saveUserInfo(LoginActivity.this, curUser);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            initViewVariables();
        }
    }

    private void initViewVariables() {
        btnSignIn = (Button)findViewById(R.id.btn_login_signin);
        btnSignIn.setOnClickListener(this);

        btnSignUp = (Button)findViewById(R.id.btn_login_signup);
        btnSignUp.setOnClickListener(this);

        txtForgot = (TextView)findViewById(R.id.txt_login_forgot);
        txtForgot.setOnClickListener(this);

        editName = (EditText)findViewById(R.id.edit_login_username);
        editPassword = (EditText)findViewById(R.id.edit_login_password);
        editPassword.setTypeface(Typeface.SERIF);
        editPassword.setTransformationMethod(new PasswordTransformationMethod());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_signin:
                String name = editName.getText().toString();
                String password = editPassword.getText().toString();

                if (name.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields.", Toast.LENGTH_LONG).show();
                } else {
                    final ProgressDialog dialog = DialogUtil.showProgressDialog(LoginActivity.this, "Please wait while checking account");
                    RestClient<UserClient> restClient = new RestClient<>();
                    UserClient userClient = restClient.getAppClient(UserClient.class);

                    Call<ObjectModel<UserModel>> call = userClient.loginUser(name, password);
                    call.enqueue(new Callback<ObjectModel<UserModel>>() {
                        @Override
                        public void onResponse(Call<ObjectModel<UserModel>> call, retrofit2.Response<ObjectModel<UserModel>> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()) {
                                UserModel curUser = response.body().getData();
                                curUser.setLoginMode(Constants.LOGINMODE_EMAIL);
                                Utils.saveUserInfo(LoginActivity.this, curUser);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, errLoad, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ObjectModel<UserModel>> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, errNetwork, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.btn_login_signup:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra(Constants.EXTRA_USERTYPE, 2);
                startActivity(intent);
                break;
            case R.id.txt_login_forgot:
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
                break;
        }
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        int a = 0;
        a += 5;
    }

    @Override
    public void onResponse(JSONObject response) {
        if (response == null) {
            Toast.makeText(LoginActivity.this, "Network connection failed.", Toast.LENGTH_LONG).show();
        } else {
            try {
                boolean status = response.getBoolean("status");
                if (status) {
                    JSONArray users = response.getJSONArray("data");
                    JSONObject user = users.getJSONObject(0);

                    UserModel curUser = new UserModel();
                    curUser.setId(user.getInt("id"));
                    curUser.setName(user.getString("name"));
                    curUser.setEmail(user.getString("email"));
                    curUser.setPhone(user.getString("phone"));
                    curUser.setAddress(user.getString("address"));
                    curUser.setLatitude(user.getDouble("latitude"));
                    curUser.setLongitude(user.getDouble("longitude"));
                    curUser.setZipcode(user.getInt("zipcode"));
                    curUser.setPassword(user.getString("password"));
                    curUser.setSkills(user.getString("skills"));
                    curUser.setAvatar(user.getString("avatar"));
                    curUser.setDescription(user.getString("description"));

                    curUser.setLoginMode(Constants.LOGINMODE_EMAIL);

                    Utils.saveUserInfo(getApplicationContext(), curUser);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    finish();
                } else {
                    String data = response.getString("data");
                    Toast.makeText(LoginActivity.this, data, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, "Error occured in server.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public void onBackPressed() {
        finish();
    }

}
