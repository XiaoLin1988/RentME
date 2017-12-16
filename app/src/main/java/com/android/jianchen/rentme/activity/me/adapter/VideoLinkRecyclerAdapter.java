package com.android.jianchen.rentme.activity.me.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.android.jianchen.rentme.helper.delegator.OnConfirmListener;
import com.android.jianchen.rentme.helper.listener.SingleClickListener;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.helper.Constants;
import com.android.jianchen.rentme.helper.utils.DialogUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by emerald on 12/8/2017.
 */
public class VideoLinkRecyclerAdapter extends RecyclerView.Adapter<VideoLinkRecyclerAdapter.VideoViewHolder> {
    private Context context;
    private ArrayList<String> videoLinks;

    public VideoLinkRecyclerAdapter(ArrayList<String> vl) {
        videoLinks = vl;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video, parent, false);

        return new VideoViewHolder(v, viewType);
    }

    public int getItemViewType(int position) {
        if (videoLinks.get(position).startsWith("http://player.vimeo.com/video/")) {
            return Constants.VALUE_LINK_VIMEO;
        } else {
            return Constants.VALUE_LINK_YOUTUBE;
        }
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        String link = videoLinks.get(position);

        holder.bindLoadVideo(link, "100%", "100%");
        holder.bindPostCancel(position);
    }

    @Override
    public int getItemCount() {
        return videoLinks.size();
    }

    public void addItem(String link) {
        videoLinks.add(link);
        notifyItemInserted(videoLinks.size() - 1);
    }

    public void refreshData(ArrayList<String> vl) {
        videoLinks = vl;
        notifyDataSetChanged();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        public AVLoadingIndicatorView loading;
        public WebView webView;
        private String webData;
        private ImageView imgRemove;

        public VideoViewHolder(View itemView, int viewType) {
            super(itemView);

            loading = (AVLoadingIndicatorView)itemView.findViewById(R.id.loading_indicator);
            imgRemove = (ImageView)itemView.findViewById(R.id.row_post_remove);

            webView = (WebView)itemView.findViewById(R.id.web_video);
            webView.setWebViewClient(new WebViewClient(){
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    loading.hide();
                    loading.setVisibility(View.GONE);
                    super.onPageFinished(view, url);
                }
            });
            webView.getSettings().setJavaScriptEnabled(true);

            if (viewType == Constants.VALUE_LINK_YOUTUBE) {
                webData = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe width=\"100%\" height=\"100%\" src=\"%link%?autoplay=1&showinfo=0&rel=0&loop=1\" frameborder=\"0\" gesture=\"media\" allow=\"encrypted-media\" allowfullscreen></iframe></body></html>";
            } else if (viewType == Constants.VALUE_LINK_VIMEO) {
                webData = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe src=\"%link%\" width=\"100%\" height=\"100%\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe></body></html>";
            }
        }

        public void bindLoadVideo(String link, String width, String height) {
            webData = webData.replace("%link%", link);
            webData = webData.replace("%width%", width);
            webData = webData.replace("%height%", height);

            webView.loadData(webData, "text/html", "UTF-8");
        }

        public void bindPostCancel(final int position) {
            imgRemove.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    DialogUtil.showConfirmDialog(context, "Remove this video?", new OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            videoLinks.remove(position);
                            notifyItemRemoved(position);
                        }
                    });
                }
            });
        }
    }
}
