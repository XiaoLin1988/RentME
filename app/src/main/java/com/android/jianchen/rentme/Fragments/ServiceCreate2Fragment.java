package com.android.jianchen.rentme.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jianchen.rentme.Adapter.VideoLinkRecyclerAdapter;
import com.android.jianchen.rentme.Adapter.WebLinkRecyclerAdapter;
import com.android.jianchen.rentme.Dialogs.PhotoDialog;
import com.android.jianchen.rentme.Dialogs.VideoLinkDialog;
import com.android.jianchen.rentme.Dialogs.WebLinkDialog;
import com.android.jianchen.rentme.Interface.OnDialogSelectListener;
import com.android.jianchen.rentme.Interface.OnPostVideoListener;
import com.android.jianchen.rentme.Interface.OnPostWebListener;
import com.android.jianchen.rentme.Listener.LoadCompleteListener;
import com.android.jianchen.rentme.Listener.SingleClickListener;
import com.android.jianchen.rentme.Models.ObjectModel;
import com.android.jianchen.rentme.Models.ServiceModel;
import com.android.jianchen.rentme.Models.SkillModel;
import com.android.jianchen.rentme.Models.WebLink;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.RestAPI.CommonClient;
import com.android.jianchen.rentme.RestAPI.RestClient;
import com.android.jianchen.rentme.RestAPI.ServiceClient;
import com.android.jianchen.rentme.ServiceDetailActivity2;
import com.android.jianchen.rentme.Utils.Constants;
import com.android.jianchen.rentme.Utils.DialogUtil;
import com.android.jianchen.rentme.Utils.Utils;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    @Bind(R.id.btn_post_add)
    FloatingActionButton btnAdd;
    @Bind(R.id.txt_done)
    TextView txtDone;
    @Bind(R.id.img_close)
    ImageView imgClose;

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
                case R.id.btn_post_add:
                    CharSequence[] options = {"Add image", "Add Youtube/Vimeo video link", "Add web link"};
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
                case R.id.txt_done:
                    if (validate()) {
                        createService();
                    }
                    break;
                case R.id.img_close:
                    getActivity().finish();
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
        txtDone.setOnClickListener(clickListener);
        imgClose.setOnClickListener(clickListener);

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

        final ServiceModel service = new ServiceModel();

        RestClient<ServiceClient> restClient = new RestClient<>();
        ServiceClient serviceClient = restClient.getAppClient(ServiceClient.class);

        Call<ObjectModel<Integer>> call = serviceClient.createService(Utils.retrieveUserInfo(context).getId(), skill.getId(), editTitle.getText().toString(), editBalance.getText().toString(), editDetail.getText().toString());
        call.enqueue(new Callback<ObjectModel<Integer>>() {
            @Override
            public void onResponse(Call<ObjectModel<Integer>> call, retrofit2.Response<ObjectModel<Integer>> response) {
                if (response.isSuccessful() && response.body().getStatus()) {
                    int serviceId = response.body().getData();
                    service.setId(serviceId);
                    service.setTitle(editTitle.getText().toString());
                    service.setDetail(editDetail.getText().toString());
                    service.setBalance(Integer.parseInt(editBalance.getText().toString()));
                    service.setPreview("");
                    service.setSkill_id(skill.getId());
                    service.setSkill_preview(skill.getPath());
                    service.setSkill_title(skill.getTitle());
                    service.setTalent_id(Utils.retrieveUserInfo(context).getId());

                    RestClient<CommonClient> restClient1 = new RestClient<>();
                    CommonClient commonClient = restClient1.getAppClient(CommonClient.class);
                    int loadCount = 0;
                    if (webLinks.size() > 0)
                        loadCount ++;
                    if (videoLinks.size() > 0)
                        loadCount ++;
                    if (loadCount > 0) {
                        final LoadCompleteListener loadListener = new LoadCompleteListener(loadCount) {
                            @Override
                            public void onLoaded() {
                                dialog.dismiss();
                                getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent().putExtra(Constants.KEY_SERVICE, service));
                                Intent intent = new Intent(context, ServiceDetailActivity2.class);
                                intent.putExtra(Constants.EXTRA_SERVICE_DETAIL, service);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        };

                        if (webLinks.size() > 0) {
                            JSONStringer webs = new JSONStringer();
                            try {
                                webs = webs.array();
                                for (int i = 0; i < webLinks.size(); i++) {
                                    webs.object();
                                    webs.key("title").value(webLinks.get(i).getTitle());
                                    webs.key("content").value(webLinks.get(i).getContent());
                                    webs.key("thumbnail").value(webLinks.get(i).getThumbnail());
                                    webs.key("link").value(webLinks.get(i).getLink());
                                    webs.endObject();
                                }
                                webs.endArray();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Call<ObjectModel<String>> call1 = commonClient.uploadWebs(Constants.VALUE_SERVICE, serviceId, webs);
                            call1.enqueue(new Callback<ObjectModel<String>>() {
                                @Override
                                public void onResponse(Call<ObjectModel<String>> call, Response<ObjectModel<String>> response) {
                                    loadListener.setLoaded();
                                    if (response.isSuccessful() && response.body().getStatus()) {
                                        int a = 0;
                                        a += 5;
                                    } else {

                                    }
                                }

                                @Override
                                public void onFailure(Call<ObjectModel<String>> call, Throwable t) {
                                    loadListener.setLoaded();
                                }
                            });
                        }
                        if (videoLinks.size() > 0) {
                            Call<ObjectModel<String>> call1 = commonClient.uploadVideos(Constants.VALUE_SERVICE, serviceId, videoLinks);
                            call1.enqueue(new Callback<ObjectModel<String>>() {
                                @Override
                                public void onResponse(Call<ObjectModel<String>> call, Response<ObjectModel<String>> response) {
                                    loadListener.setLoaded();
                                    if (response.isSuccessful() && response.body().getStatus()) {
                                        int a = 0;
                                        a += 5;
                                    } else {

                                    }
                                }

                                @Override
                                public void onFailure(Call<ObjectModel<String>> call, Throwable t) {
                                    loadListener.setLoaded();
                                }
                            });
                        }
                    } else {
                        dialog.dismiss();
                        getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent().putExtra(Constants.KEY_SERVICE, service));
                        Intent intent = new Intent(context, ServiceDetailActivity2.class);
                        intent.putExtra(Constants.EXTRA_SERVICE_DETAIL, service);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.error_load), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObjectModel<Integer>> call, Throwable t) {
                dialog.dismiss();
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
