package com.android.emerald.rentme.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.emerald.rentme.Interface.OnServiceClickListener;
import com.android.emerald.rentme.Listener.SingleClickListener;
import com.android.emerald.rentme.Models.ServiceModel;
import com.android.emerald.rentme.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Line;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.List;

public class ServiceRecyclerAdapter extends RecyclerView.Adapter<ServiceRecyclerAdapter.ServiceViewHolder>{
    private Context context;
    private ArrayList<ServiceModel> serviceList;
    private final OnServiceClickListener listener;

    public ServiceRecyclerAdapter(Context context, ArrayList<ServiceModel> serviceList, OnServiceClickListener listener) {
        this.context = context;
        this.serviceList = serviceList;
        this.listener = listener;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_service_item, parent, false);

        return new ServiceViewHolder(view);
    }

    SpannableString createIndentedText(String text, int marginFirstLine, int marginNextLines) {
        SpannableString result=new SpannableString(text);
        result.setSpan(new LeadingMarginSpan.Standard(marginFirstLine, marginNextLines),0,text.length(),0);
        return result;
    }

    public void refreshData(ArrayList<ServiceModel> sl) {
        serviceList = sl;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ServiceViewHolder holder, int position) {
        ServiceModel item = serviceList.get(position);
        if (!item.getPreview().equals(""))
            Glide.with(context).load(item.getPreview()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(holder.imgPreview);
        holder.txtBalance.setText("$" + Integer.toString(item.getBalance()));

        String str = "$" + Integer.toString(item.getBalance());
        holder.txtDetail.setText(createIndentedText(item.getDetail(), str.length() * 20 + 10, 0));

        holder.rating.setRating((float) item.getReview_score());
        holder.txtReviews.setText(Integer.toString(item.getReview_cnt()));
        holder.bind(serviceList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPreview;
        public TextView txtBalance;
        public TextView txtDetail;
        public ScaleRatingBar rating;
        public TextView txtReviews;
        public LinearLayout lytParent;

        public ServiceViewHolder(View view) {
            super(view);
            imgPreview = (ImageView)view.findViewById(R.id.img_card_service_preview);
            txtBalance = (TextView)view.findViewById(R.id.txt_card_service_balance);
            txtDetail = (TextView)view.findViewById(R.id.txt_card_service_detail);
            rating = (ScaleRatingBar)view.findViewById(R.id.rating_card_service);
            txtReviews = (TextView)view.findViewById(R.id.txt_card_service_review);
            lytParent = (LinearLayout)view.findViewById(R.id.lyt_parent);
        }

        public void bind(final ServiceModel service, final OnServiceClickListener listener) {
            lytParent.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    listener.onServiceClick(v, service);
                }
            });
        }
    }
}