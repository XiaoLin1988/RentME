package com.android.jianchen.rentme.activity.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jianchen.rentme.helper.delegator.OnSkillSelectListener;
import com.android.jianchen.rentme.helper.listener.SingleClickListener;
import com.android.jianchen.rentme.model.rentme.SkillModel;
import com.android.jianchen.rentme.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emerald on 6/10/2017.
 */
public class SkillGalleryAdapter extends RecyclerView.Adapter<SkillGalleryAdapter.MyViewHolder> {
    private List<SkillModel> skills;
    private Context mContext;
    private int lastPosition = -1;
    private OnSkillSelectListener skillSelectListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView title;
        public LinearLayout lytParent;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.img_row_skill_thumbnail);
            title = (TextView) view.findViewById(R.id.txt_row_skill_name);
            lytParent = (LinearLayout)view.findViewById(R.id.lyt_parent);
        }

        public void bindSelect(final SkillModel skill) {
            lytParent.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    skillSelectListener.onSkillSelect(skill);
                }
            });
        }
    }

    public SkillGalleryAdapter(Context context, List<SkillModel> skills) {
        mContext = context;
        this.skills = skills;
    }

    public void setOnSkillSelectListener(OnSkillSelectListener listener) {
        skillSelectListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_skill_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SkillModel skill = skills.get(position);
        Glide.with(mContext).load(skill.getPath())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);
        holder.title.setText(skill.getTitle());

        setAnimation(holder.itemView, position);
        holder.bindSelect(skill);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public void add(SkillModel s) {
        skills.add(s);

        notifyDataSetChanged();
    }

    public void refreshData(ArrayList<SkillModel> data) {
        skills = data;
        notifyDataSetChanged();
    }
}
