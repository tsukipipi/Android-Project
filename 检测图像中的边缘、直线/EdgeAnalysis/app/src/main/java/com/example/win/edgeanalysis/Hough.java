package com.example.win.edgeanalysis;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by win on 2018/1/10.
 */

public class Hough {

    //要进行hough检测的原图像
    private Bitmap originalBitmap;
    //图像识别类
    private Sobel SobelDetect;
    //获得二值化的图像
    private Bitmap binaryBitmap;
    //极坐标二维数组
    private int [][]polar;
    //保存线检测的最终图像
    private int[] resultBitmap;


    public Bitmap lineDetect(Bitmap originalBitmap){
        //获取原图
        this.originalBitmap = originalBitmap;

        SobelDetect = new Sobel();
        //用sobel算子获得二值化图像
        binaryBitmap = SobelDetect.sobelDetect(originalBitmap);

        //获取二值图像的宽高
        int width = binaryBitmap.getWidth();
        int height = binaryBitmap.getHeight();

        resultBitmap = new int[width * height];
        //获取原图像各像素点的数值，并赋给resultBitmap数组
        originalBitmap.getPixels(resultBitmap, 0, width, 0, 0, width, height);

        //极坐标的ρ
        int ro = (int)Math.sqrt(width * width + height * height);
        //Log.i("喵喵喵:ro",ro + "");
        //极坐标的θ
        int theta =180;
        //极坐标二维数组
        polar = new int[ro][theta];

        //直角坐标转化为极坐标
        double polarAngle = Math.PI/(theta*2);

        //对于直角坐标系上的每一个点(x,y)，计算出其180个角度（固定θ）下对应的ρ，即确定极坐标(ρ,θ)
        for(int i = 0; i < theta; i++){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    //??==0？
                    if( Color.red(binaryBitmap.getPixel(x,y)) == 0){
                        //Log.i("喵:pixel",binaryBitmap.getPixel(x,y) + "");
                        //计算出在θ为i时点(x,y)对应的极坐标的ρ
                        int temp_ro =(int)(x * Math.cos(i * polarAngle) + y *Math.sin(i * polarAngle));
                        //对应的极坐标的二维数组加上1
                        polar[temp_ro][i]++;
                    }
                }
            }
        }//end for:计算极坐标二维数组的值

        //获取二维数组的最大值
        int max = 0;
        for(int x = 0; x < ro; x++)
            for (int y = 0; y < theta; y++)
            {
                if(polar[x][y] > max) max = polar[x][y];
                //Log.i("喵:ro-theta",x + " " + y + " " + polar[x][y]);
            }

        //Log.i("喵喵喵:max",max + "");
        //阈值
        int threshold = (int)(0.7* max);
        //Log.i("喵喵喵:threshold",threshold + "");
        //int threshold = 20;

        for(int x = 0; x < ro; x++){
            for (int y = 0; y < theta; y++){
                if(polar[x][y] > threshold){
                    //Log.i("喵:ro-theta",x + " " + y + " " + polar[x][y]);
                    for (int i = 0; i < height; i++){
                        for (int j = 0; j < width; j++){
                            //??==0？
                            if (Color.red(binaryBitmap.getPixel(j,i)) == 0){
                                //计算出在θ为y时点(j,i)对应的极坐标的ρ
                                int temp_ro =(int)(j * Math.cos(y * polarAngle) + i *Math.sin(y * polarAngle));
                                if(temp_ro == x) {
                                    //Log.i("喵:temp_ro",temp_ro + "");
                                    resultBitmap[i * width + j] = Color.RED;
                                }
                            }
                        }
                    }
                }
            }
        }

        //将筛选出来的结果生成bitmap
        Bitmap result =  Bitmap.createBitmap(resultBitmap, width, height,
                Bitmap.Config.ARGB_8888);
        return result;

    }

}
