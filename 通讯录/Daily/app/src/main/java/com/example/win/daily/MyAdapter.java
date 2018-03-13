package com.example.win.daily;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by win on 2017/12/4.
 */

public class MyAdapter extends BaseAdapter {

    private List<Friend> friendsList;
    private Context context;

    MyAdapter(Context c, List<Friend> fl) {
        friendsList = fl;
        context = c;
    }

    //得到item的总数
    @Override
    public int getCount() {
        //返回ListView Item条目的总数
        try{
            return friendsList.size();
        }
        catch (NullPointerException e){
            return 0;
        }
    }

    //得到item代表的对象
    @Override
    public Object getItem(int position) {
        return friendsList.get(position);
    }

    //得到item的id
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        final int p = position;

        //将list_item.xml文件找出来并转换成view对象
        View view = View.inflate(context, R.layout.activity_friend_list_item, null);
        //名字
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_name.setText("Name: "+ friendsList.get(position).getName());
        //性别
        TextView tv_sex = (TextView) view.findViewById(R.id.tv_sex);
        tv_sex.setText("Sex: "+ friendsList.get(position).getSex());
        //头像
        ImageView iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
        if(friendsList.get(position).getSex().equals("女"))iv_photo.setBackgroundResource(R.drawable.girl);
        else iv_photo.setBackgroundResource(R.drawable.boy);
        //生日
        TextView tv_birthday = (TextView) view.findViewById(R.id.tv_birthday);
        tv_birthday.setText("Birthday: "+ friendsList.get(position).getBirthday());
        //电话
        TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_phone.setText("Phone: "+ friendsList.get(position).getPhone());
        //描述
        TextView tv_content = (TextView) view.findViewById(R.id.tv_message);
        tv_content.setText("Message: "+ friendsList.get(position).getMessage());

        //点击删除的事件监听响应
        LinearLayout delete = (LinearLayout) view.findViewById(R.id.ll_deleteButton);
        delete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(context, "click delete button", Toast.LENGTH_SHORT).show();

                //从数据库删除
                MyHelper myHelper = new MyHelper(context);
                //获得可以读取数据的SQLiteDataBase对象
                SQLiteDatabase db = myHelper.getWritableDatabase();
                db.delete("information","_id=?",
                        new String[]{friendsList.get(p).getId()});
                //从朋友列表删除
                friendsList.remove(p);
                notifyDataSetChanged();
            }
        });

        return view;
    }


}
