package org.techtown.care_test01;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class join_Activity extends AppCompatActivity {

    EditText join_id,join_pw,join_pw2,join_number;
    Button login_bt,id_check;
    TextView id_check_st;
    int a= 0;
    private static final String TAG = "Cannot invoke method length() on null object";
    JSONArray id_jsonArray;
    JSONObject id_jsonObject,join_jsonObject,auto_login_jsonObject;
    AlertDialog.Builder builder;
    String input;
    int auto = 0;
    String Members;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("LongLogTag")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        join_id=findViewById(R.id.join_id);
        join_pw=findViewById(R.id.join_pw);
        join_pw2=findViewById(R.id.join_pw2);
        join_number=findViewById(R.id.join_number);
        login_bt=findViewById(R.id.login_bt);
        //id_check=findViewById(R.id.id_check);
        id_check_st=findViewById(R.id.id_check_st);
        Typeface typeface = getResources().getFont(R.font.tway_air);
        id_check_st.setTypeface(typeface);

        SharedPreferences onCreate_open = getSharedPreferences("Members", 0);
        Members = onCreate_open.getString("Members", "");
        Log.e(TAG, "자동로그인정보불러옴 " + Members);
        join_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //임시비번 화면 한개더
            //focusable
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input = join_id.getText().toString();
              //  Log.e(TAG, "onTextChanged:"+input );
                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                 //       Log.e(TAG, "onResponse: "+response );
                        try {
                            id_jsonObject=new JSONObject(response);
                            boolean success =id_jsonObject.getBoolean("success");
                            if(success){

                                id_check_st.setText("사용가능한 이메일입니다.");
                                id_check_st.setTextColor(Color.parseColor("#FF3700B3"));

                                a=0;
                            }else {
                                id_check_st.setText("사용불가능한 이메일입니다.");
                                id_check_st.setTextColor(Color.parseColor("#FF0000"));
                                a=1;
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
                smpr.addStringParam("mode","check_id");
                smpr.addStringParam("id",input);


                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        login_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(a==1) {
                    msg.toast(getApplicationContext(), "이메일을 확인하여주세요");
                    }else {
                    if (join_id.getText().toString().equals("") || join_id.getText().toString() == null) {
                        msg.toast(getApplicationContext(), "이메일을 입력하여주세요");
                    } else {
//                        try {
//                            auto_login_jsonObject.put("이메일",input);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        if (join_pw.getText().toString().equals("")||join_pw.getText().toString()==null|| !join_pw.getText().toString().equals(join_pw2.getText().toString())) {
                            msg.toast(getApplicationContext(), "비밀번호를 확인하여주세요");
                        } else {
                            if(join_number.getText().toString().equals("")||join_number.getText().toString()==null){
                                msg.toast(getApplicationContext(), "휴대폰번호를 입력하여주세요");
                            }else {
                                //-짝대기거르기
                                if(join_number.getText().toString().contains("-")){
                                    msg.toast(getApplicationContext(), "휴대폰번호 양식을 참고해주세요");
                                }else {

                                    SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                                Log.e(TAG, "onResponse: "+response );
                                            try {
                                                join_jsonObject=new JSONObject(response);
                                                boolean success =join_jsonObject.getBoolean("success");
                                                if(success){
                                                       msg.toast(getApplicationContext(),"가입완료");

                                                    auto_login_jsonObject=new JSONObject();
                                                    id_jsonArray=new JSONArray();
                                                    auto_login_jsonObject.put("이메일",input);
                                                    auto_login_jsonObject.put("자동로그인",0);
                                                    id_jsonArray.put(auto_login_jsonObject);
                                                    SharedPreferences sharedPreferences = getSharedPreferences("Members", 0);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("Members", id_jsonArray.toString());
                                                    editor.commit();

                                                    Intent intent = new Intent(getApplicationContext(),login_Activity.class);
                                                                                          startActivity(intent);
                                                                                          finish();
                                                }else {
                                                    msg.toast(getApplicationContext(),"회원가입에 실패하였습니다");
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
                                                smpr.addStringParam("mode","register");
                                                smpr.addStringParam("id",input);
                                                smpr.addStringParam("pw",join_pw.getText().toString());
                                                Log.e(TAG, "회원가입휴대폰번호"+join_number.getText().toString() );
                                                smpr.addStringParam("number",join_number.getText().toString());

                                                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                                                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                                                requestQueue.add(smpr);
                                }
                            }
                        }

                    }
                }

            }
        });


    }
}