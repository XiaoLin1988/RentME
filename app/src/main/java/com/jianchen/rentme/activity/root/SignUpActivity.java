package com.jianchen.rentme.activity.root;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jianchen.rentme.R;
import com.jianchen.rentme.activity.me.ProfileActivity;
import com.jianchen.rentme.helper.Constants;
import com.jianchen.rentme.helper.network.retrofit.RestClient;
import com.jianchen.rentme.helper.network.retrofit.UserClient;
import com.jianchen.rentme.helper.utils.DialogUtil;
import com.jianchen.rentme.helper.utils.Utils;
import com.jianchen.rentme.model.rentme.ObjectModel;
import com.jianchen.rentme.model.rentme.UserModel;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.btn_back)
    ImageView btnBack;

    @Bind(R.id.edit_user_email)
    EditText editEmail;

    @Bind(R.id.edit_user_password)
    EditText editPassword;

    @Bind(R.id.edit_user_confirm)
    EditText editConfirm;

    @Bind(R.id.btn_signup)
    Button btnSignup;

    @BindString(R.string.error_signup)
    String errSignup;

    @BindString(R.string.error_network)
    String errNetwork;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        btnBack.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_signup:
                if (validate()) {
                    final ProgressDialog dialog = DialogUtil.showProgressDialog(SignUpActivity.this, "Please wait while registering account");
                    RestClient<UserClient> restClient = new RestClient<>();
                    UserClient userClient = restClient.getAppClient(UserClient.class);

                    Call<ObjectModel<Integer>> call = userClient.signupUser(Utils.getUserName(editEmail.getText().toString()), editEmail.getText().toString(), editPassword.getText().toString());
                    call.enqueue(new Callback<ObjectModel<Integer>>() {
                        @Override
                        public void onResponse(Call<ObjectModel<Integer>> call, Response<ObjectModel<Integer>> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body().getStatus()) {
                                UserModel user = new UserModel();
                                user.setId(response.body().getData());
                                user.setName(Utils.getUserName(editEmail.getText().toString()));
                                user.setEmail(editEmail.getText().toString());
                                user.setPassword(editPassword.getText().toString());
                                user.setLoginMode(Constants.LOGINMODE_EMAIL);
                                Utils.saveUserInfo(SignUpActivity.this, user);
                                Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, errSignup, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ObjectModel<Integer>> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(SignUpActivity.this, errNetwork, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }

    private boolean validate() {
        if (editEmail.getText().toString().equals("")) {
            Snackbar.make(findViewById(android.R.id.content), "Please validate email", Snackbar.LENGTH_SHORT).show();
            return false;
        } if (!editEmail.getText().toString().contains("@")) {
            Snackbar.make(findViewById(android.R.id.content), "Please input validate email", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (editPassword.getText().toString().equals("")) {
            Snackbar.make(findViewById(android.R.id.content), "Please input password", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!editPassword.getText().toString().equals(editConfirm.getText().toString())) {
            Snackbar.make(findViewById(android.R.id.content), "Confirm password doesn't match", Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
