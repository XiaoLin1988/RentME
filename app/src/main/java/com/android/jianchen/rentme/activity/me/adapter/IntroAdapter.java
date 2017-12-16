package com.android.jianchen.rentme.activity.me.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.android.jianchen.rentme.helper.Constants;
import com.android.jianchen.rentme.model.rentme.IntroModel;
import com.android.jianchen.rentme.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by emerald on 12/4/2017.
 */
public class IntroAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<IntroModel> introModelList;

    public IntroAdapter(Context ctx, ArrayList<IntroModel> ll) {
        context = ctx;
        introModelList = ll;
    }

    @Override
    public int getCount() {
        return introModelList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.card_intro, container, false);
        container.addView(itemView);

        final ImageView imgIntro = (ImageView)itemView.findViewById(R.id.img_intro);
        final WebView webIntro = (WebView)itemView.findViewById(R.id.web_intro);
        final AVLoadingIndicatorView loading = (AVLoadingIndicatorView)itemView.findViewById(R.id.loading_content);
        loading.show();

        IntroModel intro = introModelList.get(position);
        if (intro.getType() == 0) { // Image
            Glide.with(context).load(intro.getLink()).asBitmap().into(new SimpleTarget<Bitmap>(400, 400) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    loading.hide();
                    loading.setVisibility(View.GONE);

                    imgIntro.setVisibility(View.VISIBLE);
                    imgIntro.setImageBitmap(resource);
                }
            });
        } else if (intro.getType() == 2){
            String data = "";
            String link = (String)intro.getLink();
            if (link.startsWith("https://player.vimeo.com/")) {
                data = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe width=\"100%\" height=\"100%\" src=\"%link%\" frameborder=\"0\" gesture=\"media\" allow=\"encrypted-media\" allowfullscreen></iframe></body></html>";
            } else if (link.startsWith("https://www.youtube.com")) {
                data = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe src=\"%link%?autoplay=1&showinfo=0&rel=0&loop=1\" width=\"100%\" height=\"100%\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe></body></html>";
            }
            data = data.replace("%link%", link);
            data = data.replace("%width%", "100%");
            data = data.replace("%height%", "100%");

            webIntro.clearHistory();
            webIntro.setVisibility(View.VISIBLE);
            webIntro.getSettings().setJavaScriptEnabled(true);
            webIntro.setWebViewClient(new WebViewClient(){
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    loading.hide();
                    loading.setVisibility(View.GONE);
                    webIntro.setVisibility(View.VISIBLE);
                    super.onPageFinished(view, url);
                }
            });
            webIntro.loadData(data, "text/html", "utf-8");
        }

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
}
