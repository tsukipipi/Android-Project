package com.example.win.diary;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ShowDiary extends AppCompatActivity {

    TextView DiaryTitle;
    TextView DiaryDate;
    TextView DiaryContent;

    MyDiary showDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showDiary = new MyDiary();

        DiaryTitle = (TextView) findViewById(R.id.tv_diaryTitle);
        DiaryDate = (TextView) findViewById(R.id.tv_diaryDate);
        DiaryContent = (TextView) findViewById(R.id.tv_diaryContent);
        //设置滚动条可以拉动的事件
        DiaryContent.setMovementMethod(ScrollingMovementMethod.getInstance());

        Intent intent = getIntent();
        showDiary = (MyDiary) intent.getSerializableExtra("diary");
        DiaryTitle.setText(showDiary.getTitle());
        DiaryDate.setText(showDiary.getDate() + " " + intent.getStringExtra("diaryWeekday"));
        DiaryContent.setText(showDiary.getContent());
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        /*Intent intent = getIntent();
        showDiary = (MyDiary) intent.getSerializableExtra("updateDiary");
        DiaryTitle.setText(showDiary.getTitle());
        DiaryDate.setText(showDiary.getDate());
        DiaryContent.setText(showDiary.getContent());*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //修改日记
        if (id == R.id.action_update) {
            Intent intent = new Intent(ShowDiary.this,WriteDiary.class);
            //设置跳转的目的
            intent.putExtra("purpose","update");
            intent.putExtra("diary",showDiary);
            startActivity(intent);
            return true;
        }
        //删除日记
        if (id == R.id.action_delete) {
            //从数据库删除
            MyHelper myHelper = new MyHelper(this);
            //获得可以读取数据的SQLiteDataBase对象
            SQLiteDatabase db = myHelper.getWritableDatabase();
            db.delete("diary","_id=?", new String[]{showDiary.getId()});
            //跳转回到首页
            Intent intent = new Intent(ShowDiary.this,MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
