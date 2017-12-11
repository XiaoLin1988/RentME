package com.android.jianchen.rentme.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.android.jianchen.rentme.Utils.BitmapUtil;

/**
 * Created by emerald on 2017/2/9.
 */
public class PhotoWorkView extends ImageView {
    private PhotoOverlayMoveView mMoveView;
    private Bitmap watermark;

    private int isDoodle = 0;

    public PhotoWorkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMoveView = new PhotoOverlayMoveView(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDoodle == 1)
            mMoveView.draw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isDoodle == 1)
                    mMoveView.getHit(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDoodle == 1)
                    mMoveView.handleMotion(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void setupDoodle(Bitmap bmp) {
        watermark = bmp;
        mMoveView.setup((getWidth() - watermark.getWidth()) / 2, (getHeight() - watermark.getHeight()) / 2, watermark);
        //mMoveView.setup((getWidth() - watermark.getWidth()) / 2, (getHeight() - watermark.getHeight()) / 2, watermark);
        isDoodle = 1;
        invalidate();
    }

    public void deleteDoodle() {
        isDoodle = 0;
        invalidate();
    }

    public Bitmap getResultBitmap() {
        Bitmap src = ((BitmapDrawable)getDrawable()).getBitmap();
        float srcWindWidth = 0;
        float srcWindHeight = 0;
        float ratio = 0;
        if (src.getHeight() > src.getWidth()) {
            srcWindHeight = getHeight();
            ratio = (float) srcWindHeight / (float) src.getHeight();
            srcWindWidth = src.getWidth() * ratio;
        } else {
            srcWindWidth = getWidth();
            ratio = (float) srcWindWidth / (float) src.getWidth();
            srcWindHeight = src.getHeight() * srcWindWidth / src.getWidth();
        }

        float srcXPos = getWidth() / 2 - srcWindWidth / 2;
        float srcYPos = getHeight() / 2 - srcWindHeight / 2;

        float xPosInBmp = mMoveView.getLeft() - srcXPos;
        float yPosInBmp = mMoveView.getTop() - srcYPos;

        watermark = BitmapUtil.resize(watermark, (int)(watermark.getWidth() / ratio), (int)(watermark.getHeight() / ratio));

        Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newb);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(watermark, xPosInBmp / ratio, yPosInBmp / ratio, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return newb;

    }
}
