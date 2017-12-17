package com.jianchen.rentme.activity.me;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jianchen.rentme.R;
import com.jianchen.rentme.activity.me.adapter.GalleryPagerAdapter;
import com.jianchen.rentme.activity.me.dialogs.EditProfileDialog;
import com.jianchen.rentme.helper.Constants;
import com.jianchen.rentme.helper.delegator.OnConfirmListener;
import com.jianchen.rentme.helper.delegator.OnDialogSelectListener;
import com.jianchen.rentme.helper.listener.LoadCompleteListener;
import com.jianchen.rentme.helper.network.retrofit.CommonClient;
import com.jianchen.rentme.helper.network.retrofit.RestClient;
import com.jianchen.rentme.helper.network.retrofit.UserClient;
import com.jianchen.rentme.helper.utils.DialogUtil;
import com.jianchen.rentme.helper.utils.InternalStorageContentProvider;
import com.jianchen.rentme.helper.utils.Utils;
import com.jianchen.rentme.model.rentme.ArrayModel;
import com.jianchen.rentme.model.rentme.ObjectModel;
import com.jianchen.rentme.model.rentme.UserModel;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.joanzapata.iconify.widget.IconTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener, EditProfileDialog.OnUpdateListener {
    @Bind(R.id.txt_profile_name)
    IconTextView txtName;

    @Bind(R.id.txt_profile_mood)
    IconTextView txtStatus;

    @Bind(R.id.txt_profile_location)
    IconTextView txtLocation;

    @Bind(R.id.txt_profile_earned)
    TextView txtEarned;

    @Bind(R.id.txt_profile_joined)
    TextView txtJoined;

    @Bind(R.id.img_profile_main)
    RoundedImageView imgMain;

    @Bind(R.id.pager_cover)
    ViewPager pagerCover;
    GalleryPagerAdapter adapterCover;

    @Bind(R.id.img_profile_email)
    ImageView imgEmail;

    @Bind(R.id.img_profile_facebook)
    ImageView imgFacebook;

    @Bind(R.id.img_profile_google)
    ImageView imgGoogle;

    @Bind(R.id.img_profile_wechat)
    ImageView imgWechat;

    @BindString(R.string.joined)
    String joined;
    @BindString(R.string.error_edit_user)
    String errEditUser;
    @BindString(R.string.error_network)
    String errNetwork;

    private Toolbar toolbar;

    private int TAG_NAME = 0;
    private int TAG_STATUS = 1;
    private int TAG_LOCATION = 2;

    private UserModel curUser;

    private File mFileTemp;
    private Uri mainImage;

    private int TAG_MAIN = 0;
    private int TAG_SUB = 1;
    private int TAG = -1;  // 0 : mainImage , 1 : subImage

    private boolean mainImageChanged = false;
    private boolean subImageChanged = false;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        curUser = Utils.retrieveUserInfo(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
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
    }

    private void initViews() {
        txtName.setOnClickListener(this);
        txtName.setText(curUser.getName() + " {fa-pencil}");

        txtStatus.setOnClickListener(this);
        if (curUser.getDescription() == null || curUser.getDescription().equals(""))
            txtStatus.setText("fa-pencil");
        else
            txtStatus.setText(curUser.getDescription() + " {fa-pencil}");

        if (curUser.getAddress() == null || curUser.getAddress().equals(""))
            txtLocation.setText("Address is not set. {fa-pencil}");
        else
            txtLocation.setText(curUser.getAddress() + " {fa-pencil}");
        txtLocation.setOnClickListener(this);

        if (curUser.getEarning() == 0) {
            txtEarned.setText("No earning");
        } else {
            txtEarned.setText(Utils.getUserEarning(curUser.getEarning()));
        }

        Date date = Utils.stringToDate(curUser.getCtime());
        txtJoined.setText(joined + " " + Utils.beautifyDate(date, false));

        if (curUser.getCoverImg() == null || curUser.getCoverImg().size() == 0) {
            ArrayList<String> arrayList = new ArrayList<String>();
            adapterCover = new GalleryPagerAdapter(this, pagerCover, arrayList);
            pagerCover.setAdapter(adapterCover);
        }
        else {
            adapterCover = new GalleryPagerAdapter(this, pagerCover, curUser.getCoverImg());
            pagerCover.setAdapter(adapterCover);
        }

        if (curUser.getEmail() == null || curUser.getEmail().equals("")) {
            imgEmail.setImageResource(R.drawable.email_u);
        } else {
            imgEmail.setImageResource(R.drawable.email_a);
        }

        if (curUser.getFbId() == null || curUser.getFbId().equals("")) {
            imgFacebook.setImageResource(R.drawable.facebook_u);
        } else {
            imgFacebook.setImageResource(R.drawable.facebook_a);
        }

        if (curUser.getGgId() == null || curUser.getGgId().equals("")) {
            imgGoogle.setImageResource(R.drawable.google_u);
        } else {
            imgGoogle.setImageResource(R.drawable.google_a);
        }

        if (curUser.getWxId() == null || curUser.getWxId().equals("")) {
            imgWechat.setImageResource(R.drawable.wechat_u);
        } else {
            imgWechat.setImageResource(R.drawable.wechat_a);
        }

        Glide.with(this).load(curUser.getAvatar()).asBitmap().centerCrop().placeholder(R.drawable.profile_empty).into(imgMain);
        imgMain.setOnClickListener(this);
    }

    private void completeProfile() {

        RestClient<UserClient> restClient = new RestClient<>();
        final ProgressDialog dialog = DialogUtil.showProgressDialog(this, "Please wait while updating personal information");

        int loadCount = 1;

        if (this.mainImageChanged) {
            loadCount = loadCount + 1;
        }

        if (this.subImageChanged) {
            loadCount = loadCount + 1;
        }

        final LoadCompleteListener loadListener = new LoadCompleteListener(loadCount) {
            @Override
            public void onLoaded() {
                Utils.saveUserInfo(PreviewActivity.this, curUser);
                setResult(RESULT_OK);
                dialog.dismiss();
                finish();
            }
        };

        if (this.mainImageChanged) { // if main profile image changed, upload new image

            RestClient<CommonClient> restClient1 = new RestClient<>();
            CommonClient commonClient = restClient1.getAppClient(CommonClient.class);

            ArrayList<MultipartBody.Part> images = new ArrayList<MultipartBody.Part>();

            File file = new File(this.mainImage.getPath());

            RequestBody reqImage = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("images[]", file.getName(), reqImage);
            images.add(body);

            RequestBody reqType = RequestBody.create(MultipartBody.FORM, Integer.toString(Constants.VALUE_PROFILEMAIN_PHOTO));
            RequestBody reqForeign_id = RequestBody.create(MultipartBody.FORM, Integer.toString(curUser.getId()));

            Call<ArrayModel<String>> call1 = commonClient.uploadPhotos(reqType, reqForeign_id, images);
            call1.enqueue(new Callback<ArrayModel<String>>() {
                @Override
                public void onResponse(Call<ArrayModel<String>> call, Response<ArrayModel<String>> response) {

                    if (response.isSuccessful() && response.body().getStatus()) {
                        curUser.setAvatar(response.body().getData().get(0));
                        loadListener.setLoaded();
                    } else {
                        Toast.makeText(PreviewActivity.this, "Uploading Profile Image failed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ArrayModel<String>> call, Throwable t) {
                    Toast.makeText(PreviewActivity.this, "Uploading Profile Image failed", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

        }


        if (this.subImageChanged) { // if sub profile image changed, upload new image

            RestClient<CommonClient> restClient1 = new RestClient<>();
            CommonClient commonClient = restClient1.getAppClient(CommonClient.class);

            ArrayList<MultipartBody.Part> images = new ArrayList<MultipartBody.Part>();

            String weblinkList = "";
            ArrayList<String> filePathList = new ArrayList<String>();

            for (int i = 0; i < adapterCover.getImageList().size(); i++) {

                if (adapterCover.getImageList().get(i).contains("http")) { // weblink
                    weblinkList = weblinkList + adapterCover.getImageList().get(i) + " ";
                }
                else { // filepath
                    filePathList.add(adapterCover.getImageList().get(i));
                }
            }

            for (int i = 0; i < filePathList.size(); i++) {

                File file = new File(filePathList.get(i));

                RequestBody reqImage = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("images[]", file.getName(), reqImage);
                images.add(body);
            }

            RequestBody reqType = RequestBody.create(MultipartBody.FORM, Integer.toString(Constants.VALUE_PROFILESUB_PHOTO));
            RequestBody reqForeign_id = RequestBody.create(MultipartBody.FORM, Integer.toString(curUser.getId()));
            RequestBody reqLinks = RequestBody.create(MultipartBody.FORM, weblinkList);

            Call<ArrayModel<String>> call1 = commonClient.updateSubPhotos(reqLinks, reqForeign_id, images);
            call1.enqueue(new Callback<ArrayModel<String>>() {
                @Override
                public void onResponse(Call<ArrayModel<String>> call, Response<ArrayModel<String>> response) {

                    if (response.isSuccessful() && response.body().getStatus()) {
                        curUser.setCoverImg(response.body().getData());
                        Utils.saveUserInfo(PreviewActivity.this, curUser);
                        loadListener.setLoaded();
                    } else {
                        Toast.makeText(PreviewActivity.this, "Uploading Profile SubImages failed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<ArrayModel<String>> call, Throwable t) {
                    Toast.makeText(PreviewActivity.this, "Uploading Profile SubImages failed", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

        }

        UserClient userClient = restClient.getAppClient(UserClient.class);

        Call<ObjectModel<Boolean>> call = userClient.editUser(curUser.getId(), curUser.getName(), curUser.getDescription(), curUser.getAddress());
        call.enqueue(new Callback<ObjectModel<Boolean>>() {
            @Override
            public void onResponse(Call<ObjectModel<Boolean>> call, Response<ObjectModel<Boolean>> response) {

                if (response.isSuccessful() && response.body().getStatus()) {
                    loadListener.setLoaded();
                } else {
                    Toast.makeText(PreviewActivity.this, errEditUser, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ObjectModel<Boolean>> call, Throwable t) {
                Toast.makeText(PreviewActivity.this, errNetwork, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private Map<String,RequestBody> createPartFromArray(String[] skills)
    {
        Map<String, RequestBody> skill = new HashMap<String, RequestBody>();
        RequestBody requestFile ;
        for(int i=0 ;i<skills.length;i++) {
            requestFile = RequestBody.create(MultipartBody.FORM,skills[i]);
            skill.put("skill["+i+"]", requestFile);
        }
        return skill;

    }

    private void createTempFile() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            String tmp = "IMG_" + Long.toString(System.currentTimeMillis()) + ".png";
            mFileTemp = new File(Environment.getExternalStorageDirectory(), tmp);
        } else {
            String tmp = "IMG_" + Long.toString(System.currentTimeMillis()) + ".png";
            mFileTemp = new File(getFilesDir(), tmp);
        }
    }

    public void selectImage() {
        CharSequence[] options = {getResources().getString(R.string.choose_camera), getResources().getString(R.string.choose_gallery)};
        DialogUtil.showSelectDialog(this, options, new OnDialogSelectListener() {
            @Override
            public void onDialogSelect(int position) {
                if (position == 0) {
                    if (!Utils.checkPermission(PreviewActivity.this, "android.permission.CAMERA") || !Utils.checkPermission(PreviewActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                        ActivityCompat.requestPermissions(PreviewActivity.this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"}, Constants.REQUEST_CAMERA);
                    } else {
                        takePhoto();
                    }
                } else {
                    if (!Utils.checkPermission(PreviewActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                        ActivityCompat.requestPermissions(PreviewActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, Constants.REQUEST_GALLERY);
                    } else {
                        choosePhoto();
                    }
                }
            }
        });
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        createTempFile();
        Uri mImageCaptureUri = null;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mImageCaptureUri = Uri.fromFile(mFileTemp);
        } else {
            mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        takePictureIntent.putExtra("return-data", true);
        startActivityForResult(takePictureIntent, Constants.REQUEST_CAMERA);
    }

    public void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        try {
            startActivityForResult(intent, Constants.REQUEST_GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No image source available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_profile_name) {
            EditProfileDialog dialog = new EditProfileDialog(PreviewActivity.this);
            dialog.setTitle("Please input your name");
            dialog.setUpdateListener(this);
            dialog.setTag(TAG_NAME);
            dialog.setDefault(curUser.getName());
            dialog.show();
        } else if (view.getId() == R.id.txt_profile_mood) {
            EditProfileDialog dialog = new EditProfileDialog(PreviewActivity.this);
            dialog.setTitle("Please input your status");
            dialog.setUpdateListener(this);
            dialog.setTag(TAG_STATUS);
            dialog.setDefault(curUser.getDescription());
            dialog.show();
        } else if (view.getId() == R.id.txt_profile_location) {
            EditProfileDialog dialog = new EditProfileDialog(PreviewActivity.this);
            dialog.setTitle("Please input your location");
            dialog.setUpdateListener(this);
            dialog.setTag(TAG_LOCATION);
            dialog.setDefault(curUser.getAddress());
            dialog.show();
        } else if (view.getId() == R.id.img_profile_main) {
            TAG = TAG_MAIN;
            selectImage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_preview, menu);

        Drawable minus = getResources().getDrawable(R.drawable.ic_decrease);
        minus.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        menu.findItem(R.id.action_remove).setIcon(minus);

        Drawable plus = getResources().getDrawable(R.drawable.ic_increase);
        plus.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        menu.findItem(R.id.action_add).setIcon(plus);
        /*
        menu.findItem(R.id.action_remove).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_minus)
                        .colorRes(R.color.colorWhite)
                        .actionBarSize());

        menu.findItem(R.id.action_add).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_plus)
                        .colorRes(R.color.colorWhite)
                        .actionBarSize());
        */
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_remove:
                DialogUtil.showConfirmDialog(this, "Remove this cover image?", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        int pos = pagerCover.getCurrentItem();
                        adapterCover.remove(pos);
                        subImageChanged = true;
                    }
                });
                return true;
            case R.id.action_add:
                TAG = TAG_SUB;
                selectImage();
                return true;
            case R.id.action_done:
                DialogUtil.showConfirmDialog(this, "Complete Profile Edit?", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        completeProfile();
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdate(String field, int tag) {
        if (tag == TAG_NAME) {
            txtName.setText(field + " {fa-pencil}");
            curUser.setName(field);
        } else if (tag == TAG_STATUS) {
            txtStatus.setText(field + " {fa-pencil}");
            curUser.setDescription(field);
        } else if (tag == TAG_LOCATION) {
            txtLocation.setText(field + " {fa-pencil}");
            curUser.setAddress(field);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                Uri mImageUri = Uri.fromFile(mFileTemp);
                if (TAG == TAG_MAIN) {
                    mainImageChanged = true;
                    mainImage = mImageUri;
                    Glide.with(PreviewActivity.this).load(mImageUri).asBitmap().centerCrop().placeholder(R.drawable.profile_empty).into(imgMain);
                } else if (TAG == TAG_SUB) {
                    adapterCover.add(mImageUri);
                    subImageChanged = true;
                }
            }
        } else if (requestCode == Constants.REQUEST_GALLERY) {
            if (resultCode == RESULT_OK) {
                try {
                    createTempFile();

                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    Utils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    Uri uri = Uri.fromFile(mFileTemp);
                    if (TAG == TAG_MAIN) {
                        mainImageChanged = true;
                        mainImage = uri;
                        Glide.with(PreviewActivity.this).load(uri).asBitmap().centerCrop().placeholder(R.drawable.profile_empty).into(imgMain);
                    } else if (TAG == TAG_SUB) {
                        adapterCover.add(uri);
                        subImageChanged = true;
                    }
                } catch (Exception e) {
                    return;
                }
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.REQUEST_CAMERA && grantResults[0] >= 0 && grantResults[1] >= 0) {
            takePhoto();
        } else if (requestCode == Constants.REQUEST_GALLERY && grantResults[0] >= 0) {
            choosePhoto();
        }
    }
}
