package com.android.emerald.rentme;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emerald.rentme.Utils.DialogUtil;
import com.bumptech.glide.Glide;
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by emerald on 12/6/2017.
 */
public class WebLinkActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.edit_weblink)
    EditText editLink;

    @Bind(R.id.btn_weblink_fetch)
    Button btnSubmit;

    @Bind(R.id.img_weblink_thumb)
    ImageView imgThumb;

    @Bind(R.id.txt_weblink_title)
    TextView txtWebTitle;

    @Bind(R.id.txt_weblink_content)
    TextView txtWebContent;

    private TextCrawler textCrawler;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_weblink);
        ButterKnife.bind(this);

        textCrawler = new TextCrawler();

        initViews();
    }

    private void initViews() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_weblink_fetch:
                String query = editLink.getText().toString();
                if (!query.equals("")) {
                    if (!query.startsWith("http")) {
                        Toast.makeText(WebLinkActivity.this, "Please input web link", Toast.LENGTH_SHORT).show();
                    } else {
                        fetchWebData(query);
                    }
                }
                break;
        }
    }

    private void fetchWebData(String query) {
        textCrawler.makePreview(new LinkPreviewCallback() {
            ProgressDialog prgDialog;
            @Override
            public void onPre() {
                prgDialog = DialogUtil.showProgressDialog(WebLinkActivity.this, "Please wait while validating web link");
            }

            @Override
            public void onPos(SourceContent sourceContent, boolean b) {
                prgDialog.dismiss();
                if (sourceContent.isSuccess()) {
                    List<String> images = sourceContent.getImages();
                    if (images.size() > 0) {
                        Glide.with(WebLinkActivity.this).load(images.get(0)).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgThumb);
                    }

                    txtWebTitle.setText(sourceContent.getTitle());
                    txtWebContent.setText(sourceContent.getDescription());
                } else {
                    Toast.makeText(WebLinkActivity.this, "Web link validation failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, query);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textCrawler.cancel();
    }
}
