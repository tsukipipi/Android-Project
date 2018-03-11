package com.example.win.edgeanalysis;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by win on 2018/1/6.
 */


/*
* GX = [-1 0 1    GY = [-1 -2 -1
*       -2 0 2           0  0  0
*       -1 0 1]          1  2  1]
*/
public class Sobel {

    //要进行sobel处理的原图像
    private Bitmap originalBitmap;
    //保存图像的灰度图
    private Bitmap grayBitmap;
    //保存灰度图sobel处理后的最大值
    private double max;
    //保存灰度图每个像素点的数值
    private int[] grayBitmapPixels;
    //保存灰度图每个像素点计算后各对应点的数值
    private double[] calcGrayBitmapPixels;
    //存放处理后的图象各像素点的数组
    int[] resultBitmap;

    //获取原图像的灰度图
    private void getGrayBitmap() {
        //将原图灰度化
        this.grayBitmap = BitmapUtil.toGrayBitmap(this.originalBitmap);
    }

    //对灰度图进行sobel算子计算
    private void sobel(){
        //获取灰度图的宽高
        int width = grayBitmap.getWidth();
        int height= grayBitmap.getHeight();

        //存放灰度图每个像素点的数值
        grayBitmapPixels = new int[width * height];
        //保存灰度图每个像素点计算后各对应点的数值
        calcGrayBitmapPixels = new double[width * height];

        //获取灰度图各像素点的数值，并赋给grayBitmapPixels数组
        grayBitmap.getPixels(grayBitmapPixels, 0, width, 0, 0, width, height);

        //保存数值最大的数
        max = -999;
        //进行计算
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //计算横向数值
                double gx = GX(i, j, grayBitmap);
                //计算纵向数值
                double gy = GY(i, j, grayBitmap);
                //进行开方处理，calcGrayBitmapPixels保存计算后的值
                calcGrayBitmapPixels[j * width + i] = Math.sqrt(gx * gx + gy * gy);
                //保存最大值
                if (max < calcGrayBitmapPixels[j * width + i]) {
                    max = calcGrayBitmapPixels[j * width + i];
                }
            }//end for
        }//end for
    }

    //x方向sobel处理
    private static double GX(int x, int y, Bitmap bitmap) {
        /**
         * GX = [-1 0 1
         *       -2 0 2
         *       -1 0 1]
         */
        double res ;
        res = (-1) * getPixel(x - 1, y - 1, bitmap)
                + 1 * getPixel(x + 1, y - 1, bitmap)
                + (-2) * getPixel(x - 1, y, bitmap)
                + 2 * getPixel(x + 1, y, bitmap)
                + (-1) * getPixel(x - 1, y + 1, bitmap)
                + 1 * getPixel(x + 1, y + 1, bitmap);
        return res;
    }

    //y方向sobel处理
    private static double GY(int x, int y, Bitmap bitmap) {
        /**
         * GY = [-1 -2 -1
         *       0  0  0
         *       1  2  1]
         */
        double res ;
        res = (-1) * getPixel(x - 1, y - 1, bitmap)
                + (-2) * getPixel(x, y - 1, bitmap)
                + (-1) * getPixel(x + 1, y - 1, bitmap)
                + 1 * getPixel(x - 1, y + 1, bitmap)
                + 2 * getPixel(x, y + 1, bitmap)
                + 1 * getPixel(x + 1, y + 1, bitmap);
        return res;
    }

    //获取第x行第y列的像素值
    private static double getPixel(int x, int y, Bitmap bitmap) {
        //判断是否超出范围
        if (x < 0 || x >= bitmap.getWidth() || y < 0 || y >= bitmap.getHeight()) {
            return 0;
        }
        return bitmap.getPixel(x, y);
    }

    //根据阈值将图像二值化
    private void getBinaryBitmap(){
        //获取灰度图的宽高
        int width = grayBitmap.getWidth();
        int height= grayBitmap.getHeight();
        //存放处理后的图象各像素点的数组
        this.resultBitmap = new int[width * height];

        //筛选计算
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (calcGrayBitmapPixels[j * width + i] > max * 0.1) {
                    resultBitmap[j * width + i] = Color.BLACK;
                } else {
                    //否则该点为白色
                    resultBitmap[j * width + i] = Color.WHITE;
                }
            }
        }
    }

    //根据设定的阙值获取处理后的图片
    public Bitmap sobelDetect(Bitmap originalBitmap){

        //获取原图
        this.originalBitmap = originalBitmap;
        //先获得灰度图
        getGrayBitmap();
        //对灰度图进行sobel算子计算
        sobel();
        //根据阈值将图像二值化
        getBinaryBitmap();
        //将筛选出来的结果生成bitmap
        Bitmap result =  Bitmap.createBitmap(resultBitmap, grayBitmap.getWidth(), grayBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        return result;

    }

}
