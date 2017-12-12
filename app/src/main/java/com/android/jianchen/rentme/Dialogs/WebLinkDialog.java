package com.android.jianchen.rentme.Dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jianchen.rentme.Interface.OnPostWebListener;
import com.android.jianchen.rentme.Models.WebLink;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.Utils.DialogUtil;
import com.bumptech.glide.Glide;
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by emerald on 12/8/2017.
 */
public class WebLinkDialog extends Dialog implements View.OnClickListener {
    @Bind(R.id.edit_weblink)
    EditText editLink;

    @Bind(R.id.img_weblink_thumb)
    ImageView imgThumb;

    @Bind(R.id.txt_weblink_title)
    TextView txtWebTitle;

    @Bind(R.id.txt_weblink_content)
    TextView txtWebContent;

    @Bind(R.id.btn_post)
    Button btnPost;

    @Bind(R.id.lyt_web_content)
    LinearLayout lytWebContent;

    @Bind(R.id.img_web_left)
    ImageView imgLeft;

    @Bind(R.id.img_web_right)
    ImageView imgRight;

    private WebLink webLink;
    private OnPostWebListener webListener;
    private TextCrawler textCrawler;
    private boolean validate = false;
    private List<String> images;
    private int position = 0;

    public WebLinkDialog(Context context) {
        super(context);

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_weblink);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationScale;

        ButterKnife.bind(this);

        textCrawler = new TextCrawler();
        webLink = new WebLink();

        initDialog();
    }

    public void setWebListener(OnPostWebListener listener) {
        webListener = listener;
    }

    private void initDialog() {
        findViewById(R.id.img_close).setOnClickListener(this);

        imgLeft.setOnClickListener(this);
        imgRight.setOnClickListener(this);

        btnPost.setOnClickListener(this);

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
                btnPost.setText("Validate Link");
            }
        });
    }

    public void onStop() {
        super.onStop();
        textCrawler.cancel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.img_web_left:
                if (position == 0)
                    return;
                position --;
                Glide.with(getContext()).load(images.get(position)).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgThumb);
                webLink.setThumbnail(images.get(position));
                break;
            case R.id.img_web_right:
                if (images.size() > position + 1) {
                    position ++;
                    Glide.with(getContext()).load(images.get(position)).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgThumb);
                    webLink.setThumbnail(images.get(position));
                }
                break;
            case R.id.btn_post:
                if (!validate) {
                    String query = editLink.getText().toString();
                    if (!query.equals("")) {
                        fetchWebData(query);
                    }
                } else {
                    if (webListener != null) {
                        webListener.onPostWeb(webLink);
                    }
                    dismiss();
                }
                break;
        }
    }

    private void fetchWebData(final String query) {
        textCrawler.makePreview(new LinkPreviewCallback() {
            ProgressDialog prgDialog;
            @Override
            public void onPre() {
                prgDialog = DialogUtil.showProgressDialog(getContext(), "Please wait while validating web link");
            }

            @Override
            public void onPos(SourceContent sourceContent, boolean b) {
                prgDialog.dismiss();
                if (sourceContent.isSuccess()) {
                    validate = true;
                    btnPost.setText("Add Link");
                    webLink.setLink(query);

                    lytWebContent.setVisibility(View.VISIBLE);
                    position = 0;
                    images = sourceContent.getImages();
                    if (images.size() > 0) {
                        webLink.setThumbnail(images.get(0));
                        Glide.with(getContext()).load(images.get(0)).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgThumb);
                    }

                    webLink.setTitle(sourceContent.getTitle());
                    webLink.setContent(sourceContent.getDescription());

                    txtWebTitle.setText(sourceContent.getTitle());
                    txtWebContent.setText(sourceContent.getDescription());
                } else {
                    validate = false;
                    lytWebContent.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Web link validation failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, query);
    }
}
