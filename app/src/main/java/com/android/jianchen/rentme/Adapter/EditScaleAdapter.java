package com.android.jianchen.rentme.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jianchen.rentme.Models.Effect;
import com.android.jianchen.rentme.R;

import java.util.List;

public class EditScaleAdapter extends RecyclerView.Adapter<EditScaleAdapter.EditScaleViewHolder>  {
    private final OnEffectClickListener listener;

    public List<Effect> effectList;

    private int curSel = -1;

    public EditScaleAdapter(List<Effect> effectList, OnEffectClickListener listener) {
        this.listener = listener;
        this.effectList = effectList;
    }

    public interface OnEffectClickListener {
        void onEffectClick(Effect effect);
    }

    @Override
    public EditScaleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_scale, parent, false);
        return new EditScaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EditScaleViewHolder holder, int position) {

        Effect effect = effectList.get(position);
        if (position == curSel) {
            holder.subeffect_thumb.setImageResource(effect.getSelThumbId());
            holder.subeffect_name.setTextColor(Color.rgb(0, 0, 0));
        } else {
            holder.subeffect_thumb.setImageResource(effect.getThumbId());
            holder.subeffect_name.setTextColor(Color.parseColor("#999999"));
        }
        holder.subeffect_name.setText(effect.getName());
        holder.bind(effectList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return effectList.size();
    }

    public class EditScaleViewHolder extends RecyclerView.ViewHolder {
        public ImageView subeffect_thumb;
        public TextView subeffect_name;

        public EditScaleViewHolder(View view) {
            super(view);
            subeffect_thumb = (ImageView)view.findViewById(R.id.img_row_scale_thumb);
            subeffect_name = (TextView)view.findViewById(R.id.txt_row_scale_title);
        }

        public void bind(final Effect effect, final OnEffectClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    curSel = getPosition();
                    notifyDataSetChanged();
                    listener.onEffectClick(effect);
                }
            });
        }
    }
}
