package com.tugasakhir.veinred.util;

import android.graphics.Bitmap;

public class BitmapFrameBuffer {
    public final Bitmap msxBitmap;
    public final Bitmap dcBitmap;
    public BitmapFrameBuffer(Bitmap msxBitmap, Bitmap dcBitmap){
        this.msxBitmap = msxBitmap;
        this.dcBitmap = dcBitmap;
    }
}