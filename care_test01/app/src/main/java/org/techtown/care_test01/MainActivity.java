package org.techtown.care_test01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    TextView email;
    Intent intent;
    Button find_level_bt,find_center_bt,find_caregiver_bt,mypage_bt,checklist_bt;
    int a = 0;
    private static final String TAG = "Cannot invoke method length() on null object";   

    public void tcp_connetion_service (){
        //service 연결
        Intent intent = new Intent(getApplicationContext(),Client_Tcp_connetion_server.class);
        startService(intent);
    };

    @SuppressLint({"SetTextI18n", "LongLogTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dial_log);
        a=getIntent().getIntExtra("tcp",1);
        //메인에서 서비스 스타트

        if(a==1){
            Log.e(TAG, "서버 최초한번실행: " );
            tcp_connetion_service();
        }

        email=findViewById(R.id.email);
        email.setText(session.getId());
        find_level_bt=findViewById(R.id.find_level_bt);
        find_center_bt=findViewById(R.id.find_center_bt);
        find_caregiver_bt=findViewById(R.id.find_caregiver_bt);
        mypage_bt=findViewById(R.id.mypage_bt);
        checklist_bt=findViewById(R.id.checklist_bt);


        find_level_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.longtermcare.or.kr/npbs/e/b/201/npeb201m01.web?menuId=npe0000000080&prevPath=/npbs/e/b/502/npeb502m01.web"));
                startActivity(intent);

            }
        });

        find_center_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   Intent intent = new Intent(getApplicationContext(),center_map_Activity.class);
                                                startActivity(intent);

            }
        });

        find_caregiver_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(), caregiver_Request_Activity.class);
                                                startActivity(intent);

            }
        });

        mypage_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),mypage_Activity.class);
                                                startActivity(intent);

            }
        });

        //수가표확인
        checklist_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Custom Dialog");
                dialog.show();

            }
        });
    }
}