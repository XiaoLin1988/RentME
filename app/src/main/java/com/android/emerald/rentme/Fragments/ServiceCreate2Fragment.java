package com.android.emerald.rentme.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.emerald.rentme.Adapter.VideoLinkRecyclerAdapter;
import com.android.emerald.rentme.Adapter.WebLinkRecyclerAdapter;
import com.android.emerald.rentme.Dialogs.PhotoDialog;
import com.android.emerald.rentme.Dialogs.VideoLinkDialog;
import com.android.emerald.rentme.Dialogs.WebLinkDialog;
import com.android.emerald.rentme.Interface.OnDialogSelectListener;
import com.android.emerald.rentme.Interface.OnPostVideoListener;
import com.android.emerald.rentme.Interface.OnPostWebListener;
import com.android.emerald.rentme.Listener.SingleClickListener;
import com.android.emerald.rentme.Models.ArrayModel;
import com.android.emerald.rentme.Models.ObjectModel;
import com.android.emerald.rentme.Models.ReviewModel;
import com.android.emerald.rentme.Models.SkillModel;
import com.android.emerald.rentme.Models.WebLink;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.RestAPI.CommonClient;
import com.android.emerald.rentme.RestAPI.RestClient;
import com.android.emerald.rentme.RestAPI.ServiceClient;
import com.android.emerald.rentme.ServiceDetailActivity2;
import com.android.emerald.rentme.Utils.DialogUtil;
import com.android.emerald.rentme.Utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by emerald on 12/8/2017.
 */
public class ServiceCreate2Fragment extends Fragment implements OnPostVideoListener, OnPostWebListener {
    @Bind(R.id.edit_service_title)
    EditText editTitle;
    @Bind(R.id.edit_service_detail)
    EditText editDetail;
    @Bind(R.id.edit_service_balance)
    EditText editBalance;
    @Bind(R.id.btn_service_add)
    ImageButton btnAdd;
    @Bind(R.id.btn_service_create)
    Button btnCreate;

    @Bind(R.id.recycler_photos)
    RecyclerView recyclerPhotos;

    @Bind(R.id.recycler_web_links)
    RecyclerView recyclerWebLink;
    WebLinkRecyclerAdapter adapterWebLink;
    ArrayList<WebLink> webLinks;

    @Bind(R.id.recycler_videos)
    RecyclerView recyclerVideo;;
    VideoLinkRecyclerAdapter adapterVideo;
    ArrayList<String> videoLinks;

    private Context context;
    private SkillModel skill;

    private SingleClickListener clickListener = new SingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_service_add:
                    CharSequence[] options = {"Post images", "Post videos", "Post web links"};
                    DialogUtil.showSelectDialog(context, options, new OnDialogSelectListener() {
                        @Override
                        public void onDialogSelect(int position) {
                            if (position == 0) {
                                PhotoDialog dialog = new PhotoDialog(context);
                                dialog.show();
                            } else if (position == 1) {
                                VideoLinkDialog dialog = new VideoLinkDialog(context);
                                dialog.setVideoListener(ServiceCreate2Fragment.this);
                                dialog.show();
                            } else if (position == 2) {
                                WebLinkDialog dialog = new WebLinkDialog(context);
                                dialog.setWebListener(ServiceCreate2Fragment.this);
                                dialog.show();
                            }
                        }
                    });
                    break;
                case R.id.btn_service_create:
                    if (validate()) {
                        createService();
                    }
                    break;
            }
        }
    };

    public static ServiceCreate2Fragment newInstance(Context ctx, SkillModel s) {
        ServiceCreate2Fragment fragment = new ServiceCreate2Fragment();
        fragment.context = ctx;
        fragment.skill = s;

        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_create2, container, false);
        ButterKnife.bind(this, view);

        initViewVariables(view);

        return view;
    }

    private void initViewVariables(View view) {
        btnAdd.setOnClickListener(clickListener);
        btnCreate.setOnClickListener(clickListener);

        webLinks = new ArrayList<>();
        adapterWebLink = new WebLinkRecyclerAdapter(webLinks);
        recyclerWebLink.setLayoutManager(new LinearLayoutManager(context));
        recyclerWebLink.setAdapter(adapterWebLink);

        videoLinks = new ArrayList<>();
        adapterVideo = new VideoLinkRecyclerAdapter(videoLinks);
        recyclerVideo.setLayoutManager(new LinearLayoutManager(context));
        recyclerVideo.setAdapter(adapterVideo);
    }

    private boolean validate() {
        if (editTitle.getText().toString().equals("")) {
            Toast.makeText(context, "Please input all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editBalance.getText().toString().equals("")) {
            Toast.makeText(context, "Please input all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editDetail.getText().toString().equals("")) {
            Toast.makeText(context, "Please input all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void createService() {
        final ProgressDialog dialog = DialogUtil.showProgressDialog(context, "Please wait while creating service");
        RestClient<ServiceClient> restClient = new RestClient<>();
        ServiceClient serviceClient = restClient.getAppClient(ServiceClient.class);

        Call<ObjectModel<Integer>> call = serviceClient.createService(Utils.retrieveUserInfo(context).getId(), skill.getId(), editTitle.getText().toString(), editBalance.getText().toString(), editDetail.getText().toString());
        call.enqueue(new Callback<ObjectModel<Integer>>() {
            @Override
            public void onResponse(Call<ObjectModel<Integer>> call, retrofit2.Response<ObjectModel<Integer>> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body().getStatus()) {
                    int serviceId = response.body().getData();
                    if (webLinks.size() > 0 || videoLinks.size() > 0) {
                        ArrayList<String> links = new ArrayList<>();
                        for (int i = 0; i < webLinks.size(); i++) {
                            links.add(webLinks.get(i).getLink());
                        }
                    }
                    Toast.makeText(context, "Creating service succeed", Toast.LENGTH_SHORT).show();
                    RestClient<CommonClient> restClient1 = new RestClient<>();
                    CommonClient commonClient = restClient1.getAppClient(CommonClient.class);

                    //Call<ObjectModel<String>> call1 = commonClient.uploadLinks();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.error_load), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObjectModel<Integer>> call, Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPostVideo(String link) {
        adapterVideo.addItem(link);
    }

    @Override
    public void onPostWeb(WebLink webLink) {
        adapterWebLink.addItem(webLink);
    }
}
