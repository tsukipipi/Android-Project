package com.example.win.daily;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddFriend extends AppCompatActivity {


    EditText Name;
    EditText Sex;
    EditText Birthday;
    EditText Phone;
    EditText Message;
    Button enter;


    //SQLiteOpenHelper子类
    MyHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        myHelper = new MyHelper(AddFriend.this);

        //实例化
        Name = (EditText) findViewById(R.id.et_friendName);
        Sex = (EditText) findViewById(R.id.et_friendSex);
        Birthday = (EditText) findViewById(R.id.et_friendBirthday);
        Phone = (EditText) findViewById(R.id.et_friendPhone);
        Message = (EditText) findViewById(R.id.et_friendMessage);
        enter = (Button) findViewById(R.id.btn_enter);

        //添加到数据库中
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取可写入的SQLiteDatabase对象
                SQLiteDatabase db = myHelper.getWritableDatabase();
                //创建ContentValues对象
                ContentValues values = new ContentValues();

                //判断姓名是否为空
                if(TextUtils.isEmpty(Name.getText().toString().trim())){
                    Toast.makeText(AddFriend.this,"名字不可以为空！",Toast.LENGTH_SHORT).show();
                }
                else {
                    //将信息插入到数据库中
                    values.put("name",Name.getText().toString().trim());
                    values.put("sex",Sex.getText().toString().trim());
                    values.put("birthday",Birthday.getText().toString().trim());
                    values.put("phone",Phone.getText().toString().trim());
                    values.put("message",Message.getText().toString().trim());
                    db.insert("information",null,values);
                }

                //关闭数据库
                db.close();

                //跳转回到原始页面
                Intent intent = new Intent(AddFriend.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

}
