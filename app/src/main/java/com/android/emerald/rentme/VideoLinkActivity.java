package com.android.emerald.rentme;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emerald.rentme.Utils.DialogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by emerald on 12/6/2017.
 */
public class VideoLinkActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.edit_videolink)
    EditText editLink;

    @Bind(R.id.btn_videolink_fetch)
    Button btnSubmit;

    WebView webVideo;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_videolink);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        btnSubmit.setOnClickListener(this);

        webVideo = (WebView)findViewById(R.id.web_video);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_videolink_fetch:
                if (validateLink()) {
                    loadVideo();
                }
                break;
        }
    }

    private void loadVideo() {
        final ProgressDialog dialog = DialogUtil.showProgressDialog(VideoLinkActivity.this, "Please waiting while loading video");

        String link = editLink.getText().toString();

        String data = "";
        if (link.startsWith("http://player.vimeo.com/video/")) {
            data = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe src=\"%link%?autoplay=1&loop=1\" width=\"100%\" height=\"100%\" frameborder=\"0\"  style=\"outline:none;border:none;padding:0px;\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe></body></html>";
        } else if (link.startsWith("https://www.youtube.com/watch?v=")) {
            data = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe width=\"100%\" height=\"100%\" style=\"outline:none;border:none;padding:0px;\" src=\"%link%?&playsinline=1\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        }
        data = data.replace("%link%", link);

        webVideo.clearHistory();
        webVideo.getSettings().setJavaScriptEnabled(true);
        webVideo.getSettings().setAppCacheEnabled(true);
        webVideo.getSettings().setDomStorageEnabled(true);
        webVideo.getSettings().setPluginState(WebSettings.PluginState.ON);
        webVideo.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
                super.onPageFinished(view, url);
            }
        });
        //webVideo.loadData(data, "text/html", "UTF-8");
        webVideo.loadUrl("http://baidu.com");
    }

    private boolean validateLink() {
        if (editLink.getText().toString().contains("youtube")) {
            if (!editLink.getText().toString().startsWith("https://www.youtube.com/watch?v=")) {
                DialogUtil.showAlertDialog(VideoLinkActivity.this, "https://www.youtube.com/watch?v=");
            } else {
                return true;
            }
        } else if (editLink.getText().toString().contains("vimeo")) {
            if (!editLink.getText().toString().startsWith("http://player.vimeo.com/video/")) {
                DialogUtil.showAlertDialog(VideoLinkActivity.this, "http://player.vimeo.com/video/");
            } else {
                return true;
            }
        } else {
            Toast.makeText(VideoLinkActivity.this, "Please input Youtube or Vimeo link", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
