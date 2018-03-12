package com.example.win.diary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by win on 2017/12/6.
 */

public class MyHelper extends SQLiteOpenHelper {

    public MyHelper(Context context){
        //创建数据库
        super(context,"diary.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //建表
        db.execSQL("CREATE TABLE diary(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title VARCHAR(100)," +
                "date VARCHAR(50)," +
                "content VARCHAR(1000))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }

}
