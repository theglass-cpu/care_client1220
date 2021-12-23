package org.techtown.care_test01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

import java.util.ArrayList;

public class view_df_Activity extends AppCompatActivity {

    LinearLayout line;
    ImageView imageView;
    int a , b;
    private static final String TAG = "Cannot invoke method length() on null object";
    JSONObject php_jsonObject;
    JSONArray php_jsonArray;
    String id ;
    ArrayList<Uri> uri = new ArrayList<>();


    public void volley(){


        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.resum, new Response.Listener<String>() {
        @SuppressLint("LongLogTag")
        @Override
        public void onResponse(String response) {
                Log.e(TAG, "onResponse: "+response );
            try {
                php_jsonObject= new JSONObject(response);
                php_jsonArray= new JSONArray(php_jsonObject.getString("response"));
                for (int i = 0; i < php_jsonArray.length(); i++){
                    php_jsonObject=php_jsonArray.getJSONObject(i);
                    uri.add(Uri.parse(php_jsonObject.getString("image")));
                }
                for (int i = 0; i < uri.size(); i++){
                    imageView =new ImageView(getApplicationContext());
                    Glide.with(getApplicationContext()).load(url.ip+uri.get(i)).override(700,600).into(imageView);
                    line.addView(imageView); // 기존 linearLayout에 imageView 추가
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
                smpr.addStringParam("mode", "resum_df");
                smpr.addStringParam("id", getIntent().getStringExtra("id"));

                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_df);
        line = (LinearLayout) findViewById(R.id.line);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT /* layout_width */, LinearLayout.LayoutParams.WRAP_CONTENT /* layout_height */, 1f /* layout_weight */);
        imageView = new ImageView(this);  // 새로 추가할 imageView 생성

        volley();
    }
}