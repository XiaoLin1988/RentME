package com.android.emerald.rentme.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.emerald.rentme.AppController;
import com.android.emerald.rentme.MainActivity;
import com.android.emerald.rentme.Models.SkillModel;
import com.android.emerald.rentme.PhotoEditActivity;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.Task.APIRequester;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServiceCreate1Fragment extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    private Context context;

    private SkillModel skill;

    private String imgUrl;
    private ImageView imgPreview;

    private EditText editBalance;
    private EditText editDescription;
    private EditText editTitle;

    private Button btnCreate;

    private ProgressDialog mProgressDialog;

    public static ServiceCreate1Fragment newInstance(Context context, SkillModel s) {
        ServiceCreate1Fragment step3 = new ServiceCreate1Fragment();
        step3.skill = s;
        return step3;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getContext()).setPageTitel("Create Service");

        View view = inflater.inflate(R.layout.fragment_service_create1, container, false);

        initViewVariables(view);

        return view;
    }

    private void initViewVariables(View view) {
        imgPreview = (ImageView)view.findViewById(R.id.img_service_create_preview);
        imgPreview.setOnClickListener(this);

        editBalance = (EditText)view.findViewById(R.id.edit_service_create_balance);
        editDescription = (EditText)view.findViewById(R.id.edit_service_create_detail);
        editTitle = (EditText)view.findViewById(R.id.edit_service_create_title);

        btnCreate = (Button)view.findViewById(R.id.btn_service_create);
        btnCreate.setOnClickListener(this);
    }

    public void takePhoto() {
        Intent intent = new Intent(getContext(), PhotoEditActivity.class);
        intent.putExtra("ACTION", Constants.REQUEST_CAMERA);
        startActivityForResult(intent, Constants.REQUEST_CODE_UPDATE_PIC);
    }

    public void choosePhoto() {
        Intent intent = new Intent(getContext(), PhotoEditActivity.class);
        intent.putExtra("ACTION", Constants.REQUEST_GALLERY);
        startActivityForResult(intent, Constants.REQUEST_CODE_UPDATE_PIC);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_UPDATE_PIC) {
            if (resultCode == Activity.RESULT_OK) {
                imgUrl = Constants.SITE_URL + "uploads/" + data.getStringExtra("data");
                Glide.with(context).load(imgUrl).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgPreview);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = data.getStringExtra(Constants.ERROR_MSG);
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showImageSelectDialog() {
        String title = getResources().getString(R.string.choose_picture);
        final CharSequence[] options = {getResources().getString(R.string.choose_camera), getResources().getString(R.string.choose_gallery)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(getResources().getString(R.string.choose_camera))) {
                    takePhoto();
                } else if (options[which].equals(getResources().getString(R.string.choose_gallery))) {
                    choosePhoto();
                }
            }
        });
        builder.show();
    }

    private void showProgressDialog(String message) {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Please wait...");
        mProgressDialog.setMessage(message);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        mProgressDialog.hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_service_create:
                if (editBalance.getText().toString().equals("") || editDescription.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please input all fields.", Toast.LENGTH_LONG).show();
                    return;
                }

                showProgressDialog("Creating servie now...");

                int talentId = Utils.retrieveUserInfo(getContext()).getId();
                String url = Constants.API_ROOT_URL + Constants.API_SERVICE_CREATE;

                Map<String, String> params = new HashMap<>();
                params.put("talent_id", Integer.toString(talentId));
                params.put("skill_id", Integer.toString(skill.getId()));
                if (imgUrl != null)
                    params.put("preview", imgUrl);
                params.put("title", editTitle.getText().toString());
                params.put("balance", editBalance.getText().toString());
                params.put("detail", editDescription.getText().toString());

                APIRequester requester = new APIRequester(Request.Method.POST, url, params, this, this);
                AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
                break;
            case R.id.img_service_create_preview:
                showImageSelectDialog();
                break;
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        hideProgressDialog();

        try {
            Toast.makeText(getContext(), response.getString("data"), Toast.LENGTH_LONG).show();
            boolean status = response.getBoolean("status");
            if (status) {
                ((MainActivity)getContext()).setCreateStep(3);
                ((MainActivity)getContext()).openMyProjects();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgressDialog();
        Toast.makeText(getContext(), "Please check network status.", Toast.LENGTH_LONG).show();
    }
}
