package com.jianchen.rentme.activity.me.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianchen.rentme.model.photoedit.EffectModel;
import com.jianchen.rentme.R;

import java.util.List;

/**
 * Created by emerald on 6/16/2017.
 */
public class EditDoodleAdapter extends RecyclerView.Adapter<EditDoodleAdapter.ItemViewHolder>{
    private List<EffectModel> itemList;

    private final OnItemClickListener listener;

    public EditDoodleAdapter(List<EffectModel> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_effect, parent, false);
        return new ItemViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(EffectModel item);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        EffectModel item = itemList.get(position);
        holder.item_thumb.setImageResource(item.getThumbId());
        holder.item_title.setText(item.getName());
        holder.bind(itemList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView item_thumb;
        public TextView item_title;

        public ItemViewHolder(View view) {
            super(view);
            item_thumb = (ImageView)view.findViewById(R.id.img_row_effect_thumb);
            item_title = (TextView)view.findViewById(R.id.txt_row_effect_title);
        }

        public void bind(final EffectModel item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
