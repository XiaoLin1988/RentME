package com.android.emerald.rentme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.emerald.rentme.Listener.SingleClickListener;
import com.android.emerald.rentme.Models.Intro;
import com.android.emerald.rentme.Models.WebLink;
import com.android.emerald.rentme.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by emerald on 12/8/2017.
 */
public class WebLinkRecyclerAdapter extends RecyclerView.Adapter<WebLinkRecyclerAdapter.WebLinkViewHolder> {
    private Context context;
    private ArrayList<WebLink> webLinks;

    public WebLinkRecyclerAdapter(ArrayList<WebLink> wl) {
        webLinks = wl;
    }

    @Override
    public WebLinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_web_link, parent, false);

        return new WebLinkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WebLinkViewHolder holder, int position) {
        WebLink webLink = webLinks.get(position);

        Glide.with(context).load(webLink.getThumbnail()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(holder.imgThumb);
        holder.txtTitle.setText(webLink.getTitle());
        holder.txtUrl.setText(webLink.getLink());

        holder.bindLinkView(webLink);
    }

    @Override
    public int getItemCount() {
        return webLinks.size();
    }

    public void addItem(WebLink webLink) {
        webLinks.add(webLink);
        notifyItemInserted(webLinks.size() - 1);
    }

    public class WebLinkViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgThumb;
        private TextView txtTitle;
        private TextView txtUrl;

        public WebLinkViewHolder(View itemView) {
            super(itemView);

            imgThumb = (ImageView)itemView.findViewById(R.id.img_link_thumbnail);
            txtTitle = (TextView)itemView.findViewById(R.id.txt_link_title);
            txtUrl = (TextView)itemView.findViewById(R.id.txt_link_url);
        }

        public void bindLinkView(final WebLink webLink) {
            itemView.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webLink.getLink()));
                    context.startActivity(browserIntent);
                }
            });
        }
    }
}
