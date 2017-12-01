package com.android.emerald.rentme.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.emerald.rentme.Models.Effect;
import com.android.emerald.rentme.Models.ServiceModel;
import com.android.emerald.rentme.R;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ServiceRecyclerAdapter extends RecyclerView.Adapter<ServiceRecyclerAdapter.ServiceViewHolder>{
    private Context context;
    private List<ServiceModel> serviceList;
    private final OnServiceClickListener listener;

    public ServiceRecyclerAdapter(Context context, List<ServiceModel> serviceList, OnServiceClickListener listener) {
        this.context = context;
        this.serviceList = serviceList;
        this.listener = listener;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_service_item, parent, false);

        return new ServiceViewHolder(view);
    }

    public interface OnServiceClickListener {
        void onServiceClick(ServiceModel service);
    }

    SpannableString createIndentedText(String text, int marginFirstLine, int marginNextLines) {
        SpannableString result=new SpannableString(text);
        result.setSpan(new LeadingMarginSpan.Standard(marginFirstLine, marginNextLines),0,text.length(),0);
        return result;
    }

    @Override
    public void onBindViewHolder(final ServiceViewHolder holder, int position) {
        ServiceModel item = serviceList.get(position);
        if (!item.getPreview().equals(""))
            Picasso.with(context).load(item.getPreview()).into(holder.imgPreview);
        holder.txtBalance.setText("$" + Integer.toString(item.getBalance()));

        String str = "$" + Integer.toString(item.getBalance());
        holder.txtDetail.setText(createIndentedText(item.getDetail(), str.length() * 20 + 10, 0));

        holder.rating.setRating((float) item.getScore());
        holder.txtReviews.setText(Integer.toString(item.getReviews()) + " Reviews");
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
        public SimpleRatingBar rating;
        public TextView txtReviews;


        public ServiceViewHolder(View view) {
            super(view);
            imgPreview = (ImageView)view.findViewById(R.id.img_card_service_preview);
            txtBalance = (TextView)view.findViewById(R.id.txt_card_service_balance);
            txtDetail = (TextView)view.findViewById(R.id.txt_card_service_detail);
            rating = (SimpleRatingBar)view.findViewById(R.id.rating_card_service);
            txtReviews = (TextView)view.findViewById(R.id.txt_card_service_review);
        }

        public void bind(final ServiceModel service, final OnServiceClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onServiceClick(service);
                }
            });
        }
    }
}