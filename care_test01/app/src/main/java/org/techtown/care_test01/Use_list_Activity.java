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

public class Use_list_Activity extends AppCompatActivity {

    RecyclerView cl_use_recy;
    cl_use_list_Adapter cl_use_list_adapter;
    static ArrayList<use_cs_item> use_cs_itemArrayList;
    JSONObject php_jsonObject;
    JSONArray php_jsonArray;
    ImageView use_list_back_bt;


    private static final String TAG = "Cannot invoke method length() on null object";

    public void layout(){
        cl_use_recy=findViewById(R.id.cl_use_recy);
        use_list_back_bt=findViewById(R.id.use_list_back_bt);
        cl_use_recy.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        cl_use_recy.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        use_cs_itemArrayList=new ArrayList<>();
        cl_use_list_adapter=new cl_use_list_Adapter(getApplicationContext(),use_cs_itemArrayList);
        cl_use_recy.setAdapter(cl_use_list_adapter);
        php_jsonArray=new JSONArray();
        php_jsonObject=new JSONObject();
    }

    public void recydata(){

        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
        @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
        @Override
        public void onResponse(String response) {
                Log.e(TAG, "onResponse: "+response );
            try {
                php_jsonObject=new JSONObject(response);
                php_jsonArray=new JSONArray(php_jsonObject.getString("response"));
                for (int i = 0; i < php_jsonArray.length(); i++){
                    php_jsonObject=php_jsonArray.getJSONObject(i);
                    use_cs_itemArrayList.add(new use_cs_item(
                            php_jsonObject.getString("cs_id"),
                            php_jsonObject.getString("cs_resum_index"),
                            php_jsonObject.getString("cs_review"),
                            php_jsonObject.getString("cs_name")
                    ));
                    cl_use_list_adapter.notifyDataSetChanged();

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
                smpr.addStringParam("mode","cl_use_list");
                smpr.addStringParam("id",session.getId());


                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_list);
        layout();
        recydata();

        cl_use_list_adapter.setOnitemClicklistenter(new cl_use_list_Adapter.OnitemClicklistener() {
            @Override
            public void onitemClick(View v, int pos) {
                    use_cs_item item = use_cs_itemArrayList.get(pos);
                       Intent intent = new Intent(getApplicationContext(),caregiver_info_Activity.class);
                        intent.putExtra("cs_id",item.getCs_id());
                        intent.putExtra("cl_index",item.getCs_index());
                        intent.putExtra("cs_name",item.getCs_name());
                        intent.putExtra("layoutrespone","1");
                                                    startActivity(intent);
            }
        });

        use_list_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),mypage_Activity.class);
                                                startActivity(intent);
            }
        });

    }
}