package com.android.jianchen.rentme.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jianchen.rentme.Adapter.SkillGalleryAdapter;
import com.android.jianchen.rentme.AppController;
import com.android.jianchen.rentme.Interface.OnSkillSelectListener;
import com.android.jianchen.rentme.Models.SkillModel;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.Task.APIRequester;
import com.android.jianchen.rentme.Utils.Constants;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by emerald on 6/10/2017.
 */
public class ProjectCreate0Fragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private Context context;

    private ArrayList<SkillModel> skills;
    private SkillGalleryAdapter adapter;
    private RecyclerView recyclerView;

    private OnSkillSelectListener listener;

    public static ProjectCreate0Fragment newInstance(Context context, OnSkillSelectListener l) {
        ProjectCreate0Fragment fragment = new ProjectCreate0Fragment();
        fragment.context = context;
        //fragment.prepareData();
        fragment.listener = l;
        return fragment;
    }

    public ProjectCreate0Fragment() {
        skills = new ArrayList<>();
    }

    public void prepareData() {
        APIRequester requester = new APIRequester(Constants.API_ROOT_URL + Constants.API_SKILL_GET, null, this, this);
        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_create0, container, false);

        initViewVariables(view);

        return view;
    }

    private void initViewVariables(View view) {
        //((MainActivity)getContext()).setPageTitel("Select required skill");
        skills = new ArrayList<>();
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_project_create0);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new SkillGalleryAdapter(getContext(), skills);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new SkillGalleryAdapter.RecyclerTouchListener(getContext(), recyclerView, new SkillGalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SkillModel s = skills.get(position);
                listener.onSkillSelect(s);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareData();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            boolean status = response.getBoolean("status");
            if (status) {
                JSONArray jsonSkills = response.getJSONArray("data");
                for (int i = 0; i < jsonSkills.length(); i++) {
                    SkillModel skill = new SkillModel();
                    skill.setId(jsonSkills.getJSONObject(i).getInt("id"));
                    skill.setTitle(jsonSkills.getJSONObject(i).getString("title"));
                    skill.setPath(jsonSkills.getJSONObject(i).getString("preview"));

                    //skills.add(skill);
                    adapter.add(skill);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
