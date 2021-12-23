package org.techtown.care_test01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class mypage_center_list_Activity extends AppCompatActivity {

   private static final String TAG = "Cannot invoke method length() on null object";
    RecyclerView mypage_center_recy;
    JSONArray cneter_jsonArray;
    JSONObject center_jsonObject;
    ArrayList <like_center> like_centerArrayList;
    mypage_center_list_Adapter mypage_center_list_adapter;
    ImageView like_center_list_back_bt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_center_list);

        mypage_center_recy=findViewById(R.id.mypage_center_recy);
        like_center_list_back_bt=findViewById(R.id.like_center_list_back_bt);
        mypage_center_recy.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        mypage_center_recy.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        like_centerArrayList=new ArrayList<>();
        mypage_center_list_adapter = new mypage_center_list_Adapter(getApplicationContext(),like_centerArrayList);
        mypage_center_recy.setAdapter(mypage_center_list_adapter);

        like_center_list_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),mypage_Activity.class);
                                                startActivity(intent);
            }
        });

        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
        @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
        @Override
        public void onResponse(String response) {
                Log.e(TAG, "onResponse: "+response );

            try {
                center_jsonObject=new JSONObject(response);
                cneter_jsonArray=new JSONArray(center_jsonObject.getString("response"));

                for (int i = 0; i < cneter_jsonArray.length(); i++){
                    center_jsonObject=cneter_jsonArray.getJSONObject(i);
                    like_centerArrayList.add(new like_center(
                            center_jsonObject.getString("name"),
                            center_jsonObject.getString("address"),
                            center_jsonObject.getString("tel"),
                            Double.valueOf(center_jsonObject.getString("x")),
                            Double.valueOf(center_jsonObject.getString("y"))
                            )
                    );
                    mypage_center_list_adapter.setArrayList(like_centerArrayList,getApplicationContext());
                    mypage_center_recy.setAdapter(mypage_center_list_adapter);
                    mypage_center_list_adapter.notifyDataSetChanged();

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
                smpr.addStringParam("mode","mypage_like_list");
                smpr.addStringParam("id",session.getId());


                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);


    }
}