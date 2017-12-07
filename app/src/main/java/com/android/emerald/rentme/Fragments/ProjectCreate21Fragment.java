package com.android.emerald.rentme.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.emerald.rentme.AppController;
import com.android.emerald.rentme.Helper.ListHeightHelper;
import com.android.emerald.rentme.MainActivity;
import com.android.emerald.rentme.Models.ProjectModel;
import com.android.emerald.rentme.Models.UserModel;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.Task.APIStringRequester;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.Utils;
import com.android.emerald.rentme.Views.FlowLayout;
import com.android.volley.Request;
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.touchboarder.weekdaysbuttons.WeekdaysDataItem;
import com.touchboarder.weekdaysbuttons.WeekdaysDataSource;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;

/**
 * Created by emerald on 6/6/2017.
 */
public class ProjectCreate21Fragment extends Fragment implements View.OnClickListener, Response.Listener<String> {

    private UserModel talent;

    @Bind(R.id.img_profile_cover)
    ImageView imgCover;

    @Bind(R.id.img_profile_main)
    ImageView imgMain;

    @Bind(R.id.txt_profile_name)
    TextView txtName;

    @Bind(R.id.txt_profile_mood)
    TextView txtMood;

    @Bind(R.id.txt_profile_location)
    TextView txtLocation;

    @Bind(R.id.txt_profile_joined)
    TextView txtJoined;

    @Bind(R.id.txt_profile_earned)
    TextView txtEarning;

    @Bind(R.id.img_profile_mobile)
    ImageView imgMobile;

    @Bind(R.id.img_profile_facebook)
    ImageView imgFacebook;

    @Bind(R.id.img_profile_google)
    ImageView imgGoogle;

    @Bind(R.id.img_profile_wechat)
    ImageView imgWechat;

    public ProjectCreate21Fragment() {

    }

    public static ProjectCreate21Fragment newInstance(UserModel t) {
        ProjectCreate21Fragment fragment = new ProjectCreate21Fragment();
        fragment.talent = t;

        return fragment;
    }

    private void prepareData() {
        String url = Constants.API_ROOT_URL + Constants.API_PROJECT_MYREVIEW;

        Map<String, String> params = new HashMap<>();
        params.put("userid", Integer.toString(talent.getId()));

        APIStringRequester requester = new APIStringRequester(Request.Method.POST, url, params, this, null);
        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getContext()).setPageTitel("Talent information");
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        ButterKnife.bind(this, view);

        initViewVariables(view);

        return view;
    }

    private void initViewVariables(View view) {
        if (talent.getPhone() == null || talent.getPhone().equals("")) {
            imgMobile.setImageResource(R.drawable.mobile_u);
        } else {
            imgMobile.setImageResource(R.drawable.mobile_a);
        }

        if (talent.getFbId() == null || talent.getFbId().equals("")) {
            imgFacebook.setImageResource(R.drawable.facebook_u);
        } else {
            imgFacebook.setImageResource(R.drawable.facebook_a);
        }

        if (talent.getGgId() == null || talent.getGgId().equals("")) {
            imgGoogle.setImageResource(R.drawable.google_u);
        } else {
            imgGoogle.setImageResource(R.drawable.google_a);
        }

        if (talent.getWxId() == null || talent.getWxId().equals("")) {
            imgWechat.setImageResource(R.drawable.wechat_u);
        } else {
            imgWechat.setImageResource(R.drawable.wechat_a);
        }

        txtName.setText(talent.getName());
        txtMood.setText(talent.getDescription());
        txtLocation.setText(talent.getAddress());
        Date date = Utils.stringToDate(talent.getCtime());
        txtJoined.setText(Utils.beautifyDate(date, false));

        Glide.with(getContext()).load(talent.getCoverImg()).asBitmap().fitCenter().placeholder(R.drawable.placeholder).into(imgCover);
        Glide.with(getContext()).load(talent.getMainImg()).asBitmap().fitCenter().placeholder(R.drawable.profile_empty).into(imgMain);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResponse(String response) {
        try {
            JSONArray pj = new JSONArray(response);
            for (int i = 0; i < pj.length(); i++) {
                ProjectModel p = new ProjectModel();
                p.setId(pj.getJSONObject(i).getInt("id"));
                p.setName(pj.getJSONObject(i).getString("name"));
                p.setDescription(pj.getJSONObject(i).getString("description"));
                p.setTimeframe(pj.getJSONObject(i).getInt("timeframe"));
                if (pj.getJSONObject(i).getInt("consumer_score") != 0) {
                    p.setConsumer_score(pj.getJSONObject(i).getInt("consumer_score"));
                    p.setConsumer_review(pj.getJSONObject(i).getString("consumer_review"));
                }
                if (pj.getJSONObject(i).getInt("talent_score") != 0){
                    p.setTalent_score(pj.getJSONObject(i).getInt("talent_score"));
                    p.setTalent_review(pj.getJSONObject(i).getString("talent_review"));
                }
                p.setTalent_id(pj.getJSONObject(i).getInt("talent_id"));
                p.setConsumer_id(pj.getJSONObject(i).getInt("consumer_id"));
                p.setTimeframe(pj.getJSONObject(i).getInt("timeframe"));
                p.setStatus(pj.getJSONObject(i).getInt("status"));
                p.setSkill(pj.getJSONObject(i).getString("skill"));
                p.setPreview(pj.getJSONObject(i).getString("preview"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
