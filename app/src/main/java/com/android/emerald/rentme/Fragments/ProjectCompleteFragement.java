package com.android.emerald.rentme.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.emerald.rentme.Adapter.ProjectCompleteListAdapter;
import com.android.emerald.rentme.AppController;
import com.android.emerald.rentme.MainActivity;
import com.android.emerald.rentme.Models.ProjectModel;
import com.android.emerald.rentme.Models.UserModel;
import com.android.emerald.rentme.ProjectCompleteDetailActivity;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.Task.APIStringRequester;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by emerald on 6/5/2017.
 */
public class ProjectCompleteFragement extends Fragment implements AdapterView.OnItemClickListener, Response.Listener<String> {

    private Context context;

    private ListView list;
    private ProjectCompleteListAdapter adapter;
    private ArrayList<ProjectModel> projects = new ArrayList<>();

    private UserModel curUser;

    public static ProjectCompleteFragement newInstance(Context context) {
        ProjectCompleteFragement complete = new ProjectCompleteFragement();
        complete.context = context;
        complete.getCompletedProjects();
        return complete;
    }

    private void getCompletedProjects() {
        curUser = Utils.retrieveUserInfo(context);

        String url = Constants.API_ROOT_URL + Constants.API_PROJECT_COMPLETED;

        Map<String, String> params = new HashMap<>();
        params.put("userid", Integer.toString(curUser.getId()));

        APIStringRequester requester = new APIStringRequester(Request.Method.POST, url, params, this, null);
        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_completed, container, false);
        initViewVariables(view);

        return view;
    }

    private void initViewVariables(View view) {
        list = (ListView)view.findViewById(R.id.list_project_completed);
        adapter = new ProjectCompleteListAdapter(getContext(), projects);
        list.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        list.setItemsCanFocus(true);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ProjectModel project = projects.get(position);
                Intent intent = new Intent(getContext(), ProjectCompleteDetailActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(project);
                intent.putExtra(Constants.EXTRA_PROJECT_DETAIL, json);
                startActivity(intent);
                return false;
            }
        });
    }

    public void openDetail(int position) {
        ProjectModel project = projects.get(position);
        Intent intent = new Intent(getContext(), ProjectCompleteDetailActivity.class);
        Gson gson = new Gson();
        String json = gson.toJson(project);
        intent.putExtra(Constants.EXTRA_PROJECT_DETAIL, json);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProjectModel project = projects.get(position);
        Intent intent = new Intent(getContext(), ProjectCompleteDetailActivity.class);
        Gson gson = new Gson();
        String json = gson.toJson(project);
        intent.putExtra(Constants.EXTRA_PROJECT_DETAIL, json);
        startActivity(intent);
        //((MainActivity)getContext()).finish();
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

                adapter.add(p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
