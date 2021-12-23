package org.techtown.care_test01;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class client_request_Adater extends RecyclerView.Adapter<client_request_Adater.CustomViewHolder> {


    private static final String TAG = "board_Adapter";
    static ArrayList<client_request_item> client_request_itemArrayList;
    @SuppressLint("StaticFieldLeak")
    static Context bContext;

    public void setArrayList(ArrayList<client_request_item> list, Context context) {
        this.client_request_itemArrayList = list;
        this.bContext = context;
    }

    public interface OnitemClicklistener {
        void onitemClick(View v, int pos);
    }

    private client_request_Adater.OnitemClicklistener mListener = null;

    public void setOnitemClicklistenter(client_request_Adater.OnitemClicklistener listener) {
        this.mListener = listener;
    }

    public client_request_Adater(Context context, ArrayList<client_request_item> list) {

        this.bContext = context;
        this.client_request_itemArrayList = list;
        return;
    }




    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.client_request_item, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        bContext = viewGroup.getContext();


        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
            viewHolder.client_request_date.setText("간병 요청일 : "+client_request_itemArrayList.get(position).getRequest_date());
            viewHolder.client_write_date.setText("작성일 : "+client_request_itemArrayList.get(position).getWrite_date());


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = position;
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onitemClick(v, pos);
                        }
                    }

                }
            });

            viewHolder.client_request_deadline_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //마감클릭

                    SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            Log.e(TAG, "onResponse: "+response );
                               Intent intent = new Intent(bContext,mypage_Activity.class);
                                                            bContext.startActivity(intent);
                                                            msg.toast(bContext,"마감 완료");
                            }
                            }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                            }
                            });
                            smpr.addStringParam("mode","request_deadline");
                            smpr.addStringParam("index",client_request_itemArrayList.get(position).getIndex());

                            //요청객체를 서버로 보낼 우체통 같은 객체 생성
                            RequestQueue requestQueue= Volley.newRequestQueue(bContext);
                            requestQueue.add(smpr);

                }
            });

    }

    @Override
    public int getItemCount() {
        return client_request_itemArrayList.size();
    }


    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView client_write_date,client_request_date;
        protected Button client_request_deadline_bt;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.client_request_date=itemView.findViewById(R.id.client_request_date);
            this.client_write_date=itemView.findViewById(R.id.client_write_date);
            this.client_request_deadline_bt=itemView.findViewById(R.id.client_request_deadline_bt);

        }
    }
}
