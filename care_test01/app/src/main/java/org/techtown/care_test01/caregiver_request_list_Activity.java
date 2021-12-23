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

public class caregiver_request_list_Activity extends AppCompatActivity {

    private static final String TAG = "Cannot invoke method length() on null object";
    JSONArray request_jsonArray;
    JSONObject request_jsonObject;
    RecyclerView request_recy;
    ArrayList<request_item> request_itemArrayList;
    static request_list_Adapter request_list_adapter;
    ImageView Caregiver_request_back_bt;
    String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_request_list);

        request_recy=findViewById(R.id.request_recy);
        request_recy.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        request_recy.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        request_itemArrayList=new ArrayList<>();
        request_list_adapter=new request_list_Adapter(getApplicationContext(),request_itemArrayList);
        request_recy.setAdapter(request_list_adapter);

        request_jsonArray=new JSONArray();
        request_jsonObject=new JSONObject();
        Caregiver_request_back_bt=findViewById(R.id.Caregiver_request_back_bt);


        Caregiver_request_back_bt.setOnClickListener(new View.OnClickListener() {
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
                request_jsonObject =new JSONObject(response);
                request_jsonArray =new JSONArray(request_jsonObject.getString("response"));
                for (int i = 0; i < request_jsonArray.length(); i++){
                            request_jsonObject =request_jsonArray.getJSONObject(i);
                    index=request_jsonObject.getString("index");
                            request_itemArrayList.add(new request_item(
                                    request_jsonObject.getString("cs_id"),
                                    request_jsonObject.getString("index"),
                                    request_jsonObject.getString("cs_name")));



                }
                request_list_adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
                }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

                }
                });
                smpr.addStringParam("mode","request_list");
                smpr.addStringParam("id",session.getId());

                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);

                request_list_adapter.setOnitemClicklistenter(new request_list_Adapter.OnitemClicklistener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onitemClick(View v, int pos) {
                        request_item dict = request_itemArrayList.get(pos);
                           Intent intent = new Intent(getApplicationContext(), caregiver_info_Activity.class);
                           intent.putExtra("cl_index",dict.getRequest_index());
                           intent.putExtra("cs_id",dict.getId());
                           intent.putExtra("cs_name",dict.getCs_name());
                           intent.putExtra("layoutrespone","0");
                           startActivity(intent);

                    }
                });

    }
}