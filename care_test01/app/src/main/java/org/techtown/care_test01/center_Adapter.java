package org.techtown.care_test01;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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

public class center_Adapter extends RecyclerView.Adapter<center_Adapter.CustomViewHolder> {

    private static final String TAG = "board_Adapter";
    static ArrayList<marker> markerArrayList;
    @SuppressLint("StaticFieldLeak")
    static Context bContext;
    static int like=0;
    private static final int IDLE = 0;
    private static final int SIDLE = 0;
    static int  mStatus = IDLE;//처음 상태는 IDLE
    static int  SStatus = SIDLE;//처음 상태는 IDLE
    final static int RUNNING = 1;
    JSONObject like_list_jsonObject;
    JSONArray like_list_jsonArray;


    public void setArrayList(ArrayList<marker> list, Context context) {
        this.markerArrayList = list;
        this.bContext = context;
    }


    public center_Adapter(Context context, ArrayList<marker> list) {

        this.bContext = context;
        this.markerArrayList = list;
        return;
    }

    public interface OnitemClicklistener{
        void onitemClick(View v, int pos);
    }


    private center_Adapter.OnitemClicklistener mListener=null;

    public void setOnitemClicklistenter(center_Adapter.OnitemClicklistener listener)
    {
        this.mListener=listener;
    }


    @NonNull
    @Override
    public center_Adapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.center_item, viewGroup, false);

        center_Adapter.CustomViewHolder viewHolder = new center_Adapter.CustomViewHolder(view);
        bContext = viewGroup.getContext();


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull center_Adapter.CustomViewHolder viewholder, @SuppressLint("RecyclerView") int position) {
        viewholder.name.setText(markerArrayList.get(position).getName());
        viewholder.address.setText(markerArrayList.get(position).getRoadAddress());
        viewholder.tel.setText(markerArrayList.get(position).getTell());
        //여기서 볼리불러오기 클릭한 XY경도값과 저장되어있는 경도값이 같다면 이미 등록이되어있는 버튼 입니다

        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
                Log.e(TAG, "onResponse: "+response );

            try {
                like_list_jsonObject=new JSONObject(response);
                boolean success = like_list_jsonObject.getBoolean("success");

                if(success){
                   viewholder.like_bt.setBackground(ContextCompat.getDrawable(bContext,R.drawable.sff));
                    mStatus=RUNNING;
                    Log.e(TAG, "onResponse: "+markerArrayList.get(position).getName());
                }else{


                    viewholder.like_bt.setBackground(ContextCompat.getDrawable(bContext,R.drawable.sfsfs));
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
                smpr.addStringParam("mode","like_list");
                smpr.addStringParam("id",session.getId());
                smpr.addStringParam("x",String.valueOf(markerArrayList.get(position).getX()));
                smpr.addStringParam("y",String.valueOf(markerArrayList.get(position).getY()));

                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(bContext);
                requestQueue.add(smpr);




    }

    @Override
    public int getItemCount() { return markerArrayList.size(); }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView name;
        protected TextView address;
        protected TextView tel;
        protected ImageView like_bt;




        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);


            this.name=itemView.findViewById(R.id.name);
            this.address=itemView.findViewById(R.id.address);
            this.tel=itemView.findViewById(R.id.tel);
            this.like_bt=itemView.findViewById(R.id.like_bt);

            
            //처음상태는 0 인데 
            
            like_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //여기서 가져와야함
                switch(mStatus){
                    case  IDLE:
                        like_bt.setBackground(ContextCompat.getDrawable(bContext,R.drawable.sff));
                        //러닝이 아닐때 불켜지게만들기
                        mStatus = RUNNING;
                        Log.e(TAG, "불켜짐"+mStatus );
                      
                    break;

                    case RUNNING:
                        //러닝일때 불꺼지게만들고
                        like_bt.setBackground(ContextCompat.getDrawable(bContext,R.drawable.sfsfs));
                        mStatus = IDLE;
                        Log.e(TAG, "꺼짐"+mStatus );
                     
                    break;
                }


                    Log.e(TAG, "즐겨찾기 버튼 눌렀음 누른 센터이름은 ?"+markerArrayList.get(getAdapterPosition()).getName() );
                    //여기서 볼리불러오기 클릭한 XY경도값과 저장되어있는 경도값이 같다면 이미 등록이되어있는 버튼 입니다
                    Log.e(TAG, "onClick: "+markerArrayList.get(getAdapterPosition()).getX());
                    Log.e(TAG, "onClick: "+markerArrayList.get(getAdapterPosition()).getY());
                    //일단여기서 디비에 전송해봅시다
                    SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            Log.e(TAG, "onResponse: "+response );
                            }
                            }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                            }
                            });
                            smpr.addStringParam("mode","like_center");
                            smpr.addStringParam("id",session.getId());
                            smpr.addStringParam("mStatus",String.valueOf(mStatus));
                            smpr.addStringParam("ce_name",markerArrayList.get(getAdapterPosition()).getName());
                            smpr.addStringParam("ce_address",markerArrayList.get(getAdapterPosition()).getRoadAddress());
                            smpr.addStringParam("ce_tel",markerArrayList.get(getAdapterPosition()).getTell());
                            smpr.addStringParam("x",String.valueOf(markerArrayList.get(getAdapterPosition()).getX()));
                            smpr.addStringParam("y",String.valueOf(markerArrayList.get(getAdapterPosition()).getY()));

                            //요청객체를 서버로 보낼 우체통 같은 객체 생성
                            RequestQueue requestQueue= Volley.newRequestQueue(bContext);
                            requestQueue.add(smpr);


                }
            });
        }
    }

}
