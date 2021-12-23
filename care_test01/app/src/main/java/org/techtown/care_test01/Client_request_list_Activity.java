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

public class Client_request_list_Activity extends AppCompatActivity {

    private static final String TAG = "Cannot invoke method length() on null object";
    ImageView list_back_bt;
    RecyclerView client_request_recy;
    client_request_Adater client_request_adater;
    ArrayList<client_request_item> client_request_itemArrayList;
    JSONArray request_jsonArray;
    JSONObject request_jsonObject;

    public void layout(){
        list_back_bt=findViewById(R.id.list_back_bt);
        client_request_recy=findViewById(R.id.client_request_recy);
        client_request_recy.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        client_request_recy.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        client_request_itemArrayList=new ArrayList<>();
        client_request_adater= new client_request_Adater(getApplicationContext(),client_request_itemArrayList);
        client_request_recy.setAdapter(client_request_adater);
        request_jsonArray=new JSONArray();
        request_jsonObject=new JSONObject();

    }

    public void volley_request(){

        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
        @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
        @Override
        public void onResponse(String response) {
                Log.e(TAG, "onResponse: "+response );

            try {
                request_jsonObject=new JSONObject(response);
                request_jsonArray=new JSONArray(request_jsonObject.getString("response"));
                for (int i = 0; i < request_jsonArray.length(); i++){
                            request_jsonObject=request_jsonArray.getJSONObject(i);
                            client_request_itemArrayList.add(new client_request_item(
                                    request_jsonObject.getString("cl_id"),
                                    request_jsonObject.getString("cl_index"),
                                    request_jsonObject.getString("cl_write_date"),
                                    request_jsonObject.getString("cl_request_date")



                            ));
                            client_request_adater.notifyDataSetChanged();
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
                smpr.addStringParam("mode","client_request_list");
                smpr.addStringParam("id",session.getId());


                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);


    }


    public void click(){
        list_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),mypage_Activity.class);
                                                startActivity(intent);
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        layout();
        click();
        volley_request();
        client_request_adater.setOnitemClicklistenter(new client_request_Adater.OnitemClicklistener() {
            @Override
            public void onitemClick(View v, int pos) {
                client_request_item dict = client_request_itemArrayList.get(pos);
                Intent intent =new Intent(getApplicationContext(),caregiver_request_detail_Activity.class);
                intent.putExtra("index",client_request_itemArrayList.get(pos).getIndex());
                startActivity(intent);
            }
        });

    }
}