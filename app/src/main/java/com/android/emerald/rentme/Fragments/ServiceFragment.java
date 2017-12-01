package com.android.emerald.rentme.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.emerald.rentme.Adapter.ServiceListAdapter;
import com.android.emerald.rentme.Adapter.ServiceRecyclerAdapter;
import com.android.emerald.rentme.AppController;
import com.android.emerald.rentme.MainActivity;
import com.android.emerald.rentme.Models.ServiceModel;
import com.android.emerald.rentme.Models.SkillServiceModel;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.ServiceDetailActivity;
import com.android.emerald.rentme.Task.APIRequester;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by emerald on 6/29/2017.
 */
public class ServiceFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject>,ServiceRecyclerAdapter.OnServiceClickListener {
    private Context context;

    private ListView listServices;
    private ServiceListAdapter adapter;

    private List<SkillServiceModel> services;

    public static ServiceFragment newInstance(Context ctx) {
        ServiceFragment fragment = new ServiceFragment();
        fragment.context = ctx;
        //fragment.getTalentServices();
        return fragment;
    }

    public ServiceFragment () {
        services = new ArrayList<>();
        adapter = new ServiceListAdapter(context, services, this);
    }

    private void getTalentServices() {
        Map<String, String> params = new HashMap<>();
        params.put("talentid", Integer.toString(Utils.retrieveUserInfo(context).getId()));

        String url = Constants.API_ROOT_URL + Constants.API_USER_SERVICE;

        APIRequester requester = new APIRequester(Request.Method.POST, url, params, this, this);
        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getContext()).setPageTitel("My Services");
        View view = inflater.inflate(R.layout.fragment_myservices, container, false);

        initViewVariables(view);

        getTalentServices();

        return view;
    }

    private void initViewVariables(View view) {
        listServices = (ListView)view.findViewById(R.id.list_myservices);
        adapter = new ServiceListAdapter(context, services, this);
        listServices.setAdapter(adapter);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONArray array = response.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                ServiceModel service = new ServiceModel();
                service.setId(obj.getInt("id"));
                service.setTitle(obj.getString("title"));
                service.setTalent_id(obj.getInt("talent_id"));
                service.setSkill(obj.getString("skill"));
                service.setPreview(obj.getString("preview"));
                service.setBalance(obj.getInt("balance"));
                service.setDetail(obj.getString("detail"));
                if (!obj.getString("review_score").equals("null"))
                    service.setScore(obj.getDouble("review_score"));
                else
                    service.setScore(0);
                service.setReviews(obj.getInt("review_cnt"));

                adapter.addService(service);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceClick(ServiceModel item) {
        Gson gson = new Gson();
        String json =  gson.toJson(item);
        Intent intent = new Intent(getContext(), ServiceDetailActivity.class);
        intent.putExtra(Constants.EXTRA_SERVICE_DETAIL, json);

        startActivity(intent);
    }
}
