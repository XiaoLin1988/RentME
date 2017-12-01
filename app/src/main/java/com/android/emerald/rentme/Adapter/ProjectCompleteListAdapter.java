package com.android.emerald.rentme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.emerald.rentme.Models.ProjectModel;
import com.android.emerald.rentme.Models.UserModel;
import com.android.emerald.rentme.ProjectCompleteDetailActivity;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.Gson;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by emerald on 6/5/2017.
 */
public class ProjectCompleteListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProjectModel> projects;

    public ProjectCompleteListAdapter(Context context, ArrayList<ProjectModel> projects) {
        this.context = context;
        this.projects = projects;
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public ProjectModel getItem(int position) {
        return projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ProjectModel project = projects.get(position);

        UserModel curUser = Utils.retrieveUserInfo(context);

        convertView = LayoutInflater.from(context).inflate(R.layout.row_project_completed, parent, false);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProjectCompleteDetailActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(project);
                intent.putExtra(Constants.EXTRA_PROJECT_DETAIL, json);
                context.startActivity(intent);
            }
        });

        //RoundedImageView preview = (RoundedImageView)convertView.findViewById(R.id.img_project_complete_preview);
        ImageView preview = (ImageView)convertView.findViewById(R.id.img_project_complete_preview);

        Picasso.with(context).load(project.getPreview()).resize(300,200).centerCrop().into(preview);

        /*
        Glide.with(context).load(project.getPreview())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(preview);
        */
        //both talent and consumer left reviews
        if (project.getConsumer_score() != 0 && project.getTalent_score() != 0) {
            TextView txtTitle = (TextView)convertView.findViewById(R.id.txt_project_complete_title);
            txtTitle.setText(project.getName());

            SimpleRatingBar ratingBar = (SimpleRatingBar)convertView.findViewById(R.id.rating_project_complete);
            final TextView txtReview = (TextView)convertView.findViewById(R.id.txt_project_complete_review);

            if (curUser.getType() == 1) {
                ratingBar.setRating(project.getConsumer_score());
            }
            else if (curUser.getType() == 2) {
                ratingBar.setRating(project.getTalent_score());
            } else {
                ratingBar.setRating(0);
            }
            txtReview.setText(project.getDescription());
            ViewTreeObserver vto = txtReview.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver obs = txtReview.getViewTreeObserver();
                    obs.removeGlobalOnLayoutListener(this);
                    if (txtReview.getLineCount() > 2) {
                        int lineEndIndex = txtReview.getLayout().getLineEnd(1);
                        String text = txtReview.getText().subSequence(0, lineEndIndex - 2) + " ...";
                        txtReview.setText(text);
                    }
                }
            });

            txtReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProjectCompleteDetailActivity.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(project);
                    intent.putExtra(Constants.EXTRA_PROJECT_DETAIL, json);
                    context.startActivity(intent);
                }
            });
        } else {
            TextView txtTitle = (TextView)convertView.findViewById(R.id.txt_project_complete_title);
            txtTitle.setText(project.getName());

            SimpleRatingBar ratingBar = (SimpleRatingBar)convertView.findViewById(R.id.rating_project_complete);
            ratingBar.setRating(0);

            final TextView txtReview = (TextView)convertView.findViewById(R.id.txt_project_complete_review);
            txtReview.setText(project.getDescription());
            ViewTreeObserver vto = txtReview.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver obs = txtReview.getViewTreeObserver();
                    obs.removeGlobalOnLayoutListener(this);
                    if (txtReview.getLineCount() > 2) {
                        int lineEndIndex = txtReview.getLayout().getLineEnd(1);
                        String text = txtReview.getText().subSequence(0, lineEndIndex - 2) + " ...";
                        txtReview.setText(text);
                    }
                }
            });

            txtReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProjectCompleteDetailActivity.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(project);
                    intent.putExtra(Constants.EXTRA_PROJECT_DETAIL, json);
                    context.startActivity(intent);
                }
            });

            /*
            convertView = LayoutInflater.from(context).inflate(R.layout.row_project_progress, parent, false);

            TextView txtTitle = (TextView)convertView.findViewById(R.id.txt_project_progress_title);
            txtTitle.setText(project.getName());

            TextView txtDescription = (TextView)convertView.findViewById(R.id.txt_project_progress_description);
            txtDescription.setText(project.getDescription());

            TextView txtTimeframe = (TextView)convertView.findViewById(R.id.txt_project_progress_timeframe);
            txtTimeframe.setText(Integer.toString(project.getTimeframe()));
            */
        }

        return convertView;
    }

    public void add(ProjectModel p) {
        projects.add(p);
        notifyDataSetChanged();
    }
}
