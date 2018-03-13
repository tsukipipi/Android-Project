package com.example.win.daily;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by win on 2017/12/3.
 */

public class MyHelper extends SQLiteOpenHelper {

    public MyHelper(Context context){
        //创建数据库
        super(context,"friend.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //建表
        db.execSQL("CREATE TABLE information(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(20)," +
                "sex VARCHAR(2)," +
                "birthday VARCHAR(20)," +
                "phone VARCHAR(20)," +
                "message VARCHAR(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }

}
