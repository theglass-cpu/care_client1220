package org.techtown.care_test01;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class mypage_Activity extends AppCompatActivity {

    Button mypage_change_bt,like_center_bt,caregiver_bt,my_list_bt,match_list_bt;
    TextView mypage_id;
    ImageView mypage_back_bt;
    LinearLayout request_list_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        mypage_change_bt=(Button)findViewById(R.id.mypage_change_bt);
        like_center_bt=(Button)findViewById(R.id.like_center_bt);
        caregiver_bt=(Button)findViewById(R.id.caregiver_bt);
        my_list_bt=(Button)findViewById(R.id.my_list_bt);
        mypage_id=(TextView) findViewById(R.id.mypage_id);
        match_list_bt=findViewById(R.id.match_list_bt);
        mypage_back_bt=findViewById(R.id.mypage_back_bt);
        request_list_bt=findViewById(R.id.request_list_bt);
        mypage_id.setText(session.getId());


        //내가 간병인 요청한내역
        request_list_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(), Client_request_list_Activity.class);
                                                startActivity(intent);
            }
        });

        mypage_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                startActivity(intent);
            }
        });

        mypage_change_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   Intent intent = new Intent(getApplicationContext(),mypage_change_Activity.class);
                                                startActivity(intent);

            }
        });

        like_center_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),mypage_center_list_Activity.class);
                                                startActivity(intent);

            }
        });

        //간병인신청내역
        caregiver_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),caregiver_request_list_Activity.class);
                                                startActivity(intent);

            }
        });

        //매칭내역
        match_list_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   Intent intent = new Intent(getApplicationContext(),match_list_Activity.class);
                                                startActivity(intent);

            }
        });

        //이용내역
        my_list_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),Use_list_Activity.class);
                                                startActivity(intent);


            }
        });
    }
}