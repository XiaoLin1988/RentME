package com.android.emerald.rentme.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.emerald.rentme.Models.WebLink;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.Utils.Constants;
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
        } else if (videoLinks.get(position).startsWith("http://www.youtube.com/")) {
            return Constants.VALUE_LINK_YOUTUBE;
        } else {
            return Constants.VALUE_LINK_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        String link = videoLinks.get(position);

        holder.bindLoadVideo(link, "100%", "100%");
    }

    @Override
    public int getItemCount() {
        return videoLinks.size();
    }

    public void addItem(String link) {
        videoLinks.add(link);
        notifyItemInserted(videoLinks.size() - 1);
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        public AVLoadingIndicatorView loading;
        public WebView webView;
        private String webData;

        public VideoViewHolder(View itemView, int viewType) {
            super(itemView);

            loading = (AVLoadingIndicatorView)itemView.findViewById(R.id.loading_indicator);

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
                webData = "<html><head></head><body style=\"padding:0px;margin:0px;\"><div class=\"video-shortcode\"><iframe title=\"YouTube video player\" width=\"%width%\" height=\"%height%\" src=\"%link%\" frameborder=\"0\" allowfullscreen></iframe></div></body></html>";
            } else if (viewType == Constants.VALUE_LINK_VIMEO) {
                webData = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe src=\"%link%?autoplay=1&loop=1\" width=\"%width%\" height=\"%height%\" frameborder=\"0\"  style=\"outline:none;border:none;padding:0px;\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe></body></html>";
            }
        }

        public void bindLoadVideo(String link, String width, String height) {
            webData = webData.replace("%link%", link);
            webData = webData.replace("%width%", width);
            webData = webData.replace("%height%", height);

            webView.loadData(webData, "text/html", "UTF-8");
        }
    }
}
