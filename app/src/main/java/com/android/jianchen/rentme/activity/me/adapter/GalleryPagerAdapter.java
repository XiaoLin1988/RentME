package com.android.jianchen.rentme.activity.me.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.jianchen.rentme.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by emerald on 12/15/2017.
 */
public class GalleryPagerAdapter  extends PagerAdapter {
    private Context context;
    private ArrayList<String> imageList;

    private ArrayList<String> removeList;
    private ArrayList<Uri> addList;

    public GalleryPagerAdapter(Context ctx, ArrayList<String> il) {
        context = ctx;
        imageList = il;

        removeList = new ArrayList<>();
        addList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==(ImageView) object;
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.card_gallery, container, false);

        Glide.with(context).load(imageList.get(position)).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into((ImageView)itemView);

        container.addView(itemView);

        return itemView;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView)object);
    }

    public void refreshData(ArrayList<String> il) {
        imageList = il;
        notifyDataSetChanged();
    }

    public void add(Uri mImageUri) {
        imageList.add(mImageUri.getPath());

        addList.add(mImageUri);

        notifyDataSetChanged();
    }

    public void remove(int pos) {
        String image = imageList.get(pos);
        remove(image);
    }

    public void remove(String image) {
        if (image.startsWith("/storage")) {
            for (int i = 0; i < addList.size(); i++) {
                if (addList.get(i).getPath().equals(image)) {
                    addList.remove(i);
                    break;
                }
            }
        } else {
            removeList.add(image);
        }

        imageList.remove(image);
        notifyDataSetChanged();
    }
}
