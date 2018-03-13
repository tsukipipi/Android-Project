package com.example.win.daily;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FriendList extends AppCompatActivity {

    public ListView mlistview;
    public SearchView query;

    //需要适配的数据，列表存放信息
    List<Friend> friendsList;

    MyHelper myHelper;
    MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        //为List初始化
        friendsList = new ArrayList<Friend>();

        //初始化控件
        mlistview = (ListView) findViewById(R.id.lv_friendList);
        query = (SearchView) findViewById(R.id.sv_query);

        myHelper = new MyHelper(FriendList.this);
        //读取数据库中的信息
        getData("",friendsList);

        //创建一个Adapter的实例
        mAdapter = new MyAdapter(FriendList.this,friendsList);
        //设置适配器
        mlistview.setAdapter(mAdapter);


        //设置查询点击事件
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Friend> queryFriendsList = new ArrayList<Friend>();
                //查询语句
                String querytext = query.getQuery().toString().trim();
                //判断姓名是否为空
                if(TextUtils.isEmpty(querytext)){
                    Toast.makeText(FriendList.this,"输入为空！",Toast.LENGTH_SHORT).show();
                }
                else {
                    //请空列表
                    queryFriendsList.clear();
                    getData(querytext,queryFriendsList);
                    //创建一个Adapter的实例,设置适配器
                    mlistview.setAdapter(new MyAdapter(FriendList.this,queryFriendsList));
                    //mAdapter.notifyDataSetChanged();
                }
            }
        });



    }

    //读取数据库中的信息
    public void getData(String queryName,List<Friend> list){
        //获得可以读取数据的SQLiteDataBase对象
        SQLiteDatabase db = myHelper.getReadableDatabase();
        //游标
        Cursor cursor;
        if(queryName.isEmpty())
        {
            cursor = db.query("information",null,
                    null,null,null,
                    null,null);
        }
        else
        {
            cursor = db.query("information",null,
                    "name=?",new String[]{queryName},null,
                    null,null);
        }

        if(cursor.getCount() == 0){
            Toast.makeText(FriendList.this,"暂无数据",Toast.LENGTH_SHORT).show();
        }
        else{
            try{
                //将数据库的信息添加到列表中
                cursor.moveToFirst();
                list.add(new Friend(cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("sex")),
                        cursor.getString(cursor.getColumnIndex("birthday")),
                        cursor.getString(cursor.getColumnIndex("phone")),
                        cursor.getString(cursor.getColumnIndex("message"))));

                while(cursor.moveToNext()){
                    list.add(new Friend(cursor.getString(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("sex")),
                            cursor.getString(cursor.getColumnIndex("birthday")),
                            cursor.getString(cursor.getColumnIndex("phone")),
                            cursor.getString(cursor.getColumnIndex("message"))));
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

}


