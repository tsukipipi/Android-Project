package com.example.win.diary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by win on 2017/12/6.
 */

public class MyAdapter extends BaseAdapter {

    private List<MyDiary> diaryList;
    private Context context;

    //c:上下文
    //diaryList:显示列表
    MyAdapter(Context context, List<MyDiary> diaryList) {
        this.diaryList = diaryList;
        this.context = context;
    }

    //得到item的总数
    @Override
    public int getCount() {
        //返回ListView Item条目的总数
        try{
            return diaryList.size();
        }
        catch (NullPointerException e){
            return 0;
        }
    }

    //得到item代表的对象
    @Override
    public Object getItem(int position) {
        return diaryList.get(position);
    }

    //得到item的id
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        final int p = position;

        //将list_item.xml文件找出来并转换成view对象
        View view = View.inflate(context, R.layout.diary_item, null);
        //日记标题
        final TextView tv_showTitle = (TextView) view.findViewById(R.id.tv_showTitle);
        tv_showTitle.setText(diaryList.get(position).getTitle());

        //根据日期设置图标
        ImageView iv_showWeekDay = (ImageView) view.findViewById(R.id.iv_weekday);
        final String weekday = getWeekByDate(diaryList.get(p).getDate(),iv_showWeekDay);

        LinearLayout diary = (LinearLayout) view.findViewById(R.id.ll_Diary);

        //为每一个diary item设置长按删除的监听事件
        diary.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog hintDialog;
                hintDialog = new AlertDialog.Builder(context)
                        .setTitle("开玩笑的吧QAQ")
                        .setMessage("是否确定删除日记:" + tv_showTitle.getText() + " 吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //从数据库删除
                                MyHelper myHelper = new MyHelper(context);
                                //获得可以读取数据的SQLiteDataBase对象
                                SQLiteDatabase db = myHelper.getWritableDatabase();
                                db.delete("diary","_id=?",
                                        new String[]{diaryList.get(p).getId()});
                                //从日记列表删除
                                diaryList.remove(p);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                return true;
            }
        });



        //点击日记的事件监听响应
        diary.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(context,ShowDiary.class);
                intent.putExtra("diary",new MyDiary(diaryList.get(p).getId(),
                        diaryList.get(p).getTitle(),
                        diaryList.get(p).getDate(),
                        diaryList.get(p).getContent()));
                intent.putExtra("diaryWeekday",weekday);
                context.startActivity(intent);
            }
        });

        return view;
    }


    //根据日期获取处于一周的周几
    public static String getWeekByDate(String Date,ImageView iv_WeekDay)
    {
        int year = Integer.parseInt(Date.substring(0, 4));
        int month = Integer.parseInt(Date.substring(5, 7));
        int day = Integer.parseInt(Date.substring(8, 10));

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);

        String week = "";
        int weekIndex = c.get(Calendar.DAY_OF_WEEK);

        switch (weekIndex)
        {
            case 1:
                week = "Sunday";
                iv_WeekDay.setBackgroundResource(R.drawable.ic_action_sunday);
                break;
            case 2:
                week = "Monday";
                iv_WeekDay.setBackgroundResource(R.drawable.ic_action_monday);
                break;
            case 3:
                week = "Tuesday";
                iv_WeekDay.setBackgroundResource(R.drawable.ic_action_tuesday);
                break;
            case 4:
                week = "Wednesday";
                iv_WeekDay.setBackgroundResource(R.drawable.ic_action_wednesday);
                break;
            case 5:
                week = "Thursday";
                iv_WeekDay.setBackgroundResource(R.drawable.ic_action_thursday);
                break;
            case 6:
                week = "Friday";
                iv_WeekDay.setBackgroundResource(R.drawable.ic_action_friday);
                break;
            case 7:
                week = "Saturday";
                iv_WeekDay.setBackgroundResource(R.drawable.ic_action_saturday);
                break;
        }
        return week;
    }

}
