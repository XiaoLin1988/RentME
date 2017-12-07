package com.android.emerald.rentme.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.emerald.rentme.Interface.OnImageAddListener;
import com.android.emerald.rentme.Listener.SingleClickListener;
import com.android.emerald.rentme.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by emerald on 11/12/2017.
 */
public class ImageUploadRecyclerAdapter extends RecyclerView.Adapter<ImageUploadRecyclerAdapter.ImageViewHolder> {
    private Context context;
    private ArrayList<Uri> uriList;
    private OnImageAddListener addListener;

    public ImageUploadRecyclerAdapter(Context ctx, ArrayList<Uri> ul) {
        context = ctx;
        uriList = ul;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_image_upload, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        if (uriList.size() == position + 1) {
            holder.rytAdd.setVisibility(View.VISIBLE);
            holder.rytItem.setVisibility(View.GONE);

            if (addListener != null) {
                holder.bindAddListener(addListener);
            }
        } else {
            holder.rytAdd.setVisibility(View.GONE);
            holder.rytItem.setVisibility(View.VISIBLE);

            Uri uri = uriList.get(position);
            Glide.with(context).load(uri).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(holder.imgPreview);

            holder.bindCancelListener(position);
        }
    }

    public void setAddListener(OnImageAddListener listener) {
        addListener = listener;
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public void add(Uri uri) {
        uriList.add(uriList.size() - 1, uri);
        notifyDataSetChanged();
    }

    public ArrayList<Uri> getUriList() {
        return uriList;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rytItem;
        public RelativeLayout rytAdd;
        public ImageView imgPreview;
        public ImageView imgCancel;

        public ImageViewHolder(View itemView) {
            super(itemView);
            rytItem = (RelativeLayout)itemView.findViewById(R.id.ryt_img_upload_item);
            imgPreview = (ImageView)itemView.findViewById(R.id.img_img_upload_preview);
            imgCancel = (ImageView)itemView.findViewById(R.id.img_img_upload_cancel);
            rytAdd = (RelativeLayout)itemView.findViewById(R.id.ryt_img_upload_add);
        }

        public void bindCancelListener(final int position) {
            imgCancel.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    uriList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        public void bindAddListener(final OnImageAddListener listener) {
            rytAdd.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    listener.onImageAdd();
                }
            });
        }
    }
}
