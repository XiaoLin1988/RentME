package com.android.emerald.rentme;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.emerald.rentme.Adapter.IntroAdapter;
import com.android.emerald.rentme.Models.Intro;
import com.android.emerald.rentme.Models.ServiceModel;
import com.android.emerald.rentme.Task.APIRequester;
import com.android.emerald.rentme.Utils.Constants;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.borjabravo.readmoretextview.ReadMoreTextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by emerald on 12/4/2017.
 */
public class ServiceDetailActivity2 extends AppCompatActivity {
    private ServiceModel service;

    private TextView txtSkill;
    private TextView txtTitle;
    private TextView txtBalance;
    private TextView txtReviewContent;
    private TextView txtReviewName;

    private ReadMoreTextView txtDescription;

    @Bind(R.id.pager_service)
    ViewPager pagerService;
    private IntroAdapter adapter;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_service_detail2);
        ButterKnife.bind(this);

        service = (ServiceModel)getIntent().getSerializableExtra(Constants.EXTRA_SERVICE_DETAIL);

        initViews();
    }

    private void initViews() {
        txtSkill = (TextView)findViewById(R.id.txt_service_skill);
        txtSkill.setText(service.getSkill_title());

        txtTitle = (TextView)findViewById(R.id.txt_service_title);
        txtTitle.setText(service.getTitle());

        txtBalance = (TextView)findViewById(R.id.txt_service_price);
        txtBalance.setText(Integer.toString(service.getBalance()));

        txtDescription = (ReadMoreTextView)findViewById(R.id.txt_service_description);
        txtDescription.setText(service.getDetail());

        getServiceReviews();

        ArrayList<Intro> introList = new ArrayList<>();

        Intro intro1 = new Intro();
        intro1.setType(1);
        intro1.setLink("https://www.youtube.com/watch?v=7bDLIV96LD4");
        introList.add(intro1);

        Intro intro2 = new Intro();
        intro2.setType(2);
        intro2.setLink("http://player.vimeo.com/video/24577973?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800");
        introList.add(intro2);

        Intro intro3 = new Intro();
        intro3.setType(3);
        intro3.setLink("https://www.google.com/");
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
        //DialogUtil.showProgressDialog(getBaseContext(), "Please wait while loading");

        String  url = Constants.API_ROOT_URL + Constants.API_SERVICE_REVIEW;
        Map<String, String> params = new HashMap<>();
        params.put("service_id", Integer.toString(service.getId()));

        final APIRequester requester = new APIRequester(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(requester);
    }
}
