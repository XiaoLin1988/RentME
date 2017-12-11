package com.android.jianchen.rentme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jianchen.rentme.Events.ProjectCompleteEvent;
import com.android.jianchen.rentme.Listener.SingleClickListener;
import com.android.jianchen.rentme.Models.ProjectModel;
import com.android.jianchen.rentme.ProjectCompleteDetailActivity;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.Utils.Constants;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.willy.ratingbar.ScaleRatingBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by emerald on 12/10/2017.
 */
public class CompleteRecyclerAdapter extends RecyclerView.Adapter<CompleteRecyclerAdapter.CompleteViewHolder> {
    private Context context;
    private ArrayList<ProjectModel> services;

    public CompleteRecyclerAdapter(Context context, ArrayList<ProjectModel> services) {
        this.context = context;
        this.services = services;

        EventBus.getDefault().register(this);
    }

    @Override
    public CompleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_project_completed, parent, false);

        return new CompleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompleteViewHolder holder, int position) {
        ProjectModel project = services.get(position);

        Glide.with(context).load(project.getSv_preview()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(holder.preview);

        holder.txtTitle.setText(project.getSv_title());
        holder.txtReview.setText(project.getSv_detail());

        holder.bindDetail(project);
    }

    public void add(ProjectModel p) {
        services.add(p);
        notifyDataSetChanged();
    }

    public void refreshData(ArrayList<ProjectModel> data) {
        services = data;
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ProjectCompleteEvent event) {
        services.add(event.getResult());
        notifyItemInserted(services.size() - 1);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class CompleteViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtReview;
        public RoundedImageView preview;
        public ScaleRatingBar rating;
        public LinearLayout lytParent;

        public CompleteViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView)itemView.findViewById(R.id.txt_project_complete_title);
            txtReview = (TextView)itemView.findViewById(R.id.txt_project_complete_review);
            preview = (RoundedImageView)itemView.findViewById(R.id.img_project_complete_preview);
            rating = (ScaleRatingBar)itemView.findViewById(R.id.rating_project_complete);
            lytParent = (LinearLayout)itemView.findViewById(R.id.lyt_parent);
        }

        public void bindDetail(final ProjectModel service) {
            lytParent.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    Intent intent = new Intent(context, ProjectCompleteDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_PROJECT_DETAIL, service);
                    context.startActivity(intent);
                }
            });
        }
    }
}
