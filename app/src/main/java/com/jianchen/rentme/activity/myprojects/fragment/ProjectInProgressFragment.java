package com.jianchen.rentme.activity.myprojects.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jianchen.rentme.activity.myprojects.adapter.ProgressRecyclerAdapter;
import com.jianchen.rentme.model.rentme.ArrayModel;
import com.jianchen.rentme.model.rentme.ProjectModel;
import com.jianchen.rentme.model.rentme.UserModel;
import com.jianchen.rentme.R;
import com.jianchen.rentme.helper.network.retrofit.ProjectClient;
import com.jianchen.rentme.helper.network.retrofit.RestClient;
import com.jianchen.rentme.helper.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by emerald on 6/5/2017.
 */
public class ProjectInProgressFragment extends Fragment {

    private Context context;
    private UserModel curUser;

    private RecyclerView list;
    private ProgressRecyclerAdapter adapter;
    private ArrayList<ProjectModel> projects;

    private AVLoadingIndicatorView loading;

    public static ProjectInProgressFragment newInstance(Context context) {
        ProjectInProgressFragment progress = new ProjectInProgressFragment();
        progress.context = context;
        return progress;
    }

    private void getProgressProjects() {
        if (context == null)
            context = getContext();
        curUser = Utils.retrieveUserInfo(context);

        RestClient<ProjectClient> restClient = new RestClient<>();
        ProjectClient projectClient = restClient.getAppClient(ProjectClient.class);

        Call<ArrayModel<ProjectModel>> call = projectClient.getMyProgressProjects(curUser.getId());
        call.enqueue(new Callback<ArrayModel<ProjectModel>>() {
            @Override
            public void onResponse(Call<ArrayModel<ProjectModel>> call, retrofit2.Response<ArrayModel<ProjectModel>> response) {
                loading.hide();
                loading.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body().getStatus()) {
                    adapter.refreshData(response.body().getData());
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.error_load), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayModel<ProjectModel>> call, Throwable t) {
                loading.hide();
                loading.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);

                Toast.makeText(context, context.getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_progress, container, false);

        loading = (AVLoadingIndicatorView)view.findViewById(R.id.loading_indicator);
        loading.setVisibility(View.VISIBLE);
        loading.show();

        list = (RecyclerView) view.findViewById(R.id.list_project_progress);

        if (projects == null)
            projects = new ArrayList<>();

        adapter = new ProgressRecyclerAdapter(getContext(), new ArrayList<ProjectModel>());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(context));

        getProgressProjects();

        return view;
    }
}
