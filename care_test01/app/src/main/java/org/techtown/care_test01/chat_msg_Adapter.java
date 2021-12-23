package org.techtown.care_test01;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class chat_msg_Adapter extends RecyclerView.Adapter<chat_msg_Adapter.ViewHolder> {
    private static final String TAG = "Cannot invoke method length() on null object";
    static ArrayList<chat_msg> chat_msgArrayList;
    public Context bcontext;

    public void setArrayList(ArrayList<chat_msg> list, Context context) {
        this.chat_msgArrayList = list;
        this.bcontext = context;
    }

    public chat_msg_Adapter(Context context, ArrayList<chat_msg> list) {

        this.bcontext = context;
        this.chat_msgArrayList = list;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_msg_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        bcontext = viewGroup.getContext();

        return viewHolder;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {


        if(session.getId().equals(chat_msgArrayList.get(position).getUser())){
                    viewHolder.chat_user.setVisibility(View.GONE);
                    viewHolder.chat_msg.setText(chat_msgArrayList.get(position).getMsg());
                    viewHolder.msgLinear.setGravity(Gravity.RIGHT);
        }else{
            //남이보낸거


            viewHolder.chat_msg.setText(chat_msgArrayList.get(position).getMsg());
            viewHolder.msgLinear.setGravity(Gravity.LEFT);
        }

    }

    @Override
    public int getItemCount() {
        return chat_msgArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chat_msgArrayList.get(position).getViewType();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        protected TextView chat_user;
        protected TextView chat_msg;
        public LinearLayout msgLinear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.chat_user = itemView.findViewById(R.id.chat_user);
            this.chat_msg = itemView.findViewById(R.id.chat_msg);
            this.msgLinear = itemView.findViewById(R.id.msgLinear);

        }
    }

}
