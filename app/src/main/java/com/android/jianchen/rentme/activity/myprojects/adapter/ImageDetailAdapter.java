package com.android.jianchen.rentme.activity.myprojects.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.jianchen.rentme.activity.myprojects.customviews.ZoomImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageDetailAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> urls;

    public ImageDetailAdapter(Context context, ArrayList<String> us) {
        this.context = context;
        this.urls = us;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View)object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        ZoomImageView imageView = new ZoomImageView(context);
        imageView.setLayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
        /*
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        */
        imageView.setZoomEnable(true);
        Glide.with(context).load(urls.get(position)).asBitmap().into(imageView);
        container.addView(imageView);
        return imageView;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
