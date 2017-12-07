package com.android.emerald.rentme.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.android.emerald.rentme.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by emerald on 12/4/2017.
 */
public class IntroFragment extends Fragment {

    private ImageView imgIntro;
    private WebView webIntro;
    private AVLoadingIndicatorView loading;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_intro, container, false);

        initFragment(view);

        return view;
    }

    private void initFragment(View view) {
        imgIntro = (ImageView)view.findViewById(R.id.img_intro);
        webIntro = (WebView)view.findViewById(R.id.web_intro);

        webIntro.getSettings().setJavaScriptEnabled(true);
        webIntro.getSettings().setAppCacheEnabled(true);
        webIntro.getSettings().setDomStorageEnabled(true);
        webIntro.getSettings().setPluginState(WebSettings.PluginState.ON);

        webIntro.loadUrl("http://player.vimeo.com/video/24577973?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800");
    }

}
