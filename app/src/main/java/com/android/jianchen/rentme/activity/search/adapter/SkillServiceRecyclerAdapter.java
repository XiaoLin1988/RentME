package com.android.jianchen.rentme.activity.search.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.jianchen.rentme.activity.me.adapter.ServiceRecyclerAdapter;
import com.android.jianchen.rentme.helper.delegator.OnServiceClickListener;
import com.android.jianchen.rentme.model.rentme.ServiceModel;
import com.android.jianchen.rentme.model.rentme.SkillServiceModel;
import com.android.jianchen.rentme.R;

import java.util.ArrayList;

public class SkillServiceRecyclerAdapter extends RecyclerView.Adapter<SkillServiceRecyclerAdapter.SkillServiceViewHolder> {
    private Context context;
    private ArrayList<SkillServiceModel> skills;
    private OnServiceClickListener listener;

    public SkillServiceRecyclerAdapter(Context context, ArrayList<SkillServiceModel> skills, OnServiceClickListener listener) {
        this.context = context;
        this.skills = skills;
        this.listener = listener;
    }

    @Override
    public SkillServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_service_item, parent, false);

        return new SkillServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SkillServiceViewHolder holder, int position) {
        SkillServiceModel service = skills.get(position);

        holder.txtSkill.setText(service.getSkill());
        holder.adapterService.refreshData(service.getServices());
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public void addService(ServiceModel service) {
        boolean added = false;
        for (int j = 0; j < skills.size(); j++ ){
            if (skills.get(j).getSkill().equals(service.getSkill_title())) {
                skills.get(j).getServices().add(service);
                added = true;
                break;
            }
        }
        if (!added) {
            SkillServiceModel skillService = new SkillServiceModel();
            skillService.setSkill(service.getSkill_title());;
            ArrayList<ServiceModel> ss = new ArrayList<ServiceModel>();
            ss.add(service);
            skillService.setServices(ss);
            skills.add(skillService);
        }
        notifyDataSetChanged();
    }


    public class SkillServiceViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSkill;
        public RecyclerView recyclerService;
        public ServiceRecyclerAdapter adapterService;

        public SkillServiceViewHolder(View itemView) {
            super(itemView);

            txtSkill = (TextView)itemView.findViewById(R.id.txt_row_service_skill);
            recyclerService = (RecyclerView)itemView.findViewById(R.id.recycler_row_service);
            adapterService = new ServiceRecyclerAdapter(context, new ArrayList<ServiceModel>(), listener);
            recyclerService.setAdapter(adapterService);
            recyclerService.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }
    }
}
