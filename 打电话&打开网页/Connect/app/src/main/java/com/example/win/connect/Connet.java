package com.example.win.connect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Connet extends AppCompatActivity {

    private EditText message;
    private Button enter;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connet);

        message = (EditText) findViewById(R.id.et_number);
        enter = (Button) findViewById(R.id.btn_enter);
        cancel = (Button) findViewById(R.id.btn_cancel);

        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");

        //设置不同的提示信息
        if(type.equals("call")){
            message.setHint("input the number you want to call");
        }
        else if(type.equals("net")){
            message.setHint("input the net you want to surf");
        }

        //点击确定
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //先判断EditText是否为空
                if(message.getText().toString().trim() == null || message.getText().toString().trim().equals("")){
                    //提示对话框
                    Toast.makeText(Connet.this,"Empty Input",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //选择打电话
                    if(type.equals("call")){
                        Intent intent = new Intent(Intent.ACTION_CALL,
                                Uri.parse("tel:" + message.getText().toString().trim()));
                        //检查是否有打电话的权限
                        if (ActivityCompat.checkSelfPermission(Connet.this, Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            return ;
                        }
                        startActivity(intent);
                    }
                    //选择上网
                    else if(type.equals("net")){
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        //intent.setData(Uri.parse("https://www.baidu.com/"));
                        intent.setData(Uri.parse(message.getText().toString().trim()));
                        startActivity(intent);
                    }

                }
            }
        });

        //点击取消按钮
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Connet.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
