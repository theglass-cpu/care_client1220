package org.techtown.care_test01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.Date;

public class find_Activity extends AppCompatActivity {

    EditText find_id,find_pw;
    Button find_id_bt,find_pw_bt;
    private static final String TAG = "Cannot invoke method length() on null object";
    JSONObject find_id_jsonObject;
    JSONArray find_id_jsonArray;
    JSONArray login_jsonArray;
    JSONObject auto_jsonObject;
    String phone;
    String Members;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        find_id=findViewById(R.id.find_id);
        find_pw=findViewById(R.id.find_pw);
        find_id_bt=findViewById(R.id.find_id_bt);
        find_pw_bt=findViewById(R.id.find_pw_bt);
        SharedPreferences onCreate_open = getSharedPreferences("Members", 0);
        Members = onCreate_open.getString("Members", "");
        Log.e(TAG, "자동로그인정보불러옴 " + Members);

        find_id_bt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                @SuppressLint("LongLogTag")
                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response );
                    try {
                        find_id_jsonObject=new JSONObject(response);
                        find_id_jsonArray=new JSONArray(find_id_jsonObject.getString("response"));
                        for (int i = 0; i < find_id_jsonArray.length(); i++){
                                    find_id_jsonObject=find_id_jsonArray.getJSONObject(i);
                                       phone = find_id_jsonObject.getString("id");

                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(find_Activity.this);
                        builder.setMessage("아이디 : "+phone)
                                .setPositiveButton("ok", null)
                                .create()
                                .show();





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                        }
                        });
                        smpr.addStringParam("mode","find_id");
                        smpr.addStringParam("number",find_id.getText().toString());
                       Log.e(TAG, "onClick: "+find_id.getText().toString() );

                        //요청객체를 서버로 보낼 우체통 같은 객체 생성
                        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(smpr);
            }
        });

        find_pw_bt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: "+getRamdomPassword(8) );

                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, "http://3.37.212.160/and_care/mail.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response );
                        msg.toast(getApplicationContext(),"메일로 임시비밀번호를 발송하였습니다");
                    try {
                        login_jsonArray=new JSONArray(Members);
                        for (int i = 0; i < login_jsonArray.length(); i++){
                            auto_jsonObject=login_jsonArray.getJSONObject(i);
                            auto_jsonObject.put("자동로그인",0);
                            login_jsonArray.put(i,auto_jsonObject);
                            Log.e(TAG, "!?!?! " +auto_jsonObject.get("자동로그인") );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                            SharedPreferences sph = getSharedPreferences("Members", 0);
                            SharedPreferences.Editor editor1 = sph.edit();
                            editor1.putString("Members", login_jsonArray.toString());
                            editor1.commit();

                    Intent intent = new Intent(getApplicationContext(),login_Activity.class);
                                                        startActivity(intent);
                        }
                        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                        }
                        });
                        smpr.addStringParam("mode","care_client");
                        smpr.addStringParam("id",find_pw.getText().toString());
                        smpr.addStringParam("pw",getRamdomPassword(8));

                        //요청객체를 서버로 보낼 우체통 같은 객체 생성
                        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(smpr);

            }
        });

    }

    public String getRamdomPassword(int size) {
        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@' };

        StringBuffer sb = new StringBuffer();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());

        int idx = 0;
        int len = charSet.length;
        for (int i=0; i<size; i++) {
            // idx = (int) (len * Math.random());
            idx = sr.nextInt(len);    // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
            sb.append(charSet[idx]);
        }

        return sb.toString();
    }

}