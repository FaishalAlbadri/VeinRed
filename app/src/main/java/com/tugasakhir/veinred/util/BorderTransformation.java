package com.tugasakhir.veinred.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.security.MessageDigest;

import jp.wasabeef.glide.transformations.BitmapTransformation;

public class BorderTransformation extends BitmapTransformation {

    @Override
    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap sbmp;
        int radius = toTransform.getWidth();
        if (toTransform.getWidth() != radius || toTransform.getHeight() != radius) {
            float smallest = Math.min(toTransform.getWidth(), toTransform.getHeight());
            float factor = smallest / radius;
            sbmp = Bitmap.createScaledBitmap(toTransform, (int)(toTransform.getWidth() / factor), (int)(toTransform.getHeight() / factor), false);
        } else {
            sbmp = toTransform;
        }

        Bitmap output = Bitmap.createBitmap(radius, radius,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Paint stroke = new Paint();

        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);
        stroke.setAntiAlias(true);

        paint.setFilterBitmap(true);
        stroke.setFilterBitmap(true);

        paint.setDither(true);
        stroke.setDither(true);

        canvas.drawARGB(0, 0, 0, 0);
        stroke.setColor(Color.WHITE); //border color
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(14f); //border width
        canvas.drawCircle(radius / 2 + 0.7f,
                radius / 2 + 0.7f, radius / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        canvas.drawCircle(radius / 2 + 0.7f,
                radius / 2 + 0.7f, radius / 2 - stroke.getStrokeWidth()/2 +0.1f, stroke);

        return output;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
