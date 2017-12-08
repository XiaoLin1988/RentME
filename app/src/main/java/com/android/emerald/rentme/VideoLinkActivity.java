package com.android.emerald.rentme;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.emerald.rentme.Utils.Constants;
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

    @Bind(R.id.btn_post)
    Button btnPost;

    WebView webVideo;

    private String webData = "";
    private String videoLink = "";

    private ProgressDialog dialog;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_videolink);
        ButterKnife.bind(this);

        prepareActionBar();
        initViews();
    }

    private void prepareActionBar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
    }

    private void initViews() {
        btnSubmit.setOnClickListener(this);
        btnPost.setOnClickListener(this);

        webVideo = (WebView)findViewById(R.id.web_video);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_videolink_fetch:
                if (validateLink()) {
                    loadVideo();
                }
                break;
            case R.id.btn_post:
                if (videoLink.equals("")) {
                    Toast.makeText(this, "Please validate your video link", Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_OK, getIntent().putExtra(Constants.KEY_VIDEO, videoLink));
                    finish();
                }
                break;
        }
    }

    private void loadVideo() {
        dialog = DialogUtil.showProgressDialog(VideoLinkActivity.this, "Please wait while loading video");

        videoLink = editLink.getText().toString();

        if (videoLink.startsWith("http://player.vimeo.com/video/")) {
            webData = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe src=\"%link%?autoplay=1&loop=1\" width=\"%width%\" height=\"%height%\" frameborder=\"0\"  style=\"outline:none;border:none;padding:0px;\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe></body></html>";
        } else if (videoLink.startsWith("https://www.youtube.com/watch?v=")) {
            webData = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe width=\"%width%\" height=\"%height%\" style=\"outline:none;border:none;padding:0px;\" src=\"%link%?&playsinline=1\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        }
        webData = webData.replace("%link%", videoLink);

        ViewTreeObserver viewTreeObserver = webVideo.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    webVideo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    webData = webData.replace("%width%", String.valueOf(webVideo.getWidth()));
                    webData = webData.replace("%width%", String.valueOf(webVideo.getHeight()));

                    webVideo.clearHistory();
                    webVideo.loadData(webData, "text/html", "UTF-8");
                }
            });
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
