package com.android.emerald.rentme.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.emerald.rentme.Models.Intro;
import com.android.emerald.rentme.R;
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
    private ArrayList<Intro> introList;

    public IntroAdapter(Context ctx, ArrayList<Intro> ll) {
        context = ctx;
        introList = ll;
    }

    @Override
    public int getCount() {
        return introList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout)object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.card_intro, container, false);

        final ImageView imgIntro = (ImageView)itemView.findViewById(R.id.img_intro);
        WebView webIntro = (WebView)itemView.findViewById(R.id.web_intro);
        final AVLoadingIndicatorView loading = (AVLoadingIndicatorView)itemView.findViewById(R.id.loading_content);
        loading.show();

        Intro intro = introList.get(position);
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
        } else {
            webIntro.setVisibility(View.VISIBLE);

            webIntro.setWebViewClient(new HelloWebViewClient());
            webIntro.getSettings().setJavaScriptEnabled(true);
            webIntro.getSettings().setAppCacheEnabled(true);
            webIntro.getSettings().setDomStorageEnabled(true);
            webIntro.loadUrl(intro.getLink());
        }

        return itemView;
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.setVisibility(View.GONE);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
        }
    }
}
