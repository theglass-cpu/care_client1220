package org.techtown.care_test01;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.nio.channels.FileLock;
import java.time.LocalDate;
import java.util.ArrayList;

public class review_list_Adapter extends RecyclerView.Adapter<review_list_Adapter.CustomViewHolder>  {


    private static final String TAG = "board_Adapter";
    static ArrayList<review_item>review_itemArrayList;
    @SuppressLint("StaticFieldLeak")
    static Context bContext;


    public void setArrayList(ArrayList<review_item> list, Context context) {
        this.review_itemArrayList = list;
        this.bContext = context;
    }

    public interface OnitemClicklistener {
        void onitemClick(View v, int pos);
    }

    private review_list_Adapter.OnitemClicklistener mListener = null;

    public void setOnitemClicklistenter(review_list_Adapter.OnitemClicklistener listener) {
        this.mListener = listener;
    }

    public review_list_Adapter(Context context, ArrayList<review_item> list) {

        this.bContext = context;
        this.review_itemArrayList = list;
        return;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_item, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        bContext = viewGroup.getContext();

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {


        String str = review_itemArrayList.get(position).getCl_id();
        String[] mobNum = str.split("@");
        viewHolder.cl_id.setText("보호자 : "+mobNum[0]);

        if(!session.getId().equals(review_itemArrayList.get(position).getCl_id())){
             viewHolder.edit_bt.setVisibility(View.GONE);
        }

        if("N".equals(review_itemArrayList.get(position).getReview_status())){
            viewHolder.cl_review.setText(review_itemArrayList.get(position).getReview_msg());
            viewHolder.cl_data.setText(review_itemArrayList.get(position).getReview_data());
            float f ;
            f = Float.parseFloat(review_itemArrayList.get(position).getReview_rating());
            viewHolder.rating_average.setRating(f);
        }else{

            viewHolder.cl_review.setText("삭제된 데이터입니다.");
            viewHolder.cl_data.setText(review_itemArrayList.get(position).getReview_data());
             viewHolder.rating_average.setRating(0);
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
        return review_itemArrayList.size();
    }

    public  class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        protected TextView cl_id,cl_review,cl_data;
        protected RatingBar rating_average;
        protected ImageView edit_bt;


        public CustomViewHolder (View itemView){
            super(itemView);
            this.cl_id = itemView.findViewById(R.id.cl_id);
            this.cl_review = itemView.findViewById(R.id.cl_review);
            this.rating_average = itemView.findViewById(R.id.rating_average);
            this.cl_data = itemView.findViewById(R.id.cl_data);
            this.edit_bt = itemView.findViewById(R.id.edit_bt);

            itemView.setOnCreateContextMenuListener(this);

        }




        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

             Log.e(TAG, "onCreateContextMenu: "+review_itemArrayList.get(getAdapterPosition()).getCl_id());
                if(review_itemArrayList.get(getAdapterPosition()).getCl_id().equals(session.getId())) {
                    MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정");
                    MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
                    Edit.setOnMenuItemClickListener(onEditMenu);
                    Delete.setOnMenuItemClickListener(onEditMenu);
                }else{
                    Log.e(TAG, "같지않은 아이디라서 추가수정삭제안뜸" );
                }

        }

        private final  MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                LocalDate todaysDate = LocalDate.now();
                switch(item.getItemId()){
                  case 1001 :
                      AlertDialog.Builder builder = new AlertDialog.Builder(bContext);
                      @SuppressLint("InflateParams") View view = LayoutInflater.from(bContext).inflate(R.layout.review_dialog , null,false);
                      builder.setView(view);
                      final RatingBar write_review_rating = (RatingBar) view.findViewById(R.id.write_review_rating);
                      final EditText write_review = (EditText) view.findViewById(R.id.write_review);
                      final Button result_review_bt = (Button) view.findViewById(R.id.result_review_bt);

                      write_review.setText(review_itemArrayList.get(getAdapterPosition()).getReview_msg());

                      final AlertDialog dialog = builder.create();
                      result_review_bt.setOnClickListener(new View.OnClickListener() {
                          @RequiresApi(api = Build.VERSION_CODES.O)
                          @Override
                          public void onClick(View v) {
                            String str = String.valueOf(write_review_rating.getRating());
                            review_item edit = new review_item(
                                    review_itemArrayList.get(getAdapterPosition()).getCs_id(),
                                    review_itemArrayList.get(getAdapterPosition()).getCl_id(),
                                    review_itemArrayList.get(getAdapterPosition()).getReview_index(),
                                    str,
                                    write_review.getText().toString(),
                                    todaysDate.toString(),
                                    "N"
                                    );
                            review_itemArrayList.set(getAdapterPosition(),edit);
                            notifyItemChanged(getAdapterPosition());
                            dialog.dismiss();


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
                                   smpr.addStringParam("mode","review_edit");
                                   smpr.addStringParam("index",review_itemArrayList.get(getAdapterPosition()).getReview_index());
                                   smpr.addStringParam("rating",str);
                                   smpr.addStringParam("msg",write_review.getText().toString());
                                   smpr.addStringParam("date",todaysDate.toString());

                                   //요청객체를 서버로 보낼 우체통 같은 객체 생성
                                   RequestQueue requestQueue= Volley.newRequestQueue(bContext);
                                   requestQueue.add(smpr);

                          }
                      });

                    dialog.show();

                    break;

                  case 1002:
                      review_item edit = new review_item(
                              review_itemArrayList.get(getAdapterPosition()).getCs_id(),
                              review_itemArrayList.get(getAdapterPosition()).getCl_id(),
                              review_itemArrayList.get(getAdapterPosition()).getReview_index(),
                              "0",
                              "삭제된 데이터입니다.",
                              todaysDate.toString(),
                              "N"
                      );


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
                              smpr.addStringParam("mode","review_delite");
                              smpr.addStringParam("index",review_itemArrayList.get(getAdapterPosition()).getReview_index());

                              //요청객체를 서버로 보낼 우체통 같은 객체 생성
                              RequestQueue requestQueue= Volley.newRequestQueue(bContext);
                              requestQueue.add(smpr);
                      review_itemArrayList.set(getAdapterPosition(),edit);
                      notifyItemChanged(getAdapterPosition());


                    break;
                }



                return true;
            }
        };

    }

}
