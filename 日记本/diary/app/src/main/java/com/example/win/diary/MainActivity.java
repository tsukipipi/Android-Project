package com.example.win.diary;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private List<MyDiary> DiaryList;
    private List<MyDiary> SearchList;

    MyHelper myHelper;
    MyAdapter mAdapter;

    ListView mlistview;
    SearchView searchDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DiaryList = new ArrayList<MyDiary>();
        SearchList = new ArrayList<MyDiary>();

        mlistview = (ListView) findViewById(R.id.lv_diaryList);
        searchDiary = (SearchView) findViewById(R.id.sv_search);

        myHelper = new MyHelper(MainActivity.this);
        ReadData();
        //创建一个Adapter的实例
        mAdapter = new MyAdapter(MainActivity.this,DiaryList);
        //设置适配器
        mlistview.setAdapter(mAdapter);

        //设置点击搜索事件
        searchDiary.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDiary();
            }
        });


        searchDiary.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                searchDiary();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                //正常情况下将列表设置为全部数据
                mlistview.setAdapter(new MyAdapter(MainActivity.this,DiaryList));
                return true;
            }
        });


        //浮动按钮点击事件
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent intent = new Intent(MainActivity.this,WriteDiary.class);
                //设置跳转的目的
                intent.putExtra("purpose","add");
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Refresh();
    }

    //刷新列表
    void Refresh(){
        ReadData();
        mAdapter.notifyDataSetChanged();
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
        if (id == R.id.action_search) {
            //结束搜索
            if(searchDiary.getVisibility() == View.VISIBLE){
                searchDiary.setVisibility((View.GONE));
                item.setTitle("搜索");
            }
            //搜索
            else if(searchDiary.getVisibility() == View.GONE){
                //设置查找的控件可见
                searchDiary.setVisibility(View.VISIBLE);
                item.setTitle("结束搜索");
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //从数据库读取内容
    void ReadData(){
        //获得可以读取数据的SQLiteDataBase对象
        SQLiteDatabase db = myHelper.getReadableDatabase();
        //游标
        Cursor cursor = db.query("diary",null,
                null,null,null,
                null,null);

        if(cursor.getCount() == 0){
            Toast.makeText(MainActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
        }
        else{
            try{
                //先清除数据
                DiaryList.clear();
                //将数据库的信息添加到列表中
                cursor.moveToLast();
                int position = cursor.getPosition();
                while(position >= 0){
                    position --;
                    DiaryList.add(new MyDiary(cursor.getString(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("title")),
                            cursor.getString(cursor.getColumnIndex("date")),
                            cursor.getString(cursor.getColumnIndex("content"))));
                    cursor.moveToPosition(position);
                }
            }
            //空指针异常
            catch (NullPointerException e){
            }
        }//end else
        //关闭游标和数据库
        cursor.close();
        db.close();
    }

    public void searchDiary(){
        //查询语句
        String querytext = searchDiary.getQuery().toString().trim();
        //判断查询语句是否为空
        if (TextUtils.isEmpty(querytext)) {
            Toast.makeText(MainActivity.this, "输入为空！", Toast.LENGTH_SHORT).show();
        }
        else {
            //请空列表
            SearchList.clear();
            for (int i = 0; i < DiaryList.size(); i++) {
                if (DiaryList.get(i).getTitle().toString().equals(querytext))
                    SearchList.add(DiaryList.get(i));
            }
            //创建一个Adapter的实例,设置适配器
            mlistview.setAdapter(new MyAdapter(MainActivity.this, SearchList));
            //mAdapter.notifyDataSetChanged();
        }
    }

}
