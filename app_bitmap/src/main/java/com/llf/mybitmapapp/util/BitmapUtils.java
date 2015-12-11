package com.llf.mybitmapapp.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by burke.li on 10/13/2015.
 */
public class BitmapUtils {

    public static Bitmap compressBitmap(String filePath, int expectWidth, int expectHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int rateWidth = (int) Math.ceil(options.outWidth / (float) expectWidth);
        int rateHeight = (int) Math.ceil(options.outHeight / (float) expectHeight);
        if (rateWidth > 1 && rateHeight > 1) {
            options.inSampleSize = rateWidth > rateHeight ? rateWidth : rateHeight;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        int degree = readPictureDegree(filePath);
        Bitmap result_bitmap = rotaingImageView(degree, bitmap);
        return result_bitmap;
    }

    public static Bitmap compressBitmap(Context context, Uri uri, int expectWidth, int expectHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int rateWidth = (int) Math.ceil(options.outWidth / (float) expectWidth);
        int rateHeight = (int) Math.ceil(options.outHeight / (float) expectHeight);
        if (rateWidth > 1 && rateHeight > 1) {
            options.inSampleSize = rateWidth > rateHeight ? rateWidth : rateHeight;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String path = getPathFromUri(context, uri);
        int degree = readPictureDegree(path);
        Bitmap result_bitmap = rotaingImageView(degree, bitmap);
        return result_bitmap;
    }

    public static Bitmap loadImage(Context context, Uri uri, int expectWidth, int expectHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int rateWidth = (int) Math.ceil(options.outWidth / (float) expectWidth);
        int rateHeight = (int) Math.ceil(options.outHeight / (float) expectHeight);
        if (rateWidth > 1 && rateHeight > 1) {
            options.inSampleSize = rateWidth > rateHeight ? rateWidth : rateHeight;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = Bitmap.createBitmap(expectWidth, expectHeight, Bitmap.Config.ARGB_4444);
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String path = getPathFromUri(context, uri);
        int degree = readPictureDegree(path);
        Bitmap result_bitmap = rotaingImageView(degree, bitmap);
        return result_bitmap;
    }

    public static String getPathFromUri(Context context, Uri uri){
        String[] column = { MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, column, null, null, null);
        String name = null;
        if(cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
        }
        cursor.close();
        return name;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    public static void updateGallery(Context context, String filename) {
        MediaScannerConnection.scanFile(context, new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }
}
