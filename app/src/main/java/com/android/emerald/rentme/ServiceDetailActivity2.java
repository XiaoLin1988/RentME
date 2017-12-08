package com.android.emerald.rentme;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emerald.rentme.Adapter.IntroAdapter;
import com.android.emerald.rentme.Dialogs.ReviewDialog;
import com.android.emerald.rentme.Models.ArrayModel;
import com.android.emerald.rentme.Models.Intro;
import com.android.emerald.rentme.Models.ReviewModel;
import com.android.emerald.rentme.Models.ServiceModel;
import com.android.emerald.rentme.RestAPI.RestClient;
import com.android.emerald.rentme.RestAPI.ServiceClient;
import com.android.emerald.rentme.Task.APIRequester;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.DialogUtil;
import com.android.emerald.rentme.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by emerald on 12/4/2017.
 */
public class ServiceDetailActivity2 extends AppCompatActivity implements View.OnClickListener {
    private ServiceModel service;

    @Bind(R.id.txt_service_skill)
    TextView txtSkill;
    @Bind(R.id.txt_service_title)
    TextView txtTitle;
    @Bind(R.id.txt_service_price)
    TextView txtPrice;
    @Bind(R.id.txt_review_content)
    TextView txtReviewContent;
    @Bind(R.id.txt_review_name)
    TextView txtReviewName;
    @Bind(R.id.txt_review_date)
    TextView txtReviewDate;
    @Bind(R.id.txt_service_description)
    ReadMoreTextView txtDescription;
    @Bind(R.id.lyt_review_container)
    LinearLayout lytReview;
    @Bind(R.id.img_review_avatar)
    CircularImageView imgReviewAvatar;
    @Bind(R.id.ryt_read_reviews)
    RelativeLayout rytReadAll;
    @Bind(R.id.pager_service)
    ViewPager pagerService;
    private IntroAdapter adapter;

    @BindString(R.string.error_load)
    String errLoad;
    @BindString(R.string.error_network)
    String errNetwork;

    private Toolbar toolbar;

    private int curpage = 0;
    private ArrayList<ReviewModel> reviews;

    private View transition_view;

    public static void navigate(AppCompatActivity activity, View view, ServiceModel service) {
        Intent intent = new Intent(activity, ServiceDetailActivity2.class);
        intent.putExtra(Constants.EXTRA_SERVICE_DETAIL, service);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, Constants.KEY_TRANSITION);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_service_detail2);
        ButterKnife.bind(this);

        transition_view = findViewById(android.R.id.content);

        service = (ServiceModel)getIntent().getSerializableExtra(Constants.EXTRA_SERVICE_DETAIL);
        ViewCompat.setTransitionName(transition_view, Constants.KEY_TRANSITION);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        reviews = new ArrayList<>();

        prepareActionBar();
        initViews();
    }

    private void prepareActionBar() {
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(null);

        Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    private void initViews() {
        txtSkill.setText(service.getSkill_title());
        txtTitle.setText(service.getTitle());
        txtPrice.setText(Integer.toString(service.getBalance()));
        txtDescription.setText(service.getDetail());

        rytReadAll.setOnClickListener(this);

        getServiceReviews();

        ArrayList<Intro> introList = new ArrayList<>();

        Intro intro1 = new Intro();
        intro1.setType(1);
        intro1.setLink("https://www.youtube.com/watch?v=7bDLIV96LD4");
        introList.add(intro1);

        Intro intro2 = new Intro();
        intro2.setType(2);
        intro2.setLink("http://player.vimeo.com/video/24577973?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1");
        introList.add(intro2);

        Intro intro3 = new Intro();
        intro3.setType(3);
        intro3.setLink("http://player.vimeo.com/video/24577973?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1");
        introList.add(intro3);

        adapter = new IntroAdapter(this, introList);
        pagerService.setAdapter(adapter);

        //adapter = new IntroAdapter(this, introList);
        //mei
        /*
        webService.getSettings().setJavaScriptEnabled(true);
        webService.getSettings().setAppCacheEnabled(true);
        webService.getSettings().setDomStorageEnabled(true);
        webService.getSettings().setPluginState(WebSettings.PluginState.ON);
        webService.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        //webData = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe src=\"%l\" width=\"%w\" height=\"%h\" frameborder=\"0\"  style=\"outline:none;border:none;padding:0px;\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe></body></html>";
        webData = "<html><head></head><body style=\"padding:0px;margin:0px;\"><iframe width=\"%w\" height=\"%h\" style=\"outline:none;border:none;padding:0px;\" src=\"%l\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        //webData = webData.replace("%l", "https://player.vimeo.com/video/217153389?autoplay=1&loop=1");
        webData = webData.replace("%l", "https://www.youtube.com/embed/13b-EuBNE6w?&playsinline=1");
        ViewTreeObserver vto = webService.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    webService.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    webService.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                //webData = webData.replace("%w", webService.getMeasuredWidth() + "px");
                //webData = webData.replace("%h", webService.getMeasuredHeight() + "px");
                webData = webData.replace("%w", "100%");
                webData = webData.replace("%h", "100%");
                webService.loadData(webData, "text/html", "UTF-8");
            }
        });
        //webService.loadUrl("http://player.vimeo.com/video/24577973?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800");
        */
    }

    private void getServiceReviews() {
        //final ProgressDialog dialog = DialogUtil.showProgressDialog(this, "Please wait while loading reviews");

        RestClient<ServiceClient> restClient = new RestClient<>();
        ServiceClient serviceClient = restClient.getAppClient(ServiceClient.class);

        Call<ArrayModel<ReviewModel>> call = serviceClient.getServiceReview(service.getId(), curpage);
        call.enqueue(new Callback<ArrayModel<ReviewModel>>() {
            @Override
            public void onResponse(Call<ArrayModel<ReviewModel>> call, retrofit2.Response<ArrayModel<ReviewModel>> response) {
                //dialog.dismiss();
                if (response.isSuccessful()) {
                    ArrayModel<ReviewModel> r = response.body();
                    reviews = r.getData();
                    if (reviews.size() > 0) {
                        txtReviewName.setText(reviews.get(0).getUser_name());
                        Date date = Utils.stringToDate(reviews.get(0).getRv_ctime());
                        txtReviewDate.setText(Utils.beautifyDate(date, false));
                        txtReviewContent.setText(reviews.get(0).getRv_content());
                        Glide.with(ServiceDetailActivity2.this).load(reviews.get(0).getUser_avatar()).asBitmap().centerCrop().placeholder(R.drawable.main).into(imgReviewAvatar);
                        lytReview.setVisibility(View.VISIBLE);
                    }
                    reviews.add(new ReviewModel());
                } else {
                    Toast.makeText(ServiceDetailActivity2.this, errLoad, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayModel<ReviewModel>> call, Throwable t) {
                //dialog.dismiss();
                Toast.makeText(ServiceDetailActivity2.this, errNetwork, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ryt_read_reviews:
                ReviewDialog dialog = new ReviewDialog(ServiceDetailActivity2.this, service.getId(), reviews);
                dialog.show();
                break;
        }
    }
}
