package com.example.win.edgeanalysis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by win on 2018/1/6.
 */

public class BitmapUtil {

    //将图片转化成灰度图
    public static Bitmap toGrayBitmap(Bitmap originalBitmap) {

        //获取位图的宽和高
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        int []pixels = new int[width * height];
        originalBitmap.getPixels(pixels,0,width,0,0,width,height);
        int alpha = 0xFF << 24;
        for(int i = 0; i < height; i++)  {
            for(int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey  & 0x00FF0000 ) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int)((float) red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }

        Bitmap grayImage = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        grayImage.setPixels(pixels, 0, width, 0, 0, width, height);

        /*Canvas c = new Canvas(GrayImage);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);*/

        return grayImage;
    }

}
