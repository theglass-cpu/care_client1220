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

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

public class match_list_Adapter extends RecyclerView.Adapter<match_list_Adapter.CustomViewHolder>  {


    private static final String TAG = "board_Adapter";
    static ArrayList<match_item> match_itemArrayList;
    @SuppressLint("StaticFieldLeak")
    static Context bContext;


    public void setArrayList(ArrayList<match_item> list, Context context) {
        this.match_itemArrayList = list;
        this.bContext = context;
    }

    public interface OnitemClicklistener {
        void onitemClick(View v, int pos);
    }

    private match_list_Adapter.OnitemClicklistener mListener = null;

    public void setOnitemClicklistenter(match_list_Adapter.OnitemClicklistener listener) {
        this.mListener = listener;
    }

    public match_list_Adapter(Context context, ArrayList<match_item> list) {

        this.bContext = context;
        this.match_itemArrayList = list;
        return;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.match_item, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        bContext = viewGroup.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        viewHolder.match_list_cs_id.setText(match_itemArrayList.get(position).getCs_name());
        viewHolder.match_list_request_detail_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "request_detail_bt onClick: "+match_itemArrayList.get(position).getCl_request_index() );
                   Intent intent = new Intent(bContext,caregiver_request_detail_Activity.class);
                intent.putExtra("index",match_itemArrayList.get(position).getCl_request_index());
                                                bContext.startActivity(intent);

            }



        });

        viewHolder.match_list_cs_detail_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "cs_detail_bt onClick: "+match_itemArrayList.get(position).getCs_resum_index() );
                Log.e(TAG, "onClick: "+match_itemArrayList.get(position).getCs_id() );
                   Intent intent = new Intent(bContext,caregiver_info_Activity.class);
                   intent.putExtra("cs_id",match_itemArrayList.get(position).getCs_id());
                intent.putExtra("layoutrespone","1");
                                              bContext.startActivity(intent);

            }
        });

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

    }


    @Override
    public int getItemCount() {
        return match_itemArrayList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView match_list_cs_id;
        protected Button match_list_request_detail_bt;
        protected Button match_list_cs_detail_bt;


        public CustomViewHolder (View itemView){
            super(itemView);
            this.match_list_cs_id = itemView.findViewById(R.id.match_list_cs_id);
            this.match_list_request_detail_bt = itemView.findViewById(R.id.match_list_request_detail_bt);
            this.match_list_cs_detail_bt = itemView.findViewById(R.id.match_list_cs_detail_bt);
        }


    }

}
