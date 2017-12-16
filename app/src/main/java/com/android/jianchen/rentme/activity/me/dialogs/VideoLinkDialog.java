package com.android.jianchen.rentme.activity.me.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jianchen.rentme.helper.delegator.OnPostVideoListener;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.helper.utils.DialogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by emerald on 12/8/2017.
 */
public class VideoLinkDialog extends Dialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.edit_videolink)
    EditText editLink;
    @Bind(R.id.txt_video_place)
    TextView txtVideoPlace;
    @Bind(R.id.radio_youtube)
    AppCompatRadioButton radioYoutube;
    @Bind(R.id.radio_vimeo)
    AppCompatRadioButton radioVimeo;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;

    @Bind(R.id.btn_post)
    Button btnPost;

    WebView webVideo;

    private boolean validate = false;
    private int videoType = 1;

    private String webData = "";
    private String videoLink = "";
    private OnPostVideoListener videoListener;

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
        btnPost.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(this);

        txtVideoPlace.setOnClickListener(this);

        editLink.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validate = false;
                btnPost.setText("Validate Video");
            }
        });

        webVideo = (WebView)findViewById(R.id.web_video);
        webVideo.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        webVideo.getSettings().setJavaScriptEnabled(true);
    }

    public void setVideoListener(OnPostVideoListener listener) {
        videoListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.txt_video_place:
                editLink.setFocusableInTouchMode(true);
                editLink.requestFocus();
                break;
            case R.id.btn_post:
                /*
                String URL_REGEX = "^((https?|ftp)://|(youtube|vimeo)|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
                Pattern p = Pattern.compile(URL_REGEX);
                Matcher m = p.matcher(editLink.getText().toString());
                if (!m.find()) {
                    Toast.makeText(getContext(), "Please input validate video url", Toast.LENGTH_SHORT).show();
                    return;
                }
                */
                if (editLink.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please input your video code", Toast.LENGTH_SHORT).show();
                }
                if (!validate) {
                    loadVideo();
                } else {
                    if (videoListener != null) {
                        if (videoType == 1)
                            videoLink = "https://www.youtube.com/embed/" + videoLink;
                        else if (videoType == 2)
                            videoLink = "https://player.vimeo.com/video/" + videoLink;
                        videoListener.onPostVideo(videoLink);
                    }
                    dismiss();
                }
                break;
        }
    }

    private void loadVideo() {
        //ProgressDialog dialog = DialogUtil.showProgressDialog(getContext(), "Please wait while loading video");

        videoLink = editLink.getText().toString();
        if (videoType == 1) {  // Youtube
            webData = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/%link%?autoplay=1&showinfo=0&rel=0&loop=1\" frameborder=\"0\" gesture=\"media\" allow=\"encrypted-media\" allowfullscreen></iframe></body></html>";
        } else if (videoType == 2) {
            webData = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe src=\"https://player.vimeo.com/video/%link%\" width=\"100%\" height=\"100%\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe></body></html>";
        }
        webData = webData.replace("%link%", videoLink);
        webData = webData.replace("%width%", "100%");
        webData = webData.replace("%height%", "100%");

        webVideo.loadData(webData, "text/html", "UTF-8");

        validate = true;
        btnPost.setText("Add Video");
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        switch (id) {
            case R.id.radio_youtube:
                videoType = 1;
                txtVideoPlace.setText("https://www.youtube.com/watch?v=");
                break;
            case R.id.radio_vimeo:
                videoType = 2;
                txtVideoPlace.setText("https://vimeo.com/");
                break;
        }
    }
}
