package com.example.win.daily;

/**
 * Created by win on 2017/12/4.
 */

public class Friend {
    private String id;
    public String name;
    public String sex;
    public String birthday;
    public String phone;
    public String message;
    //构造函数
    public Friend(){
    }

    public Friend(String id,String n, String s, String bir, String ph, String mess){
        this.id = id;
        name = n;
        sex = s;
        birthday = bir;
        phone = ph;
        message = mess;
    }

    void setName(String n){
        name = n;
    }
    void setSex(String s){
        sex = s;
    }
    void setBirthday(String bir){
        birthday =bir;
    }
    void setPhone(String ph){
        phone = ph;
    }
    void setMessage(String mess){
        message = mess;
    }

    String getId(){
        return id;
    }
    String getName(){
        return name;
    }
    String getSex(){
        return sex;
    }
    String getBirthday(){
        return  birthday;
    }
    String getPhone(){
        return phone;
    }
    String getMessage(){
        return message;
    }
}
