package com.android.jianchen.rentme.activity.root;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.jianchen.rentme.activity.me.ProfileActivity;
import com.android.jianchen.rentme.model.rentme.ObjectModel;
import com.android.jianchen.rentme.model.rentme.UserModel;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.helper.network.retrofit.RestClient;
import com.android.jianchen.rentme.helper.network.retrofit.UserClient;
import com.android.jianchen.rentme.helper.Constants;
import com.android.jianchen.rentme.helper.utils.Utils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by emerald on 5/29/2017.
 */
public class SocialLoginActivity extends AppCompatActivity implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private LoginButton btnFB;
    private Button btnGG;
    private Button btnWX;
    private Button btnEmail;

    private GoogleApiClient mGoogleApiClient; // for google login

    private CallbackManager callbackManager; // for facebook login

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SocialLoginActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_social_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Build GoogleApiClient to request access to the basic user profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();


        callbackManager = CallbackManager.Factory.create();


        UserModel curUser = Utils.retrieveUserInfo(getApplicationContext());
        if (curUser != null) {
            Intent intent = new Intent(SocialLoginActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        } else {
            initViewVariables();
        }
    }



    @Override
    protected void onStart() {
        super.onStart();

        // Connect GoogleApiClient
        mGoogleApiClient.connect();

    }


    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount gAccount = result.getSignInAccount();
            String email = gAccount.getEmail();
            String name = gAccount.getDisplayName();
            String avatar = "";
            if (gAccount.getPhotoUrl() != null)
                avatar = gAccount.getPhotoUrl().toString();

            //Toast.makeText(SocialLoginActivity.this, name, Toast.LENGTH_LONG).show();

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    });

            doThirdPartyLogin(name, email, avatar, Constants.LOGINMODE_GOOGLE);
        } else {
            Toast.makeText(SocialLoginActivity.this, "Google Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void doThirdPartyLogin(String name, String email, String avatar, String mode) {

        // check source mode

        if (mode == Constants.LOGINMODE_GOOGLE) { // Google

            RestClient<UserClient> restClient = new RestClient<>();
            UserClient userClient = restClient.getAppClient(UserClient.class);

            Call<ObjectModel<UserModel>> call = userClient.checkGoogleUser(email, name, avatar);
            call.enqueue(new Callback<ObjectModel<UserModel>>() {
                @Override
                public void onResponse(Call<ObjectModel<UserModel>> call, retrofit2.Response<ObjectModel<UserModel>> response) {

                    if (response.isSuccessful() && response.body().getStatus()) {

                        UserModel curUser = response.body().getData();

                        curUser.setLoginMode(Constants.LOGINMODE_GOOGLE);

                        Utils.saveUserInfo(SocialLoginActivity.this, curUser);
                        Intent intent = new Intent(SocialLoginActivity.this, ProfileActivity.class);
                        startActivity(intent);


                    } else {
                        Toast.makeText(SocialLoginActivity.this, getResources().getString(R.string.error_loginSignup), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ObjectModel<UserModel>> call, Throwable t) {
                    Toast.makeText(SocialLoginActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                }
            });


        }
        else if (mode == Constants.LOGINMODE_FACEBOOK) {

        }



    }

    private void initViewVariables() {

        btnFB = (LoginButton) findViewById(R.id.btn_login_fb);
        btnFB.setReadPermissions("public_profile");
        btnFB.setOnClickListener(this);

        /*----------------------- FACEBOOK LOGIN --------------------------------*/
        btnFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String profile = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            String name = object.getString("name");

                            doThirdPartyLogin(name, name + "@gmail.com", profile, Constants.LOGINMODE_FACEBOOK);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //ADD THIS LINES PLEASE
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,name,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(SocialLoginActivity.this, "Facebook Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(SocialLoginActivity.this, "Facebook Login Failed", Toast.LENGTH_SHORT).show();
            }
        });




        btnGG = (Button)findViewById(R.id.btn_login_gg);
        btnGG.setOnClickListener(this);

//        btnWX = (Button)findViewById(R.id.btn_login_wx);
//        btnWX.setOnClickListener(this);

        btnEmail = (Button)findViewById(R.id.btn_login_email);
        btnEmail.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_fb:
                this.signInFacebook();
                break;
            case R.id.btn_login_gg:
                this.signInGoogle();
                break;
//            case R.id.btn_login_wx:
//                break;
            case R.id.btn_login_email:
                Intent intent = new Intent(SocialLoginActivity.this, LoginActivity.class);
                intent.putExtra(Constants.EXTRA_USERTYPE, 2);
                startActivity(intent);
                break;
        }
    }

    // Google Sigin

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Constants.REQUEST_GOOGLE_SIGNIN);
    }

    private void signInFacebook() {


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_GOOGLE_SIGNIN) { //  from Google Sign
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else { // from Facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
    /*
        @Override
        public void onResponse(JSONObject response) {
            if (response == null) {
                Toast.makeText(SocialLoginActivity.this, "Network connection failed.", Toast.LENGTH_LONG).show();
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
                        curUser.setType(user.getInt("type"));
                        curUser.setPhone(user.getString("phone"));
                        curUser.setAddress(user.getString("address"));
                        curUser.setLatidue(user.getDouble("latitude"));
                        curUser.setLongitude(user.getDouble("longitude"));
                        curUser.setZipcode(user.getInt("zipcode"));
                        curUser.setWorkday(user.getString("workday"));
                        curUser.setWorktime(user.getInt("worktime"));
                        curUser.setRate(user.getDouble("rate"));
                        curUser.setPassword(user.getString("password"));
                        curUser.setSkills(user.getString("skills"));
                        curUser.setAvatar(user.getString("avatar"));
                        curUser.setDescription(user.getString("description"));

                        Utils.saveUserInfo(getApplicationContext(), curUser);
                        Intent intent = new Intent(SocialLoginActivity.this, ProfileActivity.class);
                        startActivity(intent);

                        finish();
                    } else {
                        String data = response.getString("data");
                        Toast.makeText(SocialLoginActivity.this, data, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(SocialLoginActivity.this, "Error occured in server.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    */
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            String personPhoto = currentPerson.getImage().getUrl();
            String personGooglePlusProfile = currentPerson.getUrl();

            Toast.makeText(this, personName, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
