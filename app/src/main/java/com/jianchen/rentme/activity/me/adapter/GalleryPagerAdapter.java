package com.jianchen.rentme.activity.me.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jianchen.rentme.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by emerald on 12/15/2017.
 */
public class GalleryPagerAdapter  extends PagerAdapter {
    private Context context;
    private ArrayList<String> imageList;
    private ArrayList<ImageView> viewList;

    private ArrayList<String> removeList;
    private ArrayList<Uri> addList;
    private ViewPager viewPager;

    public GalleryPagerAdapter(Context ctx, ViewPager pager, ArrayList<String> il) {
        context = ctx;
        imageList = il;
        viewList = new ArrayList<>();

        viewPager = pager;

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
        viewList.add((ImageView) itemView);

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

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void remove(int pos) {
        String image = imageList.get(pos);
        remove(image);

        notifyDataSetChanged();
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

        for (int i = 0; i < imageList.size(); i ++) {
            if (imageList.get(i).equals(image)) {
                viewPager.setAdapter(null);
                imageList.remove(i);
                viewList.remove(i);
                viewPager.setAdapter(this);
                break;
            }
        }
    }
}
