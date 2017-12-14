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
public class EditFrameAdapter extends RecyclerView.Adapter<EditFrameAdapter.FrameViewHolder> {
    private List<EffectModel> frameList;

    private final OnFrameClickListener listener;

    public EditFrameAdapter(List<EffectModel> frameList, OnFrameClickListener listener) {
        this.frameList = frameList;
        this.listener = listener;
    }

    public interface OnFrameClickListener {
        void onFrameClick(EffectModel item);
    }

    @Override
    public FrameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_effect, parent, false);
        return new FrameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FrameViewHolder holder, int position) {
        EffectModel item = frameList.get(position);
        holder.frame_thumb.setImageResource(item.getThumbId());
        holder.bind(frameList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return frameList.size();
    }

    public class FrameViewHolder extends RecyclerView.ViewHolder {
        public ImageView frame_thumb;
        public TextView frame_title;

        public FrameViewHolder(View view) {
            super(view);
            frame_thumb = (ImageView)view.findViewById(R.id.img_row_effect_thumb);
            frame_title = (TextView)view.findViewById(R.id.txt_row_effect_title);
        }

        public void bind(final EffectModel item, final OnFrameClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onFrameClick(item);
                }
            });
        }
    }
}
