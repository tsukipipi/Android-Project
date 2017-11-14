package com.example.win.connect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button call;
    private Button net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        call = (Button) findViewById(R.id.btn_call);
        net = (Button) findViewById(R.id.btn_net);

        //打电话
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Connet.class);
                intent.putExtra("type","call");
                startActivity(intent);
            }
        });

        //打开网页
        net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Connet.class);
                intent.putExtra("type","net");
                startActivity(intent);
            }
        });

    }
}
