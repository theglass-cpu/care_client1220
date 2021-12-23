package org.techtown.care_test01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import org.json.JSONException;
import org.json.JSONObject;

public class caregiver_Request_Activity extends AppCompatActivity {

  LinearLayout day_care_bt,time_care_bt;
  Button cg_next_bt_1;
  View.OnClickListener clickListener;
  private static final String TAG = "Cannot invoke method length() on null object";
  String status = "24시간상주";
  JSONObject request_jsonObject;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver);

        day_care_bt=findViewById(R.id.day_care_bt);
        time_care_bt=findViewById(R.id.time_care_bt);
        cg_next_bt_1=findViewById(R.id.cg_next_bt_1);



        clickListener = new View.OnClickListener() {
            @SuppressLint({"NonConstantResourceId", "LongLogTag"})
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                  case R.id.day_care_bt :
                      day_care_bt.setBackgroundColor((Color.YELLOW));
                      time_care_bt.setBackgroundColor((Color.WHITE));
                      Log.e(TAG, "24시간 상주" );
                      status ="1";
                    break;

                    case R.id.time_care_bt:
                        day_care_bt.setBackgroundColor((Color.WHITE));
                        time_care_bt.setBackgroundColor((Color.YELLOW));
                        Log.e(TAG, "시간제 상주" );
                        status ="0";
                    break;

                    case R.id.cg_next_bt_1:


                        try {
                            request_jsonObject =new JSONObject();
                            request_jsonObject.put("id",session.getId());
                            request_jsonObject.put("type",status);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        session.setContext(getApplicationContext());
                        session.getSharedPreferences("request",0);
                        session.setSharedPreferences("request",request_jsonObject.toString());


                           Intent intent = new Intent(getApplicationContext(),caregiver_Request_2_Activity.class);
                           startActivity(intent);


                }
            }
        };
        day_care_bt.setOnClickListener(clickListener);
        time_care_bt.setOnClickListener(clickListener);
        cg_next_bt_1.setOnClickListener(clickListener);


    }
}