package org.techtown.care_test01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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

public class caregiver_request_detail_Activity extends AppCompatActivity {

    TextView d_write_date , d_type ,
            d_date,d_time_start,d_time_out,d_care_level
            ,d_place,d_sex,d_age,d_wash,d_meal,d_move,d_remark,d_adress;
    private static final String TAG = "Cannot invoke method length() on null object";
    JSONObject request_jsonObject;
    JSONArray request_jsonArray;
    String remark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_request_detail);

        d_write_date=findViewById(R.id.d_write_date);
        d_type=findViewById(R.id.d_type);
        d_date=findViewById(R.id.d_date);
        d_time_start=findViewById(R.id.d_time_start);
        d_time_out=findViewById(R.id.d_time_out);
        d_care_level=findViewById(R.id.d_care_level);
        d_place=findViewById(R.id.d_place);
        d_sex=findViewById(R.id.d_sex);
        d_age=findViewById(R.id.d_age);
        d_wash=findViewById(R.id.d_wash);
        d_meal=findViewById(R.id.d_meal);
        d_move=findViewById(R.id.d_move);
        d_remark=findViewById(R.id.d_remark);
        d_adress=findViewById(R.id.d_adress);

        request_jsonObject=new JSONObject();
        request_jsonArray=new JSONArray();



        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
        @SuppressLint({"LongLogTag", "SetTextI18n"})
        @Override
        public void onResponse(String response) {
                Log.e(TAG, "onResponse: "+response );
            try {
                request_jsonObject=new JSONObject(response);
                request_jsonArray=new JSONArray(request_jsonObject.getString("response"));
                for (int i = 0; i < request_jsonArray.length(); i++){
                            request_jsonObject=request_jsonArray.getJSONObject(i);

                            //?????????
                            d_write_date.setText(request_jsonObject.getString("writedate"));

                            //??????
                            if("0".equals(request_jsonObject.getString("type"))){
                                d_type.setText("????????? ??????");
                            }else{
                                d_type.setText("24?????? ??????");
                            }
                            
                            //????????????

                            d_date .setText(request_jsonObject.getString("date"));

                            //????????????
                            d_time_start.setText(request_jsonObject.getString("timestart") + "???");

                            //???????????????
                            if("N".equals(request_jsonObject.getString("timeout"))){
                                d_time_out.setText("??????");
                                
                            }else{
                                d_time_out.setText("??????");
                            }
                            //????????????
                            if("0".equals(request_jsonObject.getString("place"))){
                                d_place.setText("???");
                            }else{
                                d_place.setText("??????");
                            }
                            //????????????
                            d_care_level.setText(request_jsonObject.getString("level"));
                            //??????
                            d_adress.setText(request_jsonObject.getString("adress"));

                            //??????
                            if("1".equals(request_jsonObject.getString("sex"))){
                                d_sex.setText("???");
                            }else{
                                d_sex.setText("???");
                            }

                            //??????
                            if("0".equals(request_jsonObject.getString("wash"))){
                                d_wash.setText("???????????????");
                            }else{
                                d_wash.setText("?????????????????????");
                            }
                            //??????
                            if("0".equals(request_jsonObject.getString("meal"))){
                                d_wash.setText("???????????????");
                            }else{
                                d_wash.setText("?????????????????????");
                            }
                            //??????
                            if("0".equals(request_jsonObject.getString("move"))){
                                d_wash.setText("???????????????");
                            }else{
                                d_wash.setText("?????????????????????");
                            }

                            if(request_jsonObject.getString("remark").equals("")||
                                    request_jsonObject.getString("remark")==null){
                                          d_remark.setText("??????");
                            }else{
                                d_remark.setText(request_jsonObject.getString("remark"));
                            }


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
                smpr.addStringParam("mode","request_detail");
                smpr.addStringParam("id",session.getId());
                smpr.addStringParam("index",getIntent().getStringExtra("index"));

                //??????????????? ????????? ?????? ????????? ?????? ?????? ??????
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);









    }
}