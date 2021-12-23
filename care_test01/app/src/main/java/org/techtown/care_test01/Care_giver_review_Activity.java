package org.techtown.care_test01;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.util.ArrayList;
import java.util.Objects;

public class Care_giver_review_Activity extends AppCompatActivity {

    String cs_id,str;
    ImageView review_cs_profile,review_back_bt;
    TextView review_cs_nickname ,review_cs_level,review_cs_cf;
    RecyclerView review_recy;
    RatingBar review_info_rating_average;
    review_list_Adapter review_list_adapter;
    static ArrayList<review_item> review_itemArrayList;
    JSONArray review_jsonArray;
    JSONObject review_jsonObject;
    String[] mobNum;
    int page = 1;

    private static final String TAG = "Cannot invoke method length() on null object";

    @SuppressLint("CutPasteId")
    public void layout(){

        cs_id=getIntent().getStringExtra("cs_id");
        review_cs_profile=findViewById(R.id.review_cs_profile);
        review_back_bt=findViewById(R.id.review_back_bt);
        review_cs_nickname=findViewById(R.id.review_cs_nickname);
        review_cs_level=findViewById(R.id.review_cs_level);
        review_cs_cf=findViewById(R.id.review_cs_cf);
        review_recy=findViewById(R.id.review_recy);
        review_recy.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        review_recy.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        review_itemArrayList=new ArrayList<>();
        review_list_adapter= new review_list_Adapter(getApplicationContext(),review_itemArrayList);
        review_recy.setAdapter(review_list_adapter);
        review_jsonArray=new JSONArray();
        review_jsonObject=new JSONObject();
    }

    public void pageing(){
        review_recy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() - 1;
                if(lastVisibleItemPosition == itemTotalCount){
                    Log.d(TAG, "스크롤끝에닿음");
                    page++;
                    Log.e(TAG, "다음페이지: "+page);
                    cs_review_info();


                }


            }
        });

    }

    public void cs_info(){

        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"LongLogTag", "SetTextI18n", "UseCompatLoadingForDrawables"})
        @Override
        public void onResponse(String response) {
                Log.e(TAG, "정보가져옴 : "+response );

            try {
                review_jsonObject=new JSONObject(response);
                review_jsonArray=new JSONArray(review_jsonObject.getString("response"));
                for (int i = 0; i < review_jsonArray.length(); i++){
                        review_jsonObject=review_jsonArray.getJSONObject(i);
                        review_cs_nickname.setText("이름 : "+review_jsonObject.getString("name"));
                        review_cs_level.setText("경력 : "+review_jsonObject.getString("level"));
                            if ("".equals(review_jsonObject.getString("pp")) || null == review_jsonObject.getString("pp")) {
                                review_cs_profile.setImageDrawable(getResources().getDrawable(R.drawable.userss));
                            } else {

                                Glide.with(getApplicationContext()).load(url.ip + review_jsonObject.getString("pp")).into(review_cs_profile);
                            }

                         str = review_jsonObject.getString("cf");
                        String text = str.replace("|","\n");
                        review_cs_cf.setText("자격증 "+""+text );


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
                smpr.addStringParam("id",cs_id);

                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);

    }



    //평가가져온거
    public void cs_review_info(){

        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
        @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
        @Override
        public void onResponse(String response) {
            Log.e(TAG, "평가가져옴: "+response );
            try {
                float sum = 0;
                int count =0;
                float f ;
                review_jsonObject=new JSONObject();
                review_jsonObject=new JSONObject(response);
                review_jsonArray=new JSONArray(review_jsonObject.getString("response"));
                for (int i = 0; i < review_jsonArray.length(); i++){
                            review_jsonObject=review_jsonArray.getJSONObject(i);

                            Log.e(TAG, "평균 "+sum );
                            review_itemArrayList.add(
                                    new review_item(
                                            review_jsonObject.getString("cs_id"),
                                            review_jsonObject.getString("cl_id"),
                                            review_jsonObject.getString("cs_review_index"),
                                            review_jsonObject.getString("cs_rating"),
                                            review_jsonObject.getString("cs_review"),
                                            review_jsonObject.getString("review_data"),
                                            review_jsonObject.getString("review_status")));
                            review_list_adapter.notifyDataSetChanged();
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
                smpr.addStringParam("mode","review_list");
                smpr.addStringParam("cs_id",cs_id);
                smpr.addStringParam("page", String.valueOf(page));


                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);
    }


    public void click(){
        review_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),mypage_Activity .class);
                   startActivity(intent);
                   finish();


            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_giver_review);
        layout();

        cs_info();
        cs_review_info();
        click();
        pageing();

        review_list_adapter.setOnitemClicklistenter(new review_list_Adapter.OnitemClicklistener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onitemClick(View v, int pos) {
                review_item item = review_itemArrayList.get(pos);
                Log.e(TAG, "onitemClick: "+item.getReview_index() );
            }
        });

    }
}