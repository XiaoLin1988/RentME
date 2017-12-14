package com.android.jianchen.rentme.activity.root;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.helper.Constants;
import com.android.jianchen.rentme.helper.utils.InternalStorageContentProvider;
import com.android.jianchen.rentme.helper.utils.Utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.togoto.imagezoomcrop.cropoverlay.CropOverlayView;
import io.togoto.imagezoomcrop.cropoverlay.edge.Edge;
import io.togoto.imagezoomcrop.photoview.IGetImageBounds;
import io.togoto.imagezoomcrop.photoview.PhotoView;
import io.togoto.imagezoomcrop.photoview.RotationSeekBar;

/**
 * @author GT
 */
public class ImageCropActivity extends Activity {

    public static final String TAG = "ImageCropActivity";
    private static final int ANCHOR_CENTER_DELTA = 10;

    PhotoView mImageView;
    CropOverlayView mCropOverlayView;
    Button btnRetakePic;
    Button btnFromGallery;
    Button btnDone;
    Button mBtnReset;
    View mMoveResizeText;
    RotationSeekBar mRotationBar;
    Button mBtnUndoRotation;

    private ContentResolver mContentResolver;

    private final Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG;

    //Temp file to save cropped image
    private String mImagePath;
    private Uri mSaveUri = null;
    private Uri mImageUri = null;


    //File for capturing camera images
    private File mFileTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        mContentResolver = getContentResolver();
        mImageView = (PhotoView) findViewById(R.id.iv_photo);
        mCropOverlayView = (CropOverlayView) findViewById(R.id.crop_overlay);
        btnRetakePic = (Button) findViewById(R.id.btnRetakePic);
        btnFromGallery = (Button) findViewById(R.id.btnFromGallery);
        btnDone = (Button) findViewById(R.id.btn_done);
        mBtnReset = (Button) findViewById(R.id.btn_reset);
        mMoveResizeText = findViewById(R.id.tv_move_resize_txt);
        mRotationBar = (RotationSeekBar) findViewById(R.id.bar_rotation);
        mBtnUndoRotation = (Button) findViewById(R.id.btn_undo);

        btnRetakePic.setOnClickListener(btnRetakeListener);
        btnFromGallery.setOnClickListener(btnFromGalleryListener);
        btnDone.setOnClickListener(btnDoneListerner);
        mBtnReset.setOnClickListener(btnResetListerner);
        mBtnUndoRotation.setOnClickListener(btnUndoRotationListener);

        mImageView.setImageBoundsListener(new IGetImageBounds() {
            @Override
            public Rect getImageBounds() {
                return mCropOverlayView.getImageBounds();
            }
        });

        // initialize rotation seek bar
        mRotationBar.setOnSeekBarChangeListener(new RotationSeekBar.OnRotationSeekBarChangeListener(mRotationBar) {

            private float mLastAngle;

            @Override
            public void onRotationProgressChanged(@NonNull RotationSeekBar seekBar, float angle, float delta, boolean fromUser) {
                mLastAngle = angle;
                if (fromUser) {
                    mImageView.setRotationBy(delta, false);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (Math.abs(mLastAngle) < ANCHOR_CENTER_DELTA) {
                    mRotationBar.reset();
                    mImageView.setRotationBy(0, true);
                }
            }
        });


        createTempFile();
        if (savedInstanceState == null || !savedInstanceState.getBoolean("restoreState")) {
            //String action = getIntent().getStringExtra("ACTION");
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
        mImagePath = mFileTemp.getPath();
        mSaveUri = Uri.fromFile(new File(mImagePath));
        mImageUri = Uri.fromFile(new File(mImagePath));
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
        Bitmap bitmap = getBitmap(mImageUri);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

        float minScale = mImageView.setMinimumScaleToFit(drawable);
        mImageView.setMaximumScale(minScale * 3);
        mImageView.setMediumScale(minScale * 2);
        mImageView.setScale(minScale);
        mImageView.setImageDrawable(drawable);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mMoveResizeText.getLayoutParams();
        lp.setMargins(0, Math.round(Edge.BOTTOM.getCoordinate()) + 20, 0, 0);
        mMoveResizeText.setLayoutParams(lp);
    }

    private View.OnClickListener btnDoneListerner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveUploadCroppedImage();
        }
    };

    private void saveUploadCroppedImage() {
        boolean saved = saveOutput();
        if (saved) {
            Intent intent = new Intent();
            intent.putExtra(Constants.IMAGE_PATH, mImagePath);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Unable to save Image into your device.", Toast.LENGTH_LONG).show();
        }
    }

    private View.OnClickListener btnResetListerner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRotationBar.reset();
            mImageView.reset();
        }
    };

    private View.OnClickListener btnRetakeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == mFileTemp) {
                createTempFile();
            }
            takePic();
        }
    };

    private View.OnClickListener btnUndoRotationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mImageView.setRotationBy(0, true);
            mRotationBar.reset();
        }
    };

    private View.OnClickListener btnFromGalleryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == mFileTemp) {
                createTempFile();
            }
            pickImage();
        }
    };


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
            Log.e(TAG, "Can't take picture", e);
            Toast.makeText(this, "Can't take picture", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("restoreState", true);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        try {
            startActivityForResult(intent, Constants.REQUEST_CODE_PICK_GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No image source available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        createTempFile();
        if (requestCode == Constants.REQUEST_CODE_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                mImagePath = mFileTemp.getPath();
                mSaveUri = Uri.fromFile(new File(mImagePath));
                mImageUri = Uri.fromFile(new File(mImagePath));
                init();
            } else if (resultCode == RESULT_CANCELED) {
                userCancelled();
                return;
            } else {
                errored("Error while opening the image file. Please try again.");
                return;
            }

        } else if (requestCode == Constants.REQUEST_CODE_PICK_GALLERY) {
            if (resultCode == RESULT_CANCELED) {
                userCancelled();
                return;
            } else if (resultCode == RESULT_OK) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(result.getData()); // Got the bitmap .. Copy it to the temp file for cropping
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    Utils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    mImagePath = mFileTemp.getPath();
                    mSaveUri = Uri.fromFile(new File(mImagePath));
                    mImageUri = Uri.fromFile(new File(mImagePath));
                    init();
                } catch (Exception e) {
                    errored("Error while opening the image file. Please try again.");
                    return;
                }
            } else {
                errored("Error while opening the image file. Please try again.");
                return;
            }

        }
    }


    private Bitmap getBitmap(Uri uri) {
        InputStream in = null;
        Bitmap returnedBitmap = null;
        try {
            in = mContentResolver.openInputStream(uri);
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();
            int scale = 1;
            if (o.outHeight > Constants.IMAGE_MAX_SIZE || o.outWidth > Constants.IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(Constants.IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = mContentResolver.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, o2);
            in.close();

            //First check
            ExifInterface ei = new ExifInterface(uri.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    returnedBitmap = rotateImage(bitmap, 90);
                    bitmap.recycle();
                    bitmap = null;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    returnedBitmap = rotateImage(bitmap, 180);
                    bitmap.recycle();
                    bitmap = null;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    returnedBitmap = rotateImage(bitmap, 270);
                    bitmap.recycle();
                    bitmap = null;
                    break;
                default:
                    returnedBitmap = bitmap;
            }
            return returnedBitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private boolean saveOutput() {
        Bitmap croppedImage = mImageView.getCroppedImage();
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = mContentResolver.openOutputStream(mSaveUri);
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 90, outputStream);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            } finally {
                closeSilently(outputStream);
            }
        } else {
            Log.e(TAG, "not defined image url");
            return false;
        }
        croppedImage.recycle();
        return true;
    }


    public void closeSilently(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (Throwable t) {
            // do nothing
        }
    }


    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    public void userCancelled() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void errored(String msg) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ERROR, true);
        if (msg != null) {
            intent.putExtra(Constants.ERROR_MSG, msg);
        }
        finish();
    }


}
