package com.android.jianchen.rentme.activity.me.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jianchen.rentme.model.rentme.EffectModel;
import com.android.jianchen.rentme.R;

import java.util.List;

/**
 * Created by emerald on 6/16/2017.
 */
public class EditEffectAdapter extends RecyclerView.Adapter<EditEffectAdapter.EffectViewHolder> {
    private List<EffectModel> filterList;
    private final OnFilterClickListener listener;

    public EditEffectAdapter(List<EffectModel> filterList, OnFilterClickListener listener) {
        this.filterList = filterList;
        this.listener = listener;
    }

    @Override
    public EffectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_effect, parent, false);
        return new EffectViewHolder(view);
    }

    public interface OnFilterClickListener {
        void onFilterClick(EffectModel effectModel);
    }

    @Override
    public void onBindViewHolder(EffectViewHolder holder, int position) {
        EffectModel item = filterList.get(position);
        holder.filter_thumb.setImageResource(item.getThumbId());
        holder.filter_name.setText(item.getName());

        holder.bind(filterList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class EffectViewHolder extends RecyclerView.ViewHolder {
        public ImageView filter_thumb;
        public TextView filter_name;
        public EffectViewHolder(View view) {
            super(view);
            filter_thumb = (ImageView)view.findViewById(R.id.img_row_effect_thumb);
            filter_name = (TextView)view.findViewById(R.id.txt_row_effect_title);
        }

        public void bind(final EffectModel effectModel, final OnFilterClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onFilterClick(effectModel);
                }
            });
        }
    }
}
