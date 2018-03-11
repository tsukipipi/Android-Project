package com.example.win.edgeanalysis;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //调用相册
    private static final int IMAGE = 1;
    //从相册获取的图片
    Bitmap image;
    //ImageView展示图片
    ImageView imageShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //实例化对象
        imageShow = (ImageView) findViewById(R.id.iv_show);

        //悬浮按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用相册
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //选择图片格式
                intent.setType("image/*");
                intent.putExtra("return-data",true);
                startActivityForResult(intent,IMAGE);
            }
        });
    }

    //获得图片后调用
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                image = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                imageShow.setImageBitmap(image);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("tag", e.getMessage());
                Toast.makeText(this,"程序崩溃", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.item_edge_detection) {
            Sobel SobelDetect = new Sobel();
            //边缘检测后的图像
            Bitmap imageSobel;
            //检测图像边缘并获取检测边缘之后的图像
            imageSobel = SobelDetect.sobelDetect(image);
            //设置检测边缘之后的图像
            imageShow.setImageBitmap(imageSobel);
            return true;
        }
        if (id == R.id.item_line_detection) {
            Hough lineDetect = new Hough();
            Bitmap imageLine;
            //检测图像边缘并获取检测边缘之后的图像
            imageLine = lineDetect.lineDetect(image);
            //设置检测边缘之后的图像
            imageShow.setImageBitmap(imageLine);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
