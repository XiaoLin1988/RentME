package com.android.emerald.rentme.Dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.emerald.rentme.Interface.OnPostVideoListener;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.DialogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by emerald on 12/8/2017.
 */
public class VideoLinkDialog extends Dialog implements View.OnClickListener {
    @Bind(R.id.edit_videolink)
    EditText editLink;

    @Bind(R.id.btn_videolink_fetch)
    Button btnSubmit;

    @Bind(R.id.btn_post)
    Button btnPost;

    WebView webVideo;

    private String webData = "";
    private String videoLink = "";
    private OnPostVideoListener videoListener;
    private ProgressDialog dialog;

    public VideoLinkDialog(Context context) {
        super(context);

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_videolink);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationScale;

        ButterKnife.bind(this);

        initDialog();
    }

    private void initDialog() {
        findViewById(R.id.img_close).setOnClickListener(this);
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

    public void setVideoListener(OnPostVideoListener listener) {
        videoListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                if (videoListener != null)
                    videoListener.onPostVideo(videoLink);
                dismiss();
                break;
            case R.id.btn_videolink_fetch:
                if (validateLink()) {
                    loadVideo();
                }
                break;
            case R.id.btn_post:
                if (videoLink.equals("")) {
                    Toast.makeText(getContext(), "Please validate your video link", Toast.LENGTH_SHORT).show();
                } else {
                    if (videoListener != null)
                        videoListener.onPostVideo(videoLink);
                    dismiss();
                }
                break;
        }
    }

    private void loadVideo() {
        dialog = DialogUtil.showProgressDialog(getContext(), "Please wait while loading video");

        videoLink = editLink.getText().toString();

        if (videoLink.startsWith("http://player.vimeo.com/video/")) {
            webData = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe src=\"%link%?autoplay=1&loop=1\" width=\"%width%\" height=\"%height%\" frameborder=\"0\"  style=\"outline:none;border:none;padding:0px;\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe></body></html>";
        } else if (videoLink.startsWith("https://www.youtube.com/watch?v=")) {
            webData = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe width=\"%width%\" height=\"%height%\" style=\"outline:none;border:none;padding:0px;\" src=\"%link%?&playsinline=1\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        }
        webData = webData.replace("%link%", videoLink);
        webData = webData.replace("%width%", String.valueOf(100));
        webData = webData.replace("%height%", String.valueOf(100));
        webVideo.loadData(webData, "text/html", "UTF-8");
    }

    private boolean validateLink() {
        if (editLink.getText().toString().contains("youtube")) {
            if (!editLink.getText().toString().startsWith("http://www.youtube.com/")) {
                DialogUtil.showAlertDialog(getContext(), "http://www.youtube.com/");
            } else {
                return true;
            }
        } else if (editLink.getText().toString().contains("vimeo")) {
            if (!editLink.getText().toString().startsWith("http://player.vimeo.com/video/")) {
                DialogUtil.showAlertDialog(getContext(), "http://player.vimeo.com/video/");
            } else {
                return true;
            }
        } else {
            Toast.makeText(getContext(), "Please input Youtube or Vimeo link", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
