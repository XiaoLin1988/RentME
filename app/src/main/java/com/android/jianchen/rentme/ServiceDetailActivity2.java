package com.android.jianchen.rentme;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jianchen.rentme.Adapter.IntroAdapter;
import com.android.jianchen.rentme.Dialogs.ReviewDialog;
import com.android.jianchen.rentme.Interface.OnConfirmListener;
import com.android.jianchen.rentme.Interface.OnProjectCreateListener;
import com.android.jianchen.rentme.Models.ArrayModel;
import com.android.jianchen.rentme.Models.Intro;
import com.android.jianchen.rentme.Models.ObjectModel;
import com.android.jianchen.rentme.Models.ProjectModel;
import com.android.jianchen.rentme.Models.ReviewModel;
import com.android.jianchen.rentme.Models.ServiceModel;
import com.android.jianchen.rentme.RestAPI.ProjectClient;
import com.android.jianchen.rentme.RestAPI.RestClient;
import com.android.jianchen.rentme.RestAPI.ServiceClient;
import com.android.jianchen.rentme.Utils.Constants;
import com.android.jianchen.rentme.Utils.DialogUtil;
import com.android.jianchen.rentme.Utils.Utils;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

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
    private static OnProjectCreateListener projectListener;

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
    @Bind(R.id.rate_service_detail_score)
    ScaleRatingBar rateScore;
    @Bind(R.id.pager_service)
    ViewPager pagerService;
    @Bind(R.id.btn_service_buy)
    Button buyService;

    private IntroAdapter adapter;

    @BindString(R.string.error_load)
    String errLoad;
    @BindString(R.string.error_network)
    String errNetwork;

    private Toolbar toolbar;

    private int curpage = 0;
    private ArrayList<ReviewModel> reviews;

    private View transition_view;

    private PayPalConfiguration payConfig;

    public static void navigate(AppCompatActivity activity, View view, OnProjectCreateListener listener, ServiceModel service) {
        projectListener = listener;

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

        reviews = new ArrayList<>();

        payConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(Constants.PAYPAL_CLIENT_ID);

        prepareActionBar();
        initViews();
    }

    private void prepareActionBar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);

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
        rateScore.setRating((float)service.getReview_score());

        buyService.setOnClickListener(this);

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

    private void doPayment() {
        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(service.getBalance()), "USD", "Buy \"" + service.getTitle() + "\" service", PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payConfig);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, Constants.PAYPAL_REQUEST_CODE);
    }

    private void createProject() {
        final ProgressDialog dialog = DialogUtil.showProgressDialog(this, "Please wait while checking order");

        RestClient<ProjectClient> restClient = new RestClient<>();
        final ProjectClient projectClient = restClient.getAppClient(ProjectClient.class);

        Call<ObjectModel<Integer>> call = projectClient.createProject(Utils.retrieveUserInfo(this).getId(), service.getId());
        call.enqueue(new Callback<ObjectModel<Integer>>() {
            @Override
            public void onResponse(Call<ObjectModel<Integer>> call, retrofit2.Response<ObjectModel<Integer>> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body().getStatus()) {
                    Toast.makeText(ServiceDetailActivity2.this, "Creating project succeed", Toast.LENGTH_SHORT).show();

                    ProjectModel project = new ProjectModel();
                    project.setPr_id(response.body().getData());
                    project.setPr_stts(0);
                    project.setSkill_id(service.getSkill_id());
                    project.setSv_balance(service.getBalance());
                    project.setSv_id(service.getId());
                    project.setSv_preview(service.getPreview());
                    project.setSv_title(service.getTitle());
                    project.setTalent_id(service.getTalent_id());
                    project.setSv_detail(service.getDetail());
                    projectListener.onProjectCreate(project);

                    finish();
                } else {
                    Toast.makeText(ServiceDetailActivity2.this, errLoad, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObjectModel<Integer>> call, Throwable t) {
                dialog.dismiss();
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
            case R.id.btn_service_buy:
                DialogUtil.showConfirmDialog(this, "Do you really want to buy this service", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        doPayment();
                    }
                });
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == Constants.PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        /*
                        startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount));
                        */
                        createProject();
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                        DialogUtil.showAlertDialog(this, "Payment failed with some reasons");
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Payment cancelled by user", Toast.LENGTH_SHORT).show();

                createProject();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                DialogUtil.showAlertDialog(this, "An invalid Payment or PayPalConfiguration was submitted");
                Toast.makeText(this, "Payment cancelled by user", Toast.LENGTH_SHORT).show();
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }
}
