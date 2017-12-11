package com.android.jianchen.rentme;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.jianchen.rentme.Adapter.ImageUploadRecyclerAdapter;
import com.android.jianchen.rentme.Interface.OnImageAddListener;
import com.android.jianchen.rentme.Utils.Constants;
import com.android.jianchen.rentme.Utils.InternalStorageContentProvider;
import com.android.jianchen.rentme.Utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by emerald on 12/6/2017.
 */
public class PhotoActivity extends AppCompatActivity implements OnImageAddListener {
    @Bind(R.id.recycler_photos)
    RecyclerView recyclerPhotos;
    private ImageUploadRecyclerAdapter adapterPhotos;

    private File mFileTemp;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_photo);

        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        ArrayList<Uri> uris = new ArrayList<Uri>();
        uris.add(Uri.EMPTY);
        adapterPhotos = new ImageUploadRecyclerAdapter(PhotoActivity.this, uris);
        adapterPhotos.setAddListener(this);
        recyclerPhotos.setAdapter(adapterPhotos);
        recyclerPhotos.setLayoutManager(new GridLayoutManager(PhotoActivity.this, Utils.getGridSpanCount(this)));
    }

    private void selectImage() {
        String title = getResources().getString(R.string.choose_picture);
        final CharSequence[] options = {getResources().getString(R.string.choose_camera), getResources().getString(R.string.choose_gallery)};

        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoActivity.this);
        builder.setTitle(title);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(getResources().getString(R.string.choose_camera))) {
                    if (!Utils.checkPermission(PhotoActivity.this, "android.permission.CAMERA") || !Utils.checkPermission(PhotoActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                        ActivityCompat.requestPermissions(PhotoActivity.this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                    } else {
                        takePhoto();
                    }
                } else if (options[which].equals(getResources().getString(R.string.choose_gallery))) {
                    if (!Utils.checkPermission(PhotoActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                        ActivityCompat.requestPermissions(PhotoActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                    } else {
                        choosePhoto();
                    }
                }
            }
        });
        builder.show();
    }

    private void createTempFile() {
        String state = Environment.getExternalStorageState();
        String tmp = "IMG_" + Long.toString(System.currentTimeMillis()) + ".png";
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), tmp);
        } else {
            mFileTemp = new File(getFilesDir(), tmp);
        }
    }

    private void takePhoto() {
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

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        try {
            startActivityForResult(intent, Constants.REQUEST_GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No image source available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onImageAdd() {
        selectImage();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if (requestCode == Constants.REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                Uri mImageUri = Uri.fromFile(mFileTemp);
                adapterPhotos.add(mImageUri);
            }
        } else if (requestCode == Constants.REQUEST_GALLERY) {
            if (resultCode == RESULT_OK) {
                try {
                    createTempFile();

                    InputStream inputStream = getContentResolver().openInputStream(result.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    Utils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    Uri uri = Uri.fromFile(mFileTemp);
                    adapterPhotos.add(uri);
                } catch (Exception e) {
                    return;
                }
            } else if (resultCode == RESULT_CANCELED) {
                return;
            } else {
                return;
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length == 2 && grantResults[0] >= 0 && grantResults[1] >= 0) {
            takePhoto();
        } else if (grantResults.length == 1 && grantResults[0] >= 0) {
            choosePhoto();
        }
    }
}
