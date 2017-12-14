package com.android.jianchen.rentme.activity.myprojects.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.jianchen.rentme.activity.myprojects.adapter.CompleteRecyclerAdapter;
import com.android.jianchen.rentme.model.rentme.ArrayModel;
import com.android.jianchen.rentme.model.rentme.ProjectModel;
import com.android.jianchen.rentme.model.rentme.ServiceModel;
import com.android.jianchen.rentme.model.rentme.UserModel;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.helper.network.retrofit.ProjectClient;
import com.android.jianchen.rentme.helper.network.retrofit.RestClient;
import com.android.jianchen.rentme.helper.utils.Utils;
//import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by emerald on 6/5/2017.
 */
public class ProjectCompleteFragement extends Fragment {

    private Context context;

    private RecyclerView list;
    private CompleteRecyclerAdapter adapter;
    private ArrayList<ServiceModel> services = new ArrayList<>();

    private UserModel curUser;

    public static ProjectCompleteFragement newInstance(Context context) {
        ProjectCompleteFragement complete = new ProjectCompleteFragement();
        complete.context = context;

        return complete;
    }

    private void getCompletedProjects() {
        if (context == null)
            context = getContext();
        curUser = Utils.retrieveUserInfo(context);

        RestClient<ProjectClient> restClient = new RestClient<>();
        ProjectClient projectClient = restClient.getAppClient(ProjectClient.class);

        Call<ArrayModel<ProjectModel>> call = projectClient.getMyCompletedProjects(curUser.getId());
        call.enqueue(new Callback<ArrayModel<ProjectModel>>() {
            @Override
            public void onResponse(Call<ArrayModel<ProjectModel>> call, retrofit2.Response<ArrayModel<ProjectModel>> response) {
                if (response.isSuccessful() && response.body().getStatus()) {
                    adapter.refreshData(response.body().getData());
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.error_load), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayModel<ProjectModel>> call, Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_completed, container, false);
        initViewVariables(view);
        getCompletedProjects();
        return view;
    }

    private void initViewVariables(View view) {
        list = (RecyclerView) view.findViewById(R.id.list_project_completed);
        adapter = new CompleteRecyclerAdapter(getContext(), new ArrayList<ProjectModel>());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(context));
        list.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }
}
