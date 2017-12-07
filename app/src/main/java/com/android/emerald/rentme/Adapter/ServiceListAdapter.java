package com.android.emerald.rentme.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.emerald.rentme.Models.ServiceModel;
import com.android.emerald.rentme.Models.SkillServiceModel;
import com.android.emerald.rentme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emerald on 6/23/2017.
 */
public class ServiceListAdapter extends BaseAdapter {
    private Context context;
    private List<SkillServiceModel> services;
    private ServiceRecyclerAdapter.OnServiceClickListener listener;

    public ServiceListAdapter (Context context, List<SkillServiceModel> services, ServiceRecyclerAdapter.OnServiceClickListener listener) {
        this.context = context;
        this.services = services;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public SkillServiceModel getItem(int position) {
        return services.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SkillServiceModel serviceModel = services.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.row_service_item, parent, false);

        TextView txtSkill = (TextView)convertView.findViewById(R.id.txt_row_service_skill);
        txtSkill.setText(serviceModel.getSkill());

        RecyclerView recycler = (RecyclerView)convertView.findViewById(R.id.recycler_row_service);
        ServiceRecyclerAdapter adapter = new ServiceRecyclerAdapter(context, serviceModel.getServices(), listener);
        recycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(layoutManager);
        return convertView;
    }

    public void addService(ServiceModel service) {
        boolean added = false;
        for (int j = 0; j < services.size(); j++ ){
            if (services.get(j).getSkill().equals(service.getSkill_title())) {
                services.get(j).getServices().add(service);
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
            services.add(skillService);
        }
        notifyDataSetChanged();
    }
}
