package org.techtown.care_test01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

public class caregiver_info_Activity extends AppCompatActivity {

    JSONObject php_jsonObject;
    JSONArray php_jsonArray;
    private static final String TAG = "Cannot invoke method length() on null object";
    ImageView resum_profile,care_giver_info_back_space;
    TextView resum_status;
    TextView resum_name, resum_sx, resum_age, resum_lo, resum_level, resum_cf, resum_wh,
            resum_ml, resum_mv, resum_df, text_resum;

    Button matching_bt, view_df,resum_evaluation,mathing_no_bt;
    String Members;
    String ch1, ch2, ch3, cs_resum_index ,cs_id;
    ImageView imageView;
    int a = 0;
    int match =1;
    LinearLayout respone;
    String layoutrespone;

      public void layout() {
          resum_profile = findViewById(R.id.resum_profile);
          resum_name = findViewById(R.id.resum_name);
          resum_sx = findViewById(R.id.resum_sx);
          resum_age = findViewById(R.id.resum_age);
          resum_lo = findViewById(R.id.resum_lo);
          resum_level = findViewById(R.id.resum_level);
          resum_cf = findViewById(R.id.resum_cf);
          resum_wh = findViewById(R.id.resum_wh);
          resum_ml = findViewById(R.id.resum_ml);
          resum_mv = findViewById(R.id.resum_mv);
          care_giver_info_back_space = findViewById(R.id.care_giver_info_back_space);
          resum_df = findViewById(R.id.resum_df);
          mathing_no_bt = findViewById(R.id.mathing_no_bt);
          resum_evaluation = findViewById(R.id.resum_evaluation);
          respone=findViewById(R.id.respone);
          matching_bt = findViewById(R.id.matching_bt);
          view_df = findViewById(R.id.view_df);
          Dialog dialog = new Dialog(this);
          dialog.setContentView(R.layout.dial_log_document);
          ch1 = "스스로 가능하신 분";
          ch2 = "전지적 도움이 필요하신 분";
          ch3 = "둘다 상관없음";
          php_jsonArray = new JSONArray();
          php_jsonObject = new JSONObject();
          layoutrespone=getIntent().getStringExtra("layoutrespone");
        if(layoutrespone.equals("1")){
            respone.setVisibility(View.GONE);
        }else {
            respone.setVisibility(View.VISIBLE);
        }


      }

    public void volley_response() {


          SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
          @SuppressLint({"LongLogTag", "UseCompatLoadingForDrawables", "SetTextI18n"})
          @Override
          public void onResponse(String response) {
                  Log.e(TAG, "onResponse: "+response );

              try {
                  php_jsonObject=new JSONObject(response);
                  php_jsonArray=new JSONArray(php_jsonObject.getString("response"));
                  for (int i = 0; i < php_jsonArray.length(); i++){
                      php_jsonObject = php_jsonArray.getJSONObject(i);

                  }
                  cs_resum_index=php_jsonObject.getString("index");
                  cs_id=php_jsonObject.getString("id");
                  resum_name.setText(php_jsonObject.getString("name"));
                  if ("".equals(php_jsonObject.getString("pp")) || null == php_jsonObject.getString("pp")) {
                      resum_profile.setImageDrawable(getResources().getDrawable(R.drawable.userss));
                  } else {

                      Glide.with(getApplicationContext()).load(url.ip + php_jsonObject.getString("pp")).into(resum_profile);
                  }
                  if ("1".equals(php_jsonObject.getString("sx"))) {
                      resum_sx.setText("남자");
                  } else {
                      resum_sx.setText("여자");
                  }
                  resum_age.setText(php_jsonObject.getString("age"));
                  if ("n".equals(php_jsonObject.getString("lo"))) {
                      resum_lo.setText("지역 상관없음");
                  } else {
                      resum_lo.setText(php_jsonObject.getString("lo"));
                  }
                  if ("1".equals(php_jsonObject.getString("level"))) {
                      resum_level.setText(php_jsonObject.getString("level") + " 년 이하 ");
                  } else {
                      resum_level.setText(php_jsonObject.getString("level") + " 년");
                  }

                 String str = php_jsonObject.getString("cf");
                  String text = str.replace("|","  ");
                  resum_cf.setText(text);


                  if ("1".equals(php_jsonObject.getString("wh"))) {
                      resum_wh.setText(ch1);
                  } else if ("2".equals(php_jsonObject.getString("wh"))) {
                      resum_wh.setText(ch2);
                  } else {
                      resum_wh.setText(ch3);
                  }

                  if ("1".equals(php_jsonObject.getString("mv"))) {
                      resum_mv.setText(ch1);
                  } else if ("2".equals(php_jsonObject.getString("mv"))) {
                      resum_mv.setText(ch2);
                  } else {
                      resum_mv.setText(ch3);
                  }
                  if ("1".equals(php_jsonObject.getString("ml"))) {
                      resum_ml.setText(ch1);
                  } else if ("2".equals(php_jsonObject.getString("ml"))) {
                      resum_ml.setText(ch2);
                  } else {
                      resum_ml.setText(ch3);
                  }

                  resum_df.setText(php_jsonObject.getString("df") + "개");
                  a = Integer.parseInt(php_jsonObject.getString("df"));
                  Log.e(TAG, "첨부서류" + a);
                  if (0 >= a) {
                      view_df.setVisibility(View.GONE);
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
                  smpr.addStringParam("mode","caregiver_info");
                  smpr.addStringParam("id",getIntent().getStringExtra("cs_id"));

                  //요청객체를 서버로 보낼 우체통 같은 객체 생성
                  RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                  requestQueue.add(smpr);


    }



    public void click(){
          View.OnClickListener clickListener =new View.OnClickListener() {
              @SuppressLint({"LongLogTag", "NonConstantResourceId"})
              @Override
              public void onClick(View v) {
                  switch(v.getId()){
                      case  R.id.view_df:
                          Log.e(TAG, "서류보기: " );
                             Intent intent = new Intent(getApplicationContext(),view_df_Activity.class);
                             intent.putExtra("갯수",a);
                             intent.putExtra("id",cs_id);
                                                          startActivity(intent);
                      break;
                      case  R.id.matching_bt:
                          Log.e(TAG, "매칭수락: " );
                          try {
                              macth_click();
                          } catch (JSONException | IOException e) {
                              e.printStackTrace();
                          }
                                                  msg.toast(getApplicationContext(),"매칭수락 완료!");
                           Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
                                                        startActivity(intent2);
                                                        finish();
                          break;

                      case  R.id.mathing_no_bt:
                          Log.e(TAG, "매칭거절: " );
                          match=2;
                          try {
                              macth_click();
                          } catch (JSONException | IOException e) {
                              e.printStackTrace();
                          }
                          msg.toast(getApplicationContext(),"매칭수락 거절완료!");
                          Intent intent3 = new Intent(getApplicationContext(),MainActivity.class);
                          startActivity(intent3);
                          finish();
                          break;

                      case  R.id.resum_evaluation:
                          Log.e(TAG, "평가보기: " );
                             Intent intent4 = new Intent(getApplicationContext(),Care_giver_review_Activity.class);
                             intent4.putExtra("cs_id",cs_id);
                                                          startActivity(intent4);
                          break;

                  }
              }

          };
        view_df.setOnClickListener(clickListener);
        matching_bt.setOnClickListener(clickListener);
        mathing_no_bt.setOnClickListener(clickListener);
        resum_evaluation.setOnClickListener(clickListener);
    }

    public void macth_click() throws JSONException, IOException {

        php_jsonObject = new JSONObject();
        php_jsonArray =new JSONArray();
        php_jsonObject.put("from_user",session.getId());
        php_jsonObject.put("to_user",cs_id);
        php_jsonObject.put("cs_name",getIntent().getStringExtra("cs_name"));
        php_jsonObject.put("index",getIntent().getStringExtra("cl_index"));
        php_jsonObject.put("delete","3");
        php_jsonObject.put("match",String.valueOf(match));
        php_jsonObject.put("match_cs","cl");
        php_jsonArray.put(php_jsonObject);
        PrintWriter sendWriter = new PrintWriter(Client_Tcp_connetion_server.c_socket.getOutputStream());
        new Thread(){
            @Override
            public void run() {
                super.run();
                sendWriter.println(php_jsonArray.toString());
                sendWriter.flush();
            }
        }.start();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_giver_info);
        layout();
        volley_response();
        click();

        care_giver_info_back_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),mypage_Activity.class);
                                                startActivity(intent);
            }
        });

    }
}