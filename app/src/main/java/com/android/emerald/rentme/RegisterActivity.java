package com.android.emerald.rentme;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emerald.rentme.Models.UserModel;
import com.android.emerald.rentme.Task.APIRequester;
import com.android.emerald.rentme.Utils.BitmapUtil;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.Utils;
import com.android.emerald.rentme.Views.FlowLayout;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.util.Util;
import com.squareup.picasso.Picasso;
import com.touchboarder.weekdaysbuttons.WeekdaysDataItem;
import com.touchboarder.weekdaysbuttons.WeekdaysDataSource;
import com.touchboarder.weekdaysbuttons.WeekdaysDrawableProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by emerald on 5/29/2017.
 */
public class RegisterActivity extends AppCompatActivity implements WeekdaysDataSource.Callback, View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    private WeekdaysDataSource weekDayPicker;

    private CircleImageView imgAvatar;

    private ImageView btnBack;
    private TextView txtPage;

    private ImageView imgCamera;

    private TextView txtWeekDay;
    private TextView txtWorkTime;
    private TextView txtRate;
    private TextView txtSkill;

    private EditText editUsername;
    private EditText editEmail;
    private EditText editPhonenumber;
    private EditText editPassword;
    private EditText editConfirm;
    private EditText editAddress;
    private EditText editZipcode;
    private EditText editDescription;

    private FlowLayout flowSkills;

    private Button btnSubmit;

    private Bitmap bmpAvatar = null;

    private String name;
    private String email;
    private String phone;
    private String password;
    private String confirm;
    private String address;
    private String zipcode;
    private String workday = "[]";
    private String worktime = "0";
    private String hourlyrate = "0";
    private String skilsStr = "[]";
    private String description;

    private int API_TAG = 0;

    private ArrayList<Integer> workDays;
    private ArrayList<String> skills;
    private ArrayList<AppCompatCheckBox> skillChecks;

    private int usertype;
    private String imagPath;

    private UserModel curUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usertype = getIntent().getIntExtra(Constants.EXTRA_USERTYPE, 0);

        curUser = Utils.retrieveUserInfo(getApplicationContext());

        initViewVariables();

        if (usertype == 2 || (curUser != null && curUser.getType() == 2))
            getSkills();
    }

    private void initViewVariables() {
        imagPath = "";

        btnBack = (ImageView)findViewById(R.id.btn_register_back);
        btnBack.setOnClickListener(this);

        txtPage = (TextView)findViewById(R.id.txt_register_page);
        if (usertype == 0)
            txtPage.setText("Edit Profile");
        else
            txtPage.setText("Register");
        /*
        weekDayPicker = new WeekdaysDataSource(this, R.id.stub_register_weekday)
                .setDrawableType(WeekdaysDrawableProvider.MW_ROUND_RECT)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setSelectedDays(false, false, false, false, false, false, false)
                .setSelectedColorRes(R.color.colorWeekSelect)
                .setUnselectedColorRes(R.color.colorWeekUnselect)
                .setTextColorUnselected(Color.WHITE)
                .setFontTypeFace(Typeface.MONOSPACE)
                .setFontBaseSize(14)
                .setNumberOfLetters(3)
                .start(this);
        workDays = new ArrayList<Integer>();
        //workDays.add(Calendar.SUNDAY);
        //workDays.add(Calendar.SATURDAY);
        */
        imgAvatar = (CircleImageView)findViewById(R.id.img_register_avatar);

        imgCamera = (ImageView)findViewById(R.id.img_register_camera);
        imgCamera.setOnClickListener(this);

        btnSubmit = (Button)findViewById(R.id.btn_register_submit);
        btnSubmit.setOnClickListener(this);

        editUsername = (EditText)findViewById(R.id.edit_register_username);
        editEmail = (EditText)findViewById(R.id.edit_register_email);
        editPhonenumber = (EditText)findViewById(R.id.edit_register_phone);
        editPassword = (EditText)findViewById(R.id.edit_register_password);
        editPassword.setTypeface(Typeface.SERIF);
        editPassword.setTransformationMethod(new PasswordTransformationMethod());
        editConfirm = (EditText)findViewById(R.id.edit_register_confirm);
        editConfirm.setTypeface(Typeface.SERIF);
        editConfirm.setTransformationMethod(new PasswordTransformationMethod());
        editAddress = (EditText)findViewById(R.id.edit_register_address);
        editZipcode = (EditText)findViewById(R.id.edit_register_zipcode);
        editDescription = (EditText)findViewById(R.id.edit_register_description);
        /*
        editWorkTime = (EditText)findViewById(R.id.edit_register_worktime);
        editRate = (EditText)findViewById(R.id.edit_register_rate);

        flowSkills = (FlowLayout)findViewById(R.id.flow_register_skills);
        skills = new ArrayList<>();

        txtWeekDay = (TextView)findViewById(R.id.txt_register_weekday);
        txtWorkTime = (TextView)findViewById(R.id.txt_register_worktime);
        txtRate = (TextView)findViewById(R.id.txt_register_rate);
        txtSkill = (TextView)findViewById(R.id.txt_register_skill);
        */
        if ((curUser != null && curUser.getType() == 1) || usertype == 1) {
            hideConsumer();
        }
        if (curUser != null) {
            prepareUserData(curUser.getName(), curUser.getEmail(), curUser.getPhone(), curUser.getAddress(),
                    Integer.toString(curUser.getZipcode()), curUser.getWorkday(), Integer.toString(curUser.getWorktime()),
                    Double.toString(curUser.getRate()), curUser.getSkills(), curUser.getAvatar(), curUser.getDescription());
        }
    }

    private void hideConsumer() {
        /*
        LinearLayout formRate = (LinearLayout)findViewById(R.id.form_rate);
        formRate.setVisibility(View.GONE);
        txtRate.setVisibility(View.GONE);

        LinearLayout formSkills = (LinearLayout)findViewById(R.id.form_skills);
        formSkills.setVisibility(View.GONE);
        txtSkill.setVisibility(View.GONE);

        LinearLayout formWorkDay = (LinearLayout)findViewById(R.id.form_workday);
        formWorkDay.setVisibility(View.GONE);
        txtWeekDay.setVisibility(View.GONE);

        LinearLayout formWorkTime = (LinearLayout)findViewById(R.id.form_worktime);
        formWorkTime.setVisibility(View.GONE);
        txtWorkTime.setVisibility(View.GONE);
        */
    }

    private void getSkills() {
        skills = new ArrayList<>();
        API_TAG = 1;
        APIRequester requester = new APIRequester(Constants.API_ROOT_URL + Constants.API_SKILL_GET, null, this, this);
        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    private void getUserInfo() {
        API_TAG = 3;

        String url = Constants.API_ROOT_URL + Constants.API_USER_GET_BY_ID;

        Map<String, String> params = new HashMap<>();
        params.put("userid", Integer.toString(curUser.getId()));

        APIRequester requester = new APIRequester(Request.Method.POST, url, params, this, this);
        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    public void takePhoto() {
        Intent intent = new Intent(this, ImageCropActivity.class);
        intent.putExtra("ACTION", Constants.REQUEST_CAMERA);
        startActivityForResult(intent, Constants.REQUEST_CODE_UPDATE_PIC);
    }

    public void choosePhoto() {
        Intent intent = new Intent(this, ImageCropActivity.class);
        intent.putExtra("ACTION", Constants.REQUEST_GALLERY);
        startActivityForResult(intent, Constants.REQUEST_CODE_UPDATE_PIC);
    }

    public void selectImage() {
        String title = getResources().getString(R.string.choose_picture);
        final CharSequence[] options = {getResources().getString(R.string.choose_camera), getResources().getString(R.string.choose_gallery)};

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle(title);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(getResources().getString(R.string.choose_camera))) {
                    if (!Utils.checkPermission(RegisterActivity.this, "android.permission.CAMERA") || !Utils.checkPermission(RegisterActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                    } else {
                        takePhoto();
                    }
                } else if (options[which].equals(getResources().getString(R.string.choose_gallery))) {
                    if (!Utils.checkPermission(RegisterActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                    } else {
                        choosePhoto();
                    }
                }
            }
        });
        builder.show();
    }

    public void submitProfile() {
        name = editUsername.getText().toString();
        email = editEmail.getText().toString();
        phone = editPhonenumber.getText().toString();
        password = editPassword.getText().toString();
        confirm = editConfirm.getText().toString();
        address = editAddress.getText().toString();
        zipcode = editZipcode.getText().toString();
        description = editDescription.getText().toString();

        workday = "[]";
        worktime = "0";
        hourlyrate = "0";
        skilsStr = "[]";

        if (!checkValidation(name, email, phone, password, confirm, address, zipcode, worktime, hourlyrate)) {
            Toast.makeText(RegisterActivity.this, "Please fill all fields.", Toast.LENGTH_LONG).show();
        } else {
            /*
            if (workDays.size() < 1 && (usertype == 2 || (curUser != null && curUser.getType() == 2))) {
                Toast.makeText(RegisterActivity.this, "Talents should select work days.", Toast.LENGTH_LONG).show();
            }else if (skills.size() < 1 && (usertype == 2 || (curUser != null && curUser.getType() == 2))) {
                Toast.makeText(RegisterActivity.this, "Talents should select at least one skill.", Toast.LENGTH_LONG).show();
            } else if (skills.size() > Constants.MAX_SKILLS && (usertype == 2 || (curUser != null && curUser.getType() == 2))) {
                Toast.makeText(RegisterActivity.this, "Talents can select max " + Integer.toString(Constants.MAX_SKILLS) + " skill.", Toast.LENGTH_LONG).show();
            } else */if (!password.equals(confirm)) {
                Toast.makeText(RegisterActivity.this, "Confirm password doesn't match.", Toast.LENGTH_LONG).show();
            } else {
                if (bmpAvatar != null) {
                    BitmapUtil.uploadImage(RegisterActivity.this, bmpAvatar, new Response.Listener<String>(){

                        @Override
                        public void onResponse(String response) {
                            getUploadedPath(response);
                        }
                    });
                } else {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("email", email);
                    if (usertype != 0)
                        params.put("type", Integer.toString(usertype));
                    else if (curUser != null)
                        params.put("type", Integer.toString(curUser.getType()));
                    params.put("password", password);
                    params.put("phone", phone);
                    params.put("address", address);
                    params.put("latitude", Double.toString(53.558));
                    params.put("longitude", Double.toString(9.927));
                    params.put("zipcode", zipcode);
                    params.put("workday", workday);
                    params.put("worktime", worktime);
                    params.put("rate", hourlyrate);
                    params.put("skills", skilsStr);
                    params.put("description", description);
                    if (usertype == 0 && curUser != null) {
                        //params.put("avatar", curUser.getAvatar());
                        params.put("avatar", curUser.getAvatar());
                    }
                    else {
                        params.put("avatar", imagPath);
                    }

                    String url = "";
                    if (usertype != 0) {
                        url = Constants.API_ROOT_URL + Constants.API_USER_REGISTER;
                    } else {
                        url = Constants.API_ROOT_URL + Constants.API_USER_EDIT;
                        params.put("id", Integer.toString(curUser.getId()));
                    }

                    API_TAG = 2;

                    APIRequester requester = new APIRequester(Request.Method.POST, url, params, this, this);
                    AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
                }
            }
        }
    }

    private boolean checkValidation(String name, String email, String phone, String password, String confirm, String address, String zipcode, String worktime, String hourlyrate) {
        if (usertype == 1 || (curUser != null && curUser.getType() == 1)) {
            if (name.equals("") || email.equals("") || phone.equals("") || password.equals("") || confirm.equals("") || address.equals("") || zipcode.equals(""))
                return false;
            else
                return true;
        } else if (usertype == 2 || (curUser!= null && curUser.getType() == 2)) {
            if (name.equals("") || password.equals("") || confirm.equals("") || address.equals("") || zipcode.equals("") || worktime.equals("") || hourlyrate.equals(""))
                return false;
            else
                return true;
        } else
            return false;
    }

    private void emptyFields() {
        editUsername.setText("");
        editPhonenumber.setText("");
        editPassword.setText("");
        editConfirm.setText("");
        editAddress.setText("");
        editZipcode.setText("");
        editDescription.setText("");
    }

    private void showCroppedImage(String mImagePath) {
        if (mImagePath != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(mImagePath);
            bmpAvatar = myBitmap;
            //BitmapUtil.uploadImage(RegisterActivity.this, myBitmap);
            imgAvatar.setImageBitmap(myBitmap);
        }
    }

    public void getUploadedPath(String url) {
        if (curUser == null) {
            imagPath = Constants.SITE_URL + "uploads/" + url;
        } else {
            curUser.setAvatar(Constants.SITE_URL + "uploads/" + url);
            Utils.saveUserInfo(getApplicationContext(), curUser);
        }

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        if (usertype != 0)
            params.put("type", Integer.toString(usertype));
        else if (curUser != null)
            params.put("type", Integer.toString(curUser.getType()));
        params.put("password", password);
        params.put("phone", phone);
        params.put("address", address);
        params.put("latitude", Double.toString(53.558));
        params.put("longitude", Double.toString(9.927));
        params.put("zipcode", zipcode);
        params.put("workday", workday);
        params.put("worktime", worktime);
        params.put("rate", hourlyrate);
        params.put("skills", skilsStr);
        if (usertype == 0 && curUser != null) {
            //params.put("avatar", curUser.getAvatar());
            params.put("avatar", curUser.getAvatar());
        }
        else {
            params.put("avatar", imagPath);
        }

        String apiurl = "";
        if (usertype != 0) {
            apiurl = Constants.API_ROOT_URL + Constants.API_USER_REGISTER;
        } else {
            apiurl = Constants.API_ROOT_URL + Constants.API_USER_EDIT;
            params.put("id", Integer.toString(curUser.getId()));
        }

        API_TAG = 2;

        APIRequester requester = new APIRequester(Request.Method.POST, apiurl, params, this, this);
        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_UPDATE_PIC) {
            if (resultCode == RESULT_OK) {
                imagPath = data.getStringExtra(Constants.IMAGE_PATH);
                if (usertype == 0 && curUser != null)
                    curUser.setAvatar(imagPath);
                showCroppedImage(imagPath);
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = data.getStringExtra(Constants.ERROR_MSG);
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length == 2 && grantResults[0] >= 0 && grantResults[1] >= 0) {
            takePhoto();
        } else if (grantResults.length == 1 && grantResults[0] >= 0) {
            choosePhoto();
        }
    }

    @Override
    public void onWeekdaysItemClicked(int i, WeekdaysDataItem item) {
        if (item.isSelected()) {
            if (!workDays.contains(item.getCalendarDayId()))
                workDays.add(item.getCalendarDayId());
        } else {
            if (workDays.contains(item.getCalendarDayId()))
                workDays.remove((Integer) item.getCalendarDayId());
        }
    }

    @Override
    public void onWeekdaysSelected(int attachId, ArrayList<WeekdaysDataItem> items) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*
            case R.id.img_register_avatar:
                selectImage();
                break;
            */
            case R.id.btn_register_back:
                finish();
                break;
            case R.id.img_register_camera:
                selectImage();
                break;
            case R.id.btn_register_submit:
                submitProfile();
                break;
            /*
            case R.id.edit_register_worktime:
                InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(editWorkTime.getWindowToken(), 0);
                editWorkTime.setOnClickListener(this);

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RegisterActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editWorkTime.setText( selectedHour + " : " + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            */
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            if (API_TAG == 1) {
                boolean status = response.getBoolean("status");
                if (status) {
                    skillChecks = new ArrayList<>();
                    JSONArray jsonSkills = response.getJSONArray("data");
                    for (int i = 0; i < jsonSkills.length(); i++) {
                        final AppCompatCheckBox skill = new AppCompatCheckBox(RegisterActivity.this);
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        skill.setLayoutParams(params);
                        skill.setText(jsonSkills.getJSONObject(i).getString("title"));
                        skill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked && !skills.contains(buttonView.getText().toString())) {
                                    skills.add(buttonView.getText().toString());
                                } else if(!isChecked && skills.contains(buttonView.getText().toString())) {
                                    skills.remove(buttonView.getText().toString());
                                }
                            }
                        });
                        skillChecks.add(skill);
                    }

                    if (curUser != null && curUser.getType() == 2) {
                        String skillD = curUser.getSkills().substring(1, curUser.getSkills().length() - 1);
                        String[] skillStrArray = skillD.split(",");

                        for (int i = 0; i < skillChecks.size(); i++) {
                            for (int j = 0; j < skillStrArray.length; j++) {
                                if (skillChecks.get(i).getText().equals(skillStrArray[j].trim())) {
                                    skillChecks.get(i).setChecked(true);
                                    //skills.add(skillStrArray[j].trim());
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (API_TAG == 2) {
                if (response.getBoolean("status")) {
                    if (usertype == 0) {
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    } else {
                        finish();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Account already exists.", Toast.LENGTH_LONG).show();
                    //emptyFields();
                }
            } else if (API_TAG == 3) {
                if (response.getBoolean("status")) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    JSONObject jsonUser = jsonArray.getJSONObject(0);
                    curUser.setType(jsonUser.getInt("type"));
                    if (curUser.getType() == 0)
                        hideConsumer();
                    prepareUserData(jsonUser.getString("name"), jsonUser.getString("email"), jsonUser.getString("phone"), jsonUser.getString("address"),
                            jsonUser.getString("zipcode"), jsonUser.getString("workday"), jsonUser.getString("worktime"),
                            jsonUser.getString("rate"), jsonUser.getString("skills"), jsonUser.getString("avatar"), jsonUser.getString("description"));
                } else {
                    Toast.makeText(RegisterActivity.this, response.getString("data"), Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareUserData(String name, String email, String phone, String address, String zipcode, String workday, String worktime, String rate, String skill, String avatar, String description) {
        if (avatar != null && !avatar.equals("") && !avatar.equals("null")) {
            //showCroppedImage(avatar);
            Picasso.with(RegisterActivity.this).load(avatar).into(imgAvatar);
        }

        editUsername.setText(name);
        editEmail.setText(email);
        editPhonenumber.setText(phone);
        editAddress.setText(address);
        editZipcode.setText(zipcode);
        editDescription.setText(description);
        /*
        String workD = workday.substring(1, workday.length() - 1);
        String[] workDayStrArray = workD.split(",");

        for (int i = 0; i < workDayStrArray.length; i++) {
            if (workDayStrArray[i].trim().equals(""))
                continue;
            weekDayPicker.setSelectedDays(Integer.parseInt(workDayStrArray[i].trim()) - 1);
            workDays.add(Integer.parseInt(workDayStrArray[i].trim()));
        }
        */
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
