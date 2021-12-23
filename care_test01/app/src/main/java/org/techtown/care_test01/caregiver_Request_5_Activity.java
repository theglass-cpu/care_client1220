package org.techtown.care_test01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class caregiver_Request_5_Activity extends AppCompatActivity {

    private static final String TAG = "Cannot invoke method length() on null object";
    JSONObject request_jsonObject;
    Button result_last_bt;
    RadioGroup radioGroup, radioGroup2, radioGroup3;
    RadioButton w_radio_bt1, w_radio_bt2, radio_bt11, radio_bt12, radio_bt21, radio_bt22;
    View.OnClickListener clickListener;
    EditText remark;
    String date_text;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_request5);

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup2 = findViewById(R.id.radioGroup2);
        radioGroup3 = findViewById(R.id.radioGroup3);
        result_last_bt=findViewById(R.id.result_last_bt);
        remark=findViewById(R.id.remark);
        w_radio_bt1 = findViewById(R.id.w_radio_bt1);
        w_radio_bt2 = findViewById(R.id.w_radio_bt2);
        radio_bt11 = findViewById(R.id.radio_bt11);
        radio_bt12 = findViewById(R.id.radio_bt12);
        radio_bt21 = findViewById(R.id.radio_bt21);
        radio_bt22 = findViewById(R.id.radio_bt22);


        radioGroup.check(w_radio_bt1.getId());
        radioGroup2.check(radio_bt11.getId());
        radioGroup3.check(radio_bt21.getId());

        Date currentTime = Calendar.getInstance().getTime();
        date_text = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN).format(currentTime);

        Log.d("webnautes", date_text);

        String request = session.getSharedPreferences("request", 0).getString("request", "");
        Log.e(TAG, "onCreate: " + request);
        try {
            request_jsonObject = new JSONObject(request);
            request_jsonObject.put("wash",0);
            request_jsonObject.put("meal",0);
            request_jsonObject.put("move",0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.w_radio_bt1:
                        try {
                            request_jsonObject.put("wash",0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "스스로가능" );
                    break;

                    case R.id.w_radio_bt2:
                        try {
                            request_jsonObject.put("wash",1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "전지적");
                        break;
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                  case R.id.radio_bt11 :
                      try {
                          request_jsonObject.put("meal",0);
                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                      break;

                    case R.id.radio_bt12 :
                        try {
                            request_jsonObject.put("meal",1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                  case R.id.radio_bt21 :
                      try {
                          request_jsonObject.put("move",0);
                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                      break;

                    case R.id.radio_bt22 :
                        try {
                            request_jsonObject.put("move",1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });


        setClickListener();

    }
    public void setClickListener(){
        clickListener=new View.OnClickListener() {
            @SuppressLint({"NonConstantResourceId", "LongLogTag"})
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                  case R.id.result_last_bt :
                      try {
                          request_jsonObject.put("remark",remark.getText().toString());
                          Log.e(TAG, "onClick: "+request_jsonObject.toString() );

                          SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
                          @Override
                          public void onResponse(String response) {
                                  Log.e(TAG, "onResponse: "+response );
                                     msg.toast(getApplicationContext(),"제출이 완료되었습니다");
                                     Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                                  startActivity(intent);

                                  }
                                  }, new Response.ErrorListener() {
                          @Override
                          public void onErrorResponse(VolleyError error) {

                                  }
                                  });
                                  smpr.addStringParam("mode","client_request");
                                  smpr.addStringParam("id",session.getId());
                                  smpr.addStringParam("type",request_jsonObject.getString("type"));
                                  smpr.addStringParam("날짜",request_jsonObject.getString("날짜"));
                                  smpr.addStringParam("place",request_jsonObject.getString("place"));
                                  smpr.addStringParam("timeout",request_jsonObject.getString("timeout"));
                                  smpr.addStringParam("timestart",request_jsonObject.getString("timestart"));
                                  smpr.addStringParam("care_level",request_jsonObject.getString("care_level"));
                                  smpr.addStringParam("sex",request_jsonObject.getString("sex"));
                                  smpr.addStringParam("age",request_jsonObject.getString("age"));
                                  smpr.addStringParam("adress",request_jsonObject.getString("adress"));
                                  smpr.addStringParam("wash",request_jsonObject.getString("wash"));
                                  smpr.addStringParam("meal",request_jsonObject.getString("meal"));
                                  smpr.addStringParam("move",request_jsonObject.getString("move"));
                                  smpr.addStringParam("remark",request_jsonObject.getString("remark"));
                                  smpr.addStringParam("write_date",date_text);

                                  //요청객체를 서버로 보낼 우체통 같은 객체 생성
                                  RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                                  requestQueue.add(smpr);

                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                      break;


                }
            }
        };

        result_last_bt.setOnClickListener(clickListener);


    }

   

}