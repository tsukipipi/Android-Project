package com.example.win.daily;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button friend;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //实例化按钮
        friend = (Button) findViewById(R.id.btn_friend);
        add = (Button) findViewById(R.id.btn_add);
        //设置点击事件
        friend.setOnClickListener(this);
        add.setOnClickListener(this);

    }

    //设置点击事件
    public void onClick(View view){

        switch(view.getId()){
            case R.id.btn_friend://查看朋友列表
                Intent intent1 = new Intent(MainActivity.this,FriendList.class);
                startActivity(intent1);
                break;
            case R.id.btn_add://增加数据
                Intent intent2 = new Intent(MainActivity.this,AddFriend.class);
                startActivity(intent2);
                break;
        }

    }

}
