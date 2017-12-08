package com.android.emerald.rentme;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emerald.rentme.Models.Intro;
import com.android.emerald.rentme.Utils.Constants;
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

    @Bind(R.id.btn_post)
    Button btnPost;

    @Bind(R.id.lyt_web_content)
    LinearLayout lytWebContent;

    private TextCrawler textCrawler;

    private String webLink = "";

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_weblink);
        ButterKnife.bind(this);

        textCrawler = new TextCrawler();

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
        /*
        Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        */
        //supportInvalidateOptionsMenu();
    }

    private void initViews() {
        btnSubmit.setOnClickListener(this);
        btnPost.setOnClickListener(this);
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
            case R.id.btn_post:
                if (webLink.equals("")) {
                    Toast.makeText(WebLinkActivity.this, "Please validate your link", Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_OK, getIntent().putExtra(Constants.KEY_WEB, webLink));
                    finish();
                }
                break;
        }
    }

    private void fetchWebData(final String query) {
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
                    webLink = query;

                    lytWebContent.setVisibility(View.VISIBLE);
                    List<String> images = sourceContent.getImages();
                    if (images.size() > 0) {
                        Glide.with(WebLinkActivity.this).load(images.get(0)).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgThumb);
                    }

                    txtWebTitle.setText(sourceContent.getTitle());
                    txtWebContent.setText(sourceContent.getDescription());
                } else {
                    lytWebContent.setVisibility(View.GONE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_toolbar, menu);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setIconified(false);
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
        */
        return false;
    }
}