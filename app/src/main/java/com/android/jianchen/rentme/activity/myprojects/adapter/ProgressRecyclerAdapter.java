package com.android.jianchen.rentme.activity.myprojects.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jianchen.rentme.activity.myprojects.events.ProjectCompleteEvent;
import com.android.jianchen.rentme.helper.listener.SingleClickListener;
import com.android.jianchen.rentme.model.rentme.ProjectModel;
import com.android.jianchen.rentme.activity.myprojects.ProjectProgressDetailActivity;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.helper.Constants;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by emerald on 12/10/2017.
 */
public class ProgressRecyclerAdapter extends RecyclerView.Adapter<ProgressRecyclerAdapter.ProgressViewHolder> {
    private Context context;
    private ArrayList<ProjectModel> services;
    private int selected = -1;
    private int lastPosition = -1;

    public ProgressRecyclerAdapter(Context context, ArrayList<ProjectModel> services) {
        this.context = context;
        this.services = services;

        EventBus.getDefault().register(this);
    }

    @Override
    public ProgressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_project_progress, parent, false);

        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProgressViewHolder holder, int position) {
        ProjectModel service = services.get(position);

        holder.txtTitle.setText(service.getSv_title());
        holder.txtDescription.setText(service.getSv_detail());
        Glide.with(context).load(service.getSv_preview()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(holder.preview);

        setAnimation(holder.itemView, position);
        holder.bindDetail(service, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ProjectCompleteEvent event) {
        services.remove(selected);
        notifyItemRemoved(selected);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public void refreshData(ArrayList<ProjectModel> data) {
        services = data;
        notifyDataSetChanged();
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtDescription;
        public RoundedImageView preview;
        public LinearLayout lytParent;

        public ProgressViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView)itemView.findViewById(R.id.txt_project_progress_title);
            txtDescription = (TextView)itemView.findViewById(R.id.txt_project_progress_description);
            preview = (RoundedImageView)itemView.findViewById(R.id.img_project_progress_preview);
            lytParent = (LinearLayout)itemView.findViewById(R.id.lyt_parent);
        }

        public void bindDetail(final ProjectModel service, final int pos) {
            lytParent.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    selected = pos;

                    Intent intent = new Intent(context, ProjectProgressDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_PROJECT_DETAIL, service);

                    context.startActivity(intent);
                }
            });
        }
    }
}
