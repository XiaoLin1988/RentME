package com.android.emerald.rentme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.emerald.rentme.Models.ProjectModel;
import com.android.emerald.rentme.ProjectProgressDetailActivity;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.Utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.Gson;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by emerald on 6/5/2017.
 */
public class ProjectInProgressListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ProjectModel> projects;

    public ProjectInProgressListAdapter(Context context, ArrayList<ProjectModel> projects) {
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

        convertView = LayoutInflater.from(context).inflate(R.layout.row_project_progress, parent, false);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProjectProgressDetailActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(project);
                intent.putExtra(Constants.EXTRA_PROJECT_DETAIL, json);
                context.startActivity(intent);
            }
        });

        TextView txtTitle = (TextView)convertView.findViewById(R.id.txt_project_progress_title);
        txtTitle.setText(project.getName());

        final TextView txtDescription = (TextView)convertView.findViewById(R.id.txt_project_progress_description);
        txtDescription.setText(project.getDescription());
        ViewTreeObserver vto = txtDescription.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = txtDescription.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (txtDescription.getLineCount() > 2) {
                    int lineEndIndex = txtDescription.getLayout().getLineEnd(1);
                    String text = txtDescription.getText().subSequence(0, lineEndIndex - 2) + " ...";
                    txtDescription.setText(text);
                }
            }
        });

        txtDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProjectProgressDetailActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(project);
                intent.putExtra(Constants.EXTRA_PROJECT_DETAIL, json);
                context.startActivity(intent);
            }
        });

        //RoundedImageView preview = (RoundedImageView)convertView.findViewById(R.id.img_project_progress_preview);
        ImageView preview = (ImageView) convertView.findViewById(R.id.img_project_progress_preview);
        Picasso.with(context).load(project.getPreview()).resize(300,200).centerCrop().into(preview);
        /*
        Glide.with(context).load(project.getPreview())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(preview);
        */
        return convertView;
    }

    public void add (ProjectModel p) {
        projects.add(p);
        notifyDataSetChanged();
    }
}
