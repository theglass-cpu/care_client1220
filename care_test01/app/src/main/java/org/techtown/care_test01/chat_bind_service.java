package org.techtown.care_test01;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class chat_bind_service extends Service {
    static ArrayList<Messenger> clientList = new ArrayList<>();
    //messenger 객체 를 저장하는 변수 서비스에 바인딩하는 activty 등의 객체들이 전달해주는것
    //서비스가 어떤 작업을 수행하고 그 결과 값을 전달해줄 때 이 Messenger 객체를 통해서 이벤트를 전달할 수 있습니다.
    private int count = 0;
    private static final String TAG = "Cannot invoke method length() on null object";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    static final int MSG_REGISTER_CLIENT = 44;
    //클라이언트 추가를 로그인할대 해야하나?
    public static final int MSG_RESPONSE = 12;

    static final int MSG_UNREGISTER_CLIENT = 56;    // 클라이언트 제거

    static final int SEND = 11;
    public String send;


    class IncomingHandler extends Handler {
        @SuppressLint("LongLogTag")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    clientList.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:         // 클라이언트 제거
                    clientList.remove(msg.replyTo);
                    break;
                case SEND:
                    Bundle bundle = msg.getData();
                    send = bundle.getString("send");
                    Log.e(TAG, "handleMessage: "+send );

                    try {
                        server_start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //receive(send);

                    break;


                default:
                    super.handleMessage(msg);
            }


        }
    }

    public void server_start() throws IOException {
        //서버에다가 메세지보내기
        PrintWriter sendWriter = new PrintWriter(Client_Tcp_connetion_server.c_socket.getOutputStream());
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                Log.e(TAG, "run: "+send );
                sendWriter.println(send);
                sendWriter.flush();
            }
        }.start();
    }


    public static void recive(String msg) {
        new Thread(new Runnable() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                if(msg!=null){
                    Log.e(TAG, "bindsertviec_recive: care_client " );


                    for (int i = 0; i < clientList.size(); i++){

                        try {
                            Message message = Message.obtain(null, MSG_RESPONSE);
                            Bundle bundle = message.getData();
                            bundle.putString("response", msg);

                            clientList.get(i).send(message);
                        } catch (RemoteException  e) {
                            clientList.remove(i);
                            e.printStackTrace();
                        }
                    }

                }


            }
        }).start();



    }



    private Messenger messenger = new Messenger(new IncomingHandler());

    public chat_bind_service() {
    }

    @SuppressLint("LongLogTag")
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: "+messenger.getBinder() );

        return messenger.getBinder();
    }



}