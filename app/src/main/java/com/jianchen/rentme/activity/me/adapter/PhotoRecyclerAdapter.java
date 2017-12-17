package com.jianchen.rentme.activity.me.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jianchen.rentme.R;
import com.jianchen.rentme.helper.delegator.OnConfirmListener;
import com.jianchen.rentme.helper.listener.SingleClickListener;
import com.jianchen.rentme.helper.utils.DialogUtil;

import java.util.ArrayList;

/**
 * Created by emerald on 12/8/2017.
 */
public class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.PhotoViewHolder> {
    private Context context;
    private ArrayList<String> paths;

    public PhotoRecyclerAdapter(ArrayList<String> pathArray) {
        paths = pathArray;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_photo, parent, false);

        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        String path = paths.get(position);
        holder.bindLoadPhoto(path);
        holder.bindPostCancel(position);
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public void addItem(String path) {
        paths.add(path);
        notifyItemInserted(paths.size() - 1);
    }

    public void refreshData(ArrayList<String> pathArray) {
        paths = pathArray;
        notifyDataSetChanged();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageView removeBtn;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            removeBtn = (ImageView)itemView.findViewById(R.id.row_remove_btn);
        }

        public void bindLoadPhoto(final String path) {
            if (path != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(bitmap);
            }
        }

        public void bindPostCancel(final int position) {
            removeBtn.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    DialogUtil.showConfirmDialog(context, "Remove this photo?", new OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            paths.remove(position);
                            notifyItemRemoved(position);
                        }
                    });
                }
            });
        }
    }



}
