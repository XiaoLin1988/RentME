package com.android.jianchen.rentme.activity.me.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.activity.me.ServiceDetailActivity;
import com.android.jianchen.rentme.helper.delegator.OnConfirmListener;
import com.android.jianchen.rentme.helper.listener.SingleClickListener;
import com.android.jianchen.rentme.helper.utils.DialogUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by emerald on 12/8/2017.
 */
public class ReviewPhotoRecyclerAdapter extends RecyclerView.Adapter<ReviewPhotoRecyclerAdapter.PhotoViewHolder> {
    private Context context;
    private ArrayList<String> paths;

    public ReviewPhotoRecyclerAdapter(ArrayList<String> pathArray) {
        paths = pathArray;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review_photo, parent, false);

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

        public PhotoViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.img_review_photo);


        }

        public void bindLoadPhoto(final String path) {
            Glide.with(context).load(path).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imageView);
        }

        public void bindPostCancel(final int position) {
            imageView.setOnClickListener(new SingleClickListener() {
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
