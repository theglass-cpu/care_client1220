package org.techtown.care_test01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class cl_chat_room_Activity extends AppCompatActivity {
    private EditText chat_msg_edit;
    private TextView chat_room_id,delite_text;
    private Button msg_send_bt,match_delete;
    ImageView back_space;
    String room_number;
    String to_user;
    String renewal_screen = "0";
    String delete="0";
    private Messenger mService;
    private final Messenger mMessenger = new Messenger( new IncomingHandler() );
    private static final String TAG = "Cannot invoke method length() on null object";


    RecyclerView cs_chat_recy;
    chat_msg_Adapter chat_msg_adapter;
    static ArrayList<chat_msg> chat_msgArrayList;

    JSONArray send_jsonArray,receive_jsonArray;
    JSONObject send_jsonObject ,receive_jsonObject;


    private class IncomingHandler extends Handler {
        @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
        @Override
        public void handleMessage( Message msg ){
                receive_jsonObject=new JSONObject();
                receive_jsonArray=new JSONArray();
            // 메시지 값에 따라
            switch ( msg.what ){
                // msg set value
                case Client_Tcp_connetion_server.ReceiveThread.MSG_RESPONSE:
                    Bundle bundle = msg.getData();
                    String response = bundle.getString("response");
                    Log.d(TAG, "보호자 카톡왔음 : "+response);
                    try {
                        receive_jsonArray=new JSONArray(response);
                        for (int i = 0; i < receive_jsonArray.length(); i++){
                                    receive_jsonObject=receive_jsonArray.getJSONObject(i);
                                    if(room_number.equals(receive_jsonObject.getString("room_number"))){
                                        if(receive_jsonObject.getString("delete").equals("1")){
                                            chat_msg_edit.setVisibility(View.GONE);
                                            msg_send_bt.setVisibility(View.GONE);
                                            match_delete.setVisibility(View.GONE);
                                            delite_text.setText("매칭이 취소되었습니다.");
                                        }
                                        Log.e(TAG, "방번호가 같음 간병인에게서  카톡옴" );
                                        chat_msgArrayList.add(new chat_msg(chat_room_id.getText().toString(),receive_jsonObject.getString("msg")));
                                        chat_msg_adapter.notifyDataSetChanged();
                                        cs_chat_recy.scrollToPosition(chat_msg_adapter.getItemCount()-1);
                                    }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    super.handleMessage( msg );
            }
        }
    }

    ServiceConnection conn = new ServiceConnection(){

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mService = new Messenger( service );

            try{


                Message msg = Message.obtain( null, chat_bind_service.MSG_REGISTER_CLIENT );
                msg.replyTo = mMessenger;
                mService.send( msg );

            }
            catch( RemoteException e ){  }
        }
    };


    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    public void layout(){
        room_number=getIntent().getStringExtra("room_index");
        session.now_window=getIntent().getStringExtra("room_index");
        chat_room_id = findViewById(R.id.chat_room_id);
        back_space=findViewById(R.id.back_space);
        chat_msg_edit = findViewById(R.id.chat_msg_edit);
        msg_send_bt = findViewById(R.id.msg_send_bt);
        delite_text=findViewById(R.id.delite_text);
        match_delete= findViewById(R.id.match_delete);
        cs_chat_recy = findViewById(R.id.cs_chat_recy);
        renewal_screen=getIntent().getStringExtra("renewal_screen");
        cs_chat_recy.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chat_msgArrayList=new ArrayList<>();
        chat_msg_adapter=new chat_msg_Adapter(getApplicationContext(),chat_msgArrayList);
        cs_chat_recy.setAdapter(chat_msg_adapter);
        send_jsonArray=new JSONArray();
        delete=getIntent().getStringExtra("delete");
        send_jsonObject=new JSONObject();
        to_user = getIntent().getStringExtra("id");
        chat_room_id.setText(getIntent().getStringExtra("name"));


    }

    public void click(){
        msg_send_bt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                send_jsonArray=new JSONArray();
                send_jsonObject=new JSONObject();
                try {

                    Message msg = Message.obtain( null, chat_bind_service.SEND );
                    Bundle bundle = msg.getData();
                    send_jsonObject.put("msg",chat_msg_edit.getText().toString());
                    send_jsonObject.put("from_user",session.getId());
                    send_jsonObject.put("to_user",to_user);
                    send_jsonObject.put("cs_nickname",chat_room_id.getText().toString());
                    send_jsonObject.put("room_number",room_number);
                    send_jsonObject.put("delete","0");
                    send_jsonArray.put(send_jsonObject);
                    bundle.putString("send",send_jsonArray.toString());
                    msg.replyTo = mMessenger;
                    mService.send( msg );
                    chat_msgArrayList.add(new chat_msg(session.getId(),chat_msg_edit.getText().toString()));
                    cs_chat_recy.setAdapter(chat_msg_adapter);
                    chat_msg_adapter.notifyDataSetChanged();
                    chat_msg_edit.setText("");
                    cs_chat_recy.scrollToPosition(chat_msg_adapter.getItemCount()-1);


                } catch (RemoteException | JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        back_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),match_list_Activity.class);
                   session.now_window="0";
                Message msg = Message.obtain( null, chat_bind_service.MSG_UNREGISTER_CLIENT );
                msg.replyTo = mMessenger;

                try {
                    mService.send( msg );
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        match_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_jsonObject=new JSONObject();
                send_jsonArray=new JSONArray();


                try {
                    send_jsonObject.put("msg","보호자가 매칭을 취소하셨습니다.");
                    send_jsonObject.put("from_user",session.getId());
                    send_jsonObject.put("to_user",to_user);
                    send_jsonObject.put("room_number",room_number);
                    send_jsonObject.put("cs_nickname",chat_room_id.getText().toString());
                    send_jsonObject.put("delete","1");
                    send_jsonArray.put(send_jsonObject);
                    PrintWriter sendWriter = new PrintWriter(Client_Tcp_connetion_server.c_socket.getOutputStream());
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            sendWriter.println(send_jsonArray.toString());
                            sendWriter.flush();
                        }
                    }.start();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                chat_msg_edit.setVisibility(View.GONE);
                msg_send_bt.setVisibility(View.GONE);
                delite_text.setText("매칭이 취소되었습니다.");
                msg.toast(getApplicationContext(),chat_room_id.getText().toString() +"님과 매칭이 취소되었습니다");
                   Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                startActivity(intent);


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ChattingService bind
        bindService(new Intent(this, chat_bind_service.class),
                conn, Context.BIND_AUTO_CREATE);
    }

    public void get_msg_list(){
        if(delete.equals("1")){
            chat_msg_edit.setVisibility(View.GONE);
            msg_send_bt.setVisibility(View.GONE);
            delite_text.setText("매칭이 취소되었습니다.");
        }

        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.resum, new Response.Listener<String>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: "+response );

                receive_jsonArray =new JSONArray();
                receive_jsonObject=new JSONObject();

                try {
                    receive_jsonObject=new JSONObject(response);
                    receive_jsonArray=new JSONArray(receive_jsonObject.getString("response"));
                    for (int i = 0; i < receive_jsonArray.length(); i++){
                        receive_jsonObject=receive_jsonArray.getJSONObject(i);
                        chat_msgArrayList.add(new chat_msg(receive_jsonObject.getString("user")
                                ,receive_jsonObject.getString("msg")));
                        chat_msg_adapter.notifyDataSetChanged();
                        cs_chat_recy.scrollToPosition(chat_msg_adapter.getItemCount()-1);


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

        smpr.addStringParam("mode","msg_list");
        smpr.addStringParam("room_index",room_number);

        //요청객체를 서버로 보낼 우체통 같은 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(smpr);


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cl_chat_room);
        layout();
        click();
        if(renewal_screen.equals("1")){
            get_msg_list();

        }
    }

}
