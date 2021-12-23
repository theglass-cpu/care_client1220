package org.techtown.care_test01;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class request_list_Adapter extends RecyclerView.Adapter<request_list_Adapter.CustomViewHolder> {


    private static final String TAG = "board_Adapter";
    static ArrayList<request_item> request_itemArrayList;
    static Context bContext;


    public void setArrayList(ArrayList<request_item> list, Context context) {
        this.request_itemArrayList = list;
        this.bContext = context;
    }


    public request_list_Adapter(Context context, ArrayList<request_item> list) {

        this.bContext = context;
        this.request_itemArrayList = list;
        return;
    }

    public interface OnitemClicklistener {
        void onitemClick(View v, int pos);
    }


    private OnitemClicklistener mListener = null;

    public void setOnitemClicklistenter(OnitemClicklistener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.request_list_item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        bContext = viewGroup.getContext();


        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CustomViewHolder viewholder, @SuppressLint("RecyclerView") int position) {

        viewholder.cs_id.setText(request_itemArrayList.get(position).getCs_name()+" 님이 간병인 지원하셨습니다");

//        if("Y".equals(request_itemArrayList.get(position).getdeadline())){
//            viewholder.close_bt.setBackgroundColor(Color.parseColor("#bcbcbc"));
//            viewholder.close_bt.setText("마감완료");
//        }else{
//            Log.e(TAG, "마감안됨" );
//        }

        viewholder.request_detail_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bContext,caregiver_request_detail_Activity.class);
                intent.putExtra("index",request_itemArrayList.get(position).getRequest_index());
                bContext.startActivity(intent);
            }
        });

        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
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

    }

    @Override
    public int getItemCount() {
        return request_itemArrayList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView cs_id;
        protected Button request_detail_bt;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.cs_id = itemView.findViewById(R.id.cs_id);

          //  this.close_bt = itemView.findViewById(R.id.close_bt);
            this.request_detail_bt = itemView.findViewById(R.id.request_detail_bt);




        }
    }


}
