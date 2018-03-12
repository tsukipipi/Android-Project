package com.example.win.diary;

import java.io.Serializable;

/**
 * Created by win on 2017/12/6.
 */

public class MyDiary implements Serializable {

    private String id;
    private String Title;
    private String Date;
    private String Content;

    MyDiary(){
    }

    MyDiary(String id,String title,String date,String content){
        this.id = id;
        this.Title = title;
        this.Date = date;
        this.Content = content;
    }

    String getId(){
        return id;
    }

    String getTitle(){
        return Title;
    }

    String getDate(){
        return Date;
    }

    String getContent(){
        return Content;
    }

    void setTitle(String title){
        this.Title = title;
    }

    void setContent(String content){
        this.Content = content;
    }

}
