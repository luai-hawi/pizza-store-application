package com.example.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    public static byte[] convertDrawableToByteArray(Context context, int drawableResId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableResId);
        return convertBitmapToByteArray(bitmap);
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
