package com.android.emerald.rentme.Fragments;

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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by emerald on 6/6/2017.
 */
public class ProjectCreate21Fragment extends Fragment implements View.OnClickListener, WeekdaysDataSource.Callback, Response.Listener<String> {

    private UserModel talent;
    private ArrayList<ProjectModel> projects;

    private ImageView imgAvatar;

    private TextView txtName;
    private TextView txtEmail;
    private TextView txtPhone;
    private TextView txtAddress;
    private TextView txtZip;
    private TextView txtWorktime;
    private TextView txtRate;

    private FlowLayout flowSkills;
    private WeekdaysDataSource weekDay;

    private Button btnHire;

    private ListView listReview;
    //private ProjectCompleteListAdapter adapter;

    public ProjectCreate21Fragment() {
        projects = new ArrayList<>();
    }

    public static ProjectCreate21Fragment newInstance(UserModel t) {
        ProjectCreate21Fragment fragment = new ProjectCreate21Fragment();
        fragment.talent = t;
        //fragment.prepareData();
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

        View view = inflater.inflate(R.layout.fragment_project_create2, container, false);

        initViewVariables(view);

        return view;
    }

    private void initViewVariables(View view) {
        imgAvatar = (ImageView)view.findViewById(R.id.img_project_create2_avatar);
        if (talent.getAvatar() != null && !talent.getAvatar().equals("null") && !talent.getAvatar().equals("")) {
            Glide.with(getContext()).load(talent.getAvatar())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgAvatar);
        }

        txtName = (TextView)view.findViewById(R.id.txt_project_create2_talentname);
        txtName.setText(talent.getName());

        txtEmail = (TextView)view.findViewById(R.id.txt_project_create2_talentemail);
        txtEmail.setText(talent.getEmail());

        txtPhone = (TextView)view.findViewById(R.id.txt_project_create2_phone);
        txtPhone.setText(talent.getPhone());

        txtAddress = (TextView)view.findViewById(R.id.txt_project_create2_address);
        txtAddress.setText(talent.getAddress());

        txtZip = (TextView)view.findViewById(R.id.txt_project_create2_zipcode);
        txtZip.setText(Integer.toString(talent.getZipcode()));
        /*
        txtWorktime = (TextView)view.findViewById(R.id.txt_project_create2_worktime);
        txtWorktime.setText(Integer.toString(talent.getWorktime()));

        txtRate = (TextView)view.findViewById(R.id.txt_project_create2_rate);
        txtRate.setText(Double.toString(talent.getRate()));

        listReview = (ListView)view.findViewById(R.id.list_project_create2_review);
        //adapter = new ProjectCompleteListAdapter(getContext(), projects);
        //listReview.setAdapter(adapter);
        ListHeightHelper.getListViewSize(listReview);

        flowSkills = (FlowLayout)view.findViewById(R.id.flow_project_create2_skills);
        String ss = talent.getSkills().substring(1, talent.getSkills().length() - 1);
        String[] sSkills = ss.split(",");
        for (int i = 0; i < sSkills.length; i++) {
            if (sSkills[i].trim().equals(""))
                continue;
            AppCompatCheckBox skill = new AppCompatCheckBox(getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            skill.setLayoutParams(params);
            skill.setText(sSkills[i].trim());
            skill.setChecked(true);
            skill.setClickable(false);

            flowSkills.addView(skill);
        }
        */
        /*
        weekDay = new WeekdaysDataSource((MainActivity)getContext(), R.id.stub_project_create2_weekday)
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
        */
        String ww = talent.getWorkday().substring(1, talent.getWorkday().length() - 1);
        String[] days = ww.split(",");

        for (int i = 0; i < days.length; i++) {
            if (days[i].trim().equals(""))
                continue;
        }
        /*
        btnHire = (Button)view.findViewById(R.id.btn_project_create2_hire);
        btnHire.setOnClickListener(this);
        */
    }

    @Override
    public void onClick(View v) {
        /*
        switch (v.getId()) {
            case R.id.btn_project_create2_hire:
                //((ProjectCreateFragment)getParentFragment()).hireTalent(talent.getId(), skill);
                break;
        }
        */
    }

    @Override
    public void onWeekdaysItemClicked(int i, WeekdaysDataItem weekdaysDataItem) {

    }

    @Override
    public void onWeekdaysSelected(int i, ArrayList<WeekdaysDataItem> arrayList) {

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

                //adapter.add(p);
                ListHeightHelper.getListViewSize(listReview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
