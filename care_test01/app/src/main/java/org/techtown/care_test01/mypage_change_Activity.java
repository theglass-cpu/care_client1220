package org.techtown.care_test01;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class mypage_change_Activity extends AppCompatActivity {

 EditText pw,new_pw,new_pw2;
 Button result_bt;
 TextView id;
 JSONObject jsonObject ,auto_jsonObject;
 JSONArray login_jsonArray;
    private static final String TAG = "Cannot invoke method length() on null object";
    String Members;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_change);
        pw=(EditText)findViewById(R.id.pw);
        new_pw=(EditText)findViewById(R.id.new_pw);
        new_pw2=(EditText)findViewById(R.id.new_pw2);
        result_bt=(Button)findViewById(R.id.result_bt);
        id=(TextView)findViewById(R.id.id);
        id.setText(session.getId());

        SharedPreferences onCreate_open = getSharedPreferences("Members", 0);
        Members = onCreate_open.getString("Members", "");
        Log.e(TAG, "자동로그인정보불러옴 " + Members);

        result_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pw.getText().toString().equals("")||pw.getText().toString()==null){
                 msg.toast(getApplicationContext(),"공백이 존재합니다");
                }else{
                    if(new_pw.getText().toString().equals("")||new_pw.getText().toString()==null){
                        msg.toast(getApplicationContext(),"공백이 존재합니다");
                    }else {
                     if(!new_pw.getText().toString().equals(new_pw2.getText().toString())){
                         msg.toast(getApplicationContext(),"비밀번호가 일치하지않습니다");
                     }   else {
                            //조건충족

                            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onResponse(String response) {
                                    Log.e(TAG, "onResponse: "+response );
                                try {
                                    jsonObject=new JSONObject(response);
                                    boolean success =jsonObject.getBoolean("success");
                                    if(success){
                                        msg.toast(getApplicationContext(),"비밀번호 변경성공");
                                           login_jsonArray=new JSONArray(Members);
                                           for (int i = 0; i < login_jsonArray.length(); i++){
                                                       auto_jsonObject=login_jsonArray.getJSONObject(i);
                                                       auto_jsonObject.put("자동로그인",0);
                                                       login_jsonArray.put(i,auto_jsonObject);
                                               Log.e(TAG, "!?!?! " +auto_jsonObject.get("자동로그인") );
                                           }
                                        SharedPreferences sph = getSharedPreferences("Members", 0);
                                        SharedPreferences.Editor editor1 = sph.edit();
                                        editor1.putString("Members", login_jsonArray.toString());
                                        editor1.commit();

                                           Intent intent = new Intent(getApplicationContext(),login_Activity.class);
                                                                        startActivity(intent);


                                    }else {
                                        msg.toast(getApplicationContext(),"비밀번호를 다시확인하여 주세요");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                    }
                                    }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                    }
                                    });
                                    smpr.addStringParam("mode","new_pw");
                                    smpr.addStringParam("id",id.getText().toString());
                                    smpr.addStringParam("pw",pw.getText().toString());
                                    smpr.addStringParam("pw2",new_pw.getText().toString());

                                    //요청객체를 서버로 보낼 우체통 같은 객체 생성
                                    RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                                    requestQueue.add(smpr);
                        }
                    }
                }
            }
        });

    }
}