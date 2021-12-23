package org.techtown.care_test01;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.time.LocalDate;
import java.util.ArrayList;

public class cl_use_list_Adapter extends RecyclerView.Adapter<cl_use_list_Adapter.CustomViewHolder>  {


    private static final String TAG = "board_Adapter";
    static ArrayList<use_cs_item> use_arraylist;
    @SuppressLint("StaticFieldLeak")
    static Context bContext;


    public void setArrayList(ArrayList<use_cs_item> list, Context context) {
        this.use_arraylist = list;
        this.bContext = context;
    }

    public interface OnitemClicklistener {
        void onitemClick(View v, int pos);
    }

    private cl_use_list_Adapter.OnitemClicklistener mListener = null;

    public void setOnitemClicklistenter(cl_use_list_Adapter.OnitemClicklistener listener) {
        this.mListener = listener;
    }

    public cl_use_list_Adapter(Context context, ArrayList<use_cs_item> list) {

        this.bContext = context;
        this.use_arraylist = list;
        return;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.use_item, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        bContext = viewGroup.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        viewHolder.use_cs_name.setText(use_arraylist.get(position).getCs_name());
        if("1".equals(use_arraylist.get(position).getCs_review())){
            viewHolder.use_review_bt.setText("리뷰완료");
            viewHolder.use_review_bt.setEnabled(false);
        }else{
            viewHolder.use_review_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "onClick: "+use_arraylist.get(position).getCs_index());
                    Dialog builder = new Dialog (bContext);
                    builder.setContentView(R.layout.review_dialog);
                    final Button result_review_bt =(Button)builder.findViewById(R.id.result_review_bt);
                    final EditText write_review =(EditText)builder.findViewById(R.id.write_review);
                    final RatingBar write_review_rating =(RatingBar)builder.findViewById(R.id.write_review_rating);

                    result_review_bt.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View v) {
                            Log.e(TAG, "onClick: "+write_review.getText().toString() );
                            Log.e(TAG, "onClick: "+write_review_rating.getRating() );

                            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                    Log.e(TAG, "onResponse: "+response );
                                       Intent intent = new Intent(bContext,Care_giver_review_Activity.class);
                                       intent.putExtra("cs_id",use_arraylist.get(position).getCs_id());
                                                                    bContext.startActivity(intent);
                                    }
                                    }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                    }
                                    });
                                    LocalDate todaysDate = LocalDate.now();
                                    smpr.addStringParam("mode","write_review");
                                    smpr.addStringParam("cl_id",session.getId());
                                    smpr.addStringParam("cs_id",use_arraylist.get(position).getCs_id());
                                    smpr.addStringParam("cs_resum",use_arraylist.get(position).getCs_index());
                                    smpr.addStringParam("cs_review",write_review.getText().toString());
                                    smpr.addStringParam("cs_rating",String.valueOf(write_review_rating.getRating()));
                                    smpr.addStringParam("date",todaysDate.toString());

                                    //요청객체를 서버로 보낼 우체통 같은 객체 생성
                                    RequestQueue requestQueue= Volley.newRequestQueue(bContext);
                                    requestQueue.add(smpr);


                            builder.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }


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
        return use_arraylist.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView use_cs_name;
        protected Button use_review_bt;



        public CustomViewHolder (View itemView){
            super(itemView);
            this.use_cs_name = itemView.findViewById(R.id.use_cs_name);
            this.use_review_bt = itemView.findViewById(R.id.use_review_bt);

        }


    }

}
