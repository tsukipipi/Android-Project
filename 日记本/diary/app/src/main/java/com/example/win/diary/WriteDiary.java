package com.example.win.diary;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

public class WriteDiary extends AppCompatActivity implements View.OnClickListener{

    //Vector<String> MP3File ;

    Button enter;
    Button cancel;
    EditText title;
    EditText content;

    //SQLiteOpenHelper子类
    MyHelper myHelper;
    //保存要修改的日记
    MyDiary updateDiary;

    //跳转目的
    String purpose;

    //链接对象
    private myConn conn;
    //服务的binder对象
    MusicService.MyBinder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //初始化
        init();

        Intent intent = getIntent();
        purpose = intent.getStringExtra("purpose");
        if(purpose.equals("add")){
            title.setText("");
            content.setText("");
            enter.setText("确定");
        }
        else if(purpose.equals("update")){
            updateDiary = (MyDiary) intent.getSerializableExtra("diary");
            title.setText(updateDiary.getTitle());
            content.setText(updateDiary.getContent());
            enter.setText("修改");
        }

        conn = new myConn();
        //绑定服务
        Intent serviceIntent = new Intent(WriteDiary.this,MusicService.class);
        bindService(serviceIntent,conn,BIND_AUTO_CREATE);

    }

    //初始化
    void init(){
        //MP3File = new Vector<String>();

        updateDiary = new MyDiary();

        //实例化控件
        title = (EditText) findViewById(R.id.et_Title);
        content = (EditText) findViewById(R.id.et_Content);
        enter = (Button) findViewById(R.id.btn_enter);
        cancel = (Button) findViewById(R.id.btn_cancel);
        //注册监听器
        enter.setOnClickListener(this);
        cancel.setOnClickListener(this);

        myHelper = new MyHelper(WriteDiary.this);

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            //保存到数据库，并返回
            case R.id.btn_enter:
                Intent intent1;
                if(purpose.equals("add")){
                    //保存数据
                    SaveData();
                    intent1 = new Intent(WriteDiary.this,MainActivity.class);
                    startActivity(intent1);
                }
                else if(purpose.equals("update")){
                    //获取可写入的SQLiteDatabase对象
                    SQLiteDatabase db = myHelper.getWritableDatabase();
                    //创建ContentValues对象
                    ContentValues values = new ContentValues();

                    String tempTitle = title.getText().toString().trim();
                    String tempContent = content.getText().toString().trim();
                    //将信息插入到数据库中
                    values.put("title",tempTitle);
                    values.put("content",tempContent);
                    //更新日记内容
                    db.update("diary",values,"_id=?",
                            new String[]{updateDiary.getId().toString()});

                    updateDiary.setTitle(tempTitle);
                    updateDiary.setContent(tempContent);

                    /*//跳转回到展示页面
                    intent1 = new Intent(WriteDiary.this,ShowDiary.class);
                    intent1.putExtra("updateDiary",updateDiary);
                    startActivity(intent1);*/

                    intent1 = new Intent(WriteDiary.this,MainActivity.class);
                    startActivity(intent1);
                }

                break;
            //不保存，返回
            case R.id.btn_cancel:
                Intent intent2;
                if(purpose.equals("add")){
                    intent2 = new Intent(WriteDiary.this,MainActivity.class);
                    startActivity(intent2);
                }
                else if(purpose.equals("update")){
                    //跳转回到展示页面，维持日记标题及内容不变
                    intent1 = new Intent(WriteDiary.this,ShowDiary.class);
                    intent1.putExtra("diary",updateDiary);
                    startActivity(intent1);
                }
                break;
        }
    }

    //保存数据到数据库中
    void SaveData()
    {
        //获取可写入的SQLiteDatabase对象
        SQLiteDatabase db = myHelper.getWritableDatabase();
        //创建ContentValues对象
        ContentValues values = new ContentValues();

        //判断是否为空
        if(TextUtils.isEmpty(title.getText().toString().trim())){
            Toast.makeText(WriteDiary.this,"标题不可以为空！",Toast.LENGTH_SHORT).show();
        }
        else {
            //将信息插入到数据库中
            values.put("title",title.getText().toString().trim());
            //获取系统时间
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = sDateFormat.format(new java.util.Date());
            values.put("date",date);
            values.put("content",content.getText().toString().trim());
            //插入到数据库中
            db.insert("diary",null,values);
        }
        //关闭数据库
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        String pathway = "Music/mr.music.mp3";
        //获取手机内置SD卡的路径
        File SDPath = Environment.getExternalStorageDirectory();
        //Log.e("cwj", "内置SD卡路径 = " + Environment.getExternalStorageDirectory().getAbsolutePath());
        File file = new File(SDPath,pathway);
        //获取文件的绝对路径
        String path = file.getAbsolutePath();

        //播放音乐
        if (id == R.id.action_music) {
            if(file.exists() && file.length()>0)
            {
                //暂停播放
                if(item.getTitle().equals("暂停")){
                    item.setTitle("播放音乐");
                    binder.pause();
                }
                //播放
                else if(item.getTitle().equals("播放音乐")){
                    item.setTitle("暂停");
                    binder.play(path);
                }
            }
            else {
                Toast.makeText(WriteDiary.this,
                        "找不到音乐文件",Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    public void getMP3File(String fileAbsolutePath){
        File file = new File(fileAbsolutePath);
        //获取目录下的文件
        File[] subFile = file.listFiles();

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                // 判断是否为MP3结尾
                if (filename.trim().toLowerCase().endsWith(".mp3")) {
                    MP3File.add(filename);
                }
            }
        }
    }*/


    private class myConn implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            //向客户端传递用来与服务通信的IBinder
            binder = (MusicService.MyBinder) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name){
        }
    }

    @Override
    protected void onDestroy(){
        unbindService(conn);
        super.onDestroy();
    }

}
