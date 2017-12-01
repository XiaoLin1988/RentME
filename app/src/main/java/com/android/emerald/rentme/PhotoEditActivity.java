package com.android.emerald.rentme;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.emerald.rentme.Adapter.EditDoodleAdapter;
import com.android.emerald.rentme.Adapter.EditEffectAdapter;
import com.android.emerald.rentme.Adapter.EditFrameAdapter;
import com.android.emerald.rentme.Adapter.EditScaleAdapter;
import com.android.emerald.rentme.Fragments.EditFilterFragment;
import com.android.emerald.rentme.Fragments.EditOverlayFragment;
import com.android.emerald.rentme.Fragments.EditScaleFragment;
import com.android.emerald.rentme.Models.Effect;
import com.android.emerald.rentme.Utils.BitmapUtil;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.InternalStorageContentProvider;
import com.android.emerald.rentme.Utils.Utils;
import com.android.emerald.rentme.Views.PhotoWorkView;
import com.android.volley.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
/**
 * Created by emerald on 6/14/2017.
 */
public class PhotoEditActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Response.Listener<String> {

    private PhotoWorkView imgPanel;

    private ImageView imgBack;
    private ImageView imgSave;
    private ImageView imgCancel;

    private ImageView imgScaleEdit;
    private ImageView imgFilterEdit;
    private ImageView imgOverlayEdit;

    private File mFileTemp;
    private String mImagePath;
    private Uri mImageUri;

    private Bitmap workingBitmap;
    private Bitmap currentBitmap;

    private int action;
    private int angle;
    private int brightness, contrast, saturation;
    private boolean flipH, flipV;

    private EditScaleFragment fragmentEditScale;
    private EditFilterFragment fragmentEditFilter;
    private EditOverlayFragment fragmentEditOverlay;

    public EditScaleAdapter.OnEffectClickListener effectClickLister() {
        return new EditScaleAdapter.OnEffectClickListener(){
            @Override
            public void onEffectClick(Effect effect) {
                action = effect.getAction();
                switch (effect.getAction()) {
                    case Constants.ACTION_CROP:
                        break;
                    case Constants.ACTION_ROTATE_LEFT:
                        angle -= 90;
                        if (angle < 0) {
                            angle = 360 + angle;
                        }
                        applyEffect(Constants.ACTION_ROTATE_LEFT, currentBitmap);
                        break;
                    case Constants.ACTION_ROTATE_RIGHT:
                        angle += 90;
                        if (angle > 360) {
                            angle = angle - 360;
                        }
                        applyEffect (Constants.ACTION_ROTATE_RIGHT, currentBitmap);
                        break;
                    case Constants.ACTION_FLIP_HORIZONTAL:
                        flipH = !flipH;
                        applyEffect(Constants.ACTION_FLIP_HORIZONTAL, currentBitmap);
                        break;
                    case Constants.ACTION_FLIP_VERTICAL:
                        flipV = !flipV;
                        applyEffect(Constants.ACTION_FLIP_VERTICAL, currentBitmap);
                        break;
                }
            }
        };
    }

    public EditEffectAdapter.OnFilterClickListener filterClickListener() {
        return new EditEffectAdapter.OnFilterClickListener() {
            @Override
            public void onFilterClick(Effect effect) {
                applyEffect(effect.getAction(), currentBitmap);
            }
        };
    }

    public EditFrameAdapter.OnFrameClickListener frameClickListener() {
        return new EditFrameAdapter.OnFrameClickListener() {
            @Override
            public void onFrameClick(Effect item) {
                setPanelBitmap(addFrame(item.getThumbId()));
            }
        };
    }

    public EditDoodleAdapter.OnItemClickListener itemClickListener() {
        return new EditDoodleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Effect item) {
                action = Constants.ACTION_DOODLE;
                Bitmap bm = BitmapFactory.decodeResource(getResources(), item.getThumbId());
                imgPanel.setupDoodle(bm);
            }
        };
    }

    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_photoedit);

        createTempFile();
        getParams(savedBundle);
        initVariables();
    }

    private void getParams(Bundle bundle) {
        if (bundle == null || !bundle.getBoolean("restoreState")) {
            int action = getIntent().getIntExtra("ACTION", 0);
            switch (action) {
                case Constants.REQUEST_CAMERA:
                    getIntent().removeExtra("ACTION");
                    takePic();
                    return;
                case Constants.REQUEST_GALLERY:
                    getIntent().removeExtra("ACTION");
                    pickImage();
                    return;
            }
        }
    }

    private void initVariables() {
        mImagePath = mFileTemp.getPath();
        mImageUri = Uri.fromFile(new File(mImagePath));

        Bitmap bitmap = BitmapUtil.getBitmapFromUri(this, mImageUri);
        imgPanel = (PhotoWorkView)findViewById(R.id.img_photoedit_panel);
        imgPanel.setImageBitmap(bitmap);

        imgBack = (ImageView)findViewById(R.id.img_photoedit_back);
        imgBack.setOnClickListener(this);

        imgSave = (ImageView)findViewById(R.id.btn_save);
        imgSave.setOnClickListener(this);

        imgCancel = (ImageView)findViewById(R.id.btn_cancel);
        imgCancel.setOnClickListener(this);

        imgScaleEdit = (ImageView)findViewById(R.id.img_photoedit_scale);
        imgScaleEdit.setOnClickListener(this);

        imgFilterEdit = (ImageView)findViewById(R.id.img_photoedit_filter);
        imgFilterEdit.setOnClickListener(this);

        imgOverlayEdit = (ImageView)findViewById(R.id.img_photoedit_overlay);
        imgOverlayEdit.setOnClickListener(this);

        fragmentEditScale = new EditScaleFragment();
        fragmentEditFilter = new EditFilterFragment();
        fragmentEditOverlay = new EditOverlayFragment();

        angle = 0;
        flipH = false; flipV = false;
    }

    private Bitmap addFrame(int res) {
        Bitmap frameBitmap = BitmapFactory.decodeResource(getResources(),res);
        Bitmap mergedBitmap = Bitmap.createBitmap(currentBitmap.getWidth(),currentBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mergedBitmap);
        Drawable d1 = new BitmapDrawable(currentBitmap);
        Drawable d2 = new BitmapDrawable(frameBitmap);
        d1.setBounds(0, 0, currentBitmap.getWidth(), currentBitmap.getHeight());
        d2.setBounds(0, 0, currentBitmap.getWidth(), currentBitmap.getHeight());
        d1.draw(canvas);
        d2.draw(canvas);
        return mergedBitmap;
    }

    private void applyEffect(int action, Bitmap b) {
        new ApplyEffects(b).execute(action);
    }

    private void setPanelBitmap(Bitmap bmp) {
        imgPanel.setImageBitmap(bmp);
    }

    private void createTempFile() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), Constants.TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), Constants.TEMP_PHOTO_FILE_NAME);
        }
    }

    private void takePic() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            takePictureIntent.putExtra("return-data", true);
            startActivityForResult(takePictureIntent, Constants.REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Can't take picture", Toast.LENGTH_LONG).show();
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        try {
            startActivityForResult(intent, Constants.REQUEST_CODE_PICK_GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No image source available", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        createTempFile();
        if (requestCode == Constants.REQUEST_CODE_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                mImagePath = mFileTemp.getPath();
                mImageUri = Uri.fromFile(new File(mImagePath));
                currentBitmap = BitmapUtil.getBitmapFromUri(PhotoEditActivity.this, mImageUri);
                setPanelBitmap(currentBitmap);
            } else if (resultCode == RESULT_CANCELED) {
                return;
            } else {
                Toast.makeText(PhotoEditActivity.this, "Error while opening the image file. Please try again.", Toast.LENGTH_LONG).show();
                return;
            }
        } else if (requestCode == Constants.REQUEST_CODE_PICK_GALLERY) {
            if (resultCode == RESULT_CANCELED) {
                return;
            } else if (resultCode == RESULT_OK) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(result.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    Utils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    mImagePath = mFileTemp.getPath();
                    mImageUri = Uri.fromFile(new File(mImagePath));
                    currentBitmap = BitmapUtil.getBitmapFromUri(PhotoEditActivity.this, mImageUri);
                    setPanelBitmap(currentBitmap);
                } catch (Exception e) {
                    Toast.makeText(PhotoEditActivity.this, "Error while opening the image file. Please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                Toast.makeText(PhotoEditActivity.this, "Error while opening the image file. Please try again.", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.img_photoedit_back:
                finish();
                break;
            case R.id.btn_save:
                Bitmap resultBitmap = ((BitmapDrawable)imgPanel.getDrawable()).getBitmap();
                /*
                Intent intent = new Intent();
                intent.putExtra("data", resultBitmap);
                setResult(RESULT_OK, intent);
                finish();
                */
                BitmapUtil.uploadImage(PhotoEditActivity.this, resultBitmap, this);
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.img_photoedit_scale:
                if (findViewById(R.id.img_photoedit_scale_selector).getVisibility() == View.INVISIBLE) {
                    ft.replace(R.id.container_photoedit, fragmentEditScale);
                    findViewById(R.id.img_photoedit_scale_selector).setVisibility(View.VISIBLE);
                    findViewById(R.id.img_photoedit_filter_selector).setVisibility(View.INVISIBLE);
                    findViewById(R.id.img_photoedit_overlay_selector).setVisibility(View.INVISIBLE);
                } else {
                    ft.remove(fragmentEditScale);
                    findViewById(R.id.img_photoedit_scale_selector).setVisibility(View.INVISIBLE);
                }
                ft.commit();
                break;
            case R.id.img_photoedit_filter:
                if (findViewById(R.id.img_photoedit_filter_selector).getVisibility() == View.INVISIBLE) {
                    ft.replace(R.id.container_photoedit, fragmentEditFilter);
                    findViewById(R.id.img_photoedit_scale_selector).setVisibility(View.INVISIBLE);
                    findViewById(R.id.img_photoedit_filter_selector).setVisibility(View.VISIBLE);
                    findViewById(R.id.img_photoedit_overlay_selector).setVisibility(View.INVISIBLE);
                } else {
                    ft.remove(fragmentEditFilter);
                    findViewById(R.id.img_photoedit_filter_selector).setVisibility(View.INVISIBLE);
                }
                ft.commit();
                break;
            case R.id.img_photoedit_overlay:
                if (findViewById(R.id.img_photoedit_overlay_selector).getVisibility() == View.INVISIBLE) {
                    ft.replace(R.id.container_photoedit, fragmentEditOverlay);
                    findViewById(R.id.img_photoedit_scale_selector).setVisibility(View.INVISIBLE);
                    findViewById(R.id.img_photoedit_filter_selector).setVisibility(View.INVISIBLE);
                    findViewById(R.id.img_photoedit_overlay_selector).setVisibility(View.VISIBLE);
                } else {
                    ft.remove(fragmentEditOverlay);
                    findViewById(R.id.img_photoedit_overlay_selector).setVisibility(View.INVISIBLE);
                }
                ft.commit();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId())
        {
            case R.id.seek1:
                saturation = seekBar.getProgress();
                applyEffect(Constants.ACTION_SATURATION, currentBitmap);
                break;
            case R.id.seek2:
                brightness = seekBar.getProgress();
                applyEffect(Constants.ACTION_BRIGHTNESS, currentBitmap);
                break;
            case R.id.seek3:
                contrast = seekBar.getProgress();
                applyEffect(Constants.ACTION_CONTRAST, currentBitmap);
                break;
        }
    }

    @Override
    public void onResponse(String response) {
        Intent intent = new Intent();
        intent.putExtra("data", response);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class ApplyEffects extends AsyncTask<Integer, Void, Bitmap> {
        Bitmap bitmap;
        
        public ApplyEffects (Bitmap bmp) {
            bitmap = bmp.copy(bmp.getConfig(), true);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            int act = params[0];
            action = act;
            switch (act) {
                case Constants.ACTION_FLIP_HORIZONTAL:
                    bitmap = BitmapUtil.flip(bitmap, flipH, flipV);
                    break;
                case Constants.ACTION_FLIP_VERTICAL:
                    bitmap = BitmapUtil.flip(bitmap, flipH, flipV);
                    break;
                case Constants.ACTION_ROTATE_LEFT:
                    bitmap = BitmapUtil.rotate(bitmap, angle);
                    break;
                case Constants.ACTION_ROTATE_RIGHT:
                    bitmap = BitmapUtil.rotate(bitmap, angle);
                    break;
                case Constants.ACTION_ORIGINAL:
                    break;
                case Constants.ACTION_BOOST:
                    bitmap = BitmapUtil.boost(bitmap, 2, 50);
                    break;
                case Constants.ACTION_BRIGHTNESS:
                    bitmap = BitmapUtil.brightness(bitmap, brightness);
                    break;
                case Constants.ACTION_COLORDEPTH:
                    bitmap = BitmapUtil.cdepth(bitmap, 100);
                    break;
                case Constants.ACTION_COLORFILTER:
                    bitmap = BitmapUtil.cfilter(bitmap, 100, 100, 100);
                    break;
                case Constants.ACTION_CONTRAST:
                    bitmap = BitmapUtil.contrast(bitmap, contrast);
                    break;
                case Constants.ACTION_EMBOSS:
                    bitmap = BitmapUtil.emboss(bitmap);
                    break;
                case Constants.ACTION_FLIP:
                    break;
                case Constants.ACTION_GAMMA:
                    bitmap = BitmapUtil.gamma(bitmap, 100, 100, 100);
                    break;
                case Constants.ACTION_GAUSSIAN:
                    bitmap = BitmapUtil.gaussian(bitmap);
                    break;
                case Constants.ACTION_GRAYSCALE:
                    bitmap = BitmapUtil.grayscale(bitmap);
                    break;
                case Constants.ACTION_HUE:
                    bitmap = BitmapUtil.hue(bitmap, 100);
                    break;
                case Constants.ACTION_INVERT:
                    bitmap = BitmapUtil.invert(bitmap);
                    break;
                case Constants.ACTION_NOISE:
                    bitmap = BitmapUtil.noise(bitmap);
                    break;
                case Constants.ACTION_SATURATION:
                    bitmap = BitmapUtil.saturation(bitmap, saturation);
                    break;
                case Constants.ACTION_SEPIA:
                    bitmap = BitmapUtil.sepia(bitmap);
                    break;
                case Constants.ACTION_SHARPEN:
                    bitmap = BitmapUtil.sharpen(bitmap);
                    break;
                case Constants.ACTION_SKETCH:
                    bitmap = BitmapUtil.sketch(bitmap);
                    break;
                case Constants.ACTION_TINT:
                    bitmap = BitmapUtil.tint(bitmap, Color.parseColor("#FFFF0000"));
                    break;
                case Constants.ACTION_VIGNETTE:
                    bitmap = BitmapUtil.vignette(bitmap);
                    break;
            }
            return bitmap;
        }
        
        protected void onPreExecute() {
            
        }
        
        protected void onPostExecute(Bitmap bmp) {
            workingBitmap = bmp;
            setPanelBitmap(workingBitmap);
        }
    }
}
