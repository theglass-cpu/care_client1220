package org.techtown.care_test01;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.care_test01.session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client_Tcp_connetion_server extends Service {

    private static final String TAG = "Cannot invoke method length() on null object";
    String id;
    PrintWriter sendWriter;
    static Socket c_socket;


//jar 파일 실행명령어 java -jar test.jar

    //메세지 받음


    public class ReceiveThread extends Thread{
        public static final int MSG_RESPONSE = 12;
        private Socket c_socket;
        private String recive_msg;
        JSONArray jsonArray;
        JSONObject jsonObject;
        Context mContext;
        String from_usr;
        private int count = 0;

        public static final String NOTIFICATION_CHANNEL_ID = "10001";

        private static final String TAG = "Cannot invoke method length() on null object";


        @SuppressLint("LongLogTag")
        @Override
        public void run() {
            super.run();
            try {
                BufferedReader tmpbuf = new BufferedReader(new InputStreamReader(c_socket.getInputStream()));
                jsonObject = new JSONObject();
                jsonArray =new JSONArray();

                while(true){

                    recive_msg = tmpbuf.readLine();
                    //여기서 채팅방으로 보내줄거야
                    Log.e(TAG, "r!?!??!?! "+recive_msg );
                    if(recive_msg!=null){
                        chat_bind_service.recive(recive_msg);
                        jsonArray=new JSONArray(recive_msg);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.getString("delete").equals("3")) {

                                    if(jsonObject.getString("match").equals("0")){
                                        Log.e(TAG, "(보호자코드 )간병인 이 보호자에게 수락신청" );
                                        math_noti();
                                    }else{
                                        Log.e(TAG, "(보호자코드 )간병인 이 보호자에게 거부신청" );

                                    }
                            }  else {

                                from_usr = jsonObject.getString("from_user");
                                String[] array = from_usr.split("@");

                                if (session.getNow_window().equals(jsonObject.getString("room_number"))) {
                                    Log.e(TAG, "현재방에있음");
                                } else {
                                    Log.e(TAG, "현재방에없음");
                                    Log.e(TAG, "현재방에없음 " + jsonObject.getString("room_number") + "||" + count++);
                                    NotificationSomethings("케어팜알림",
                                            jsonObject.getString("from_user"),
                                            jsonObject.getString("cs_nickname"),
                                            jsonObject.getString("room_number"),
                                            jsonObject.getString("delete"));
                                }

                                //  NotificationSomethings("케어팜알림",jsonObject.getString("from_user"),jsonObject.getString("cs_nickname"),jsonObject.getString("room_number"));
                            }
                        }

                    }

                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


        }



        public ReceiveThread(Context mcontext) {
            this.mContext = mcontext;
        }

        @SuppressLint("LongLogTag")
        public void NotificationSomethings(String title , String usr , String name, String room_number , String delete) {

                // 채널을 생성 및 전달해 줄수 있는 NotificationManager를 생성한다.
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // 이동하려는 액티비티를 작성해준다.
                Intent notificationIntent = new Intent(mContext, cl_chat_room_Activity.class);
                // 노티를 눌러서 이동시 전달할 값을 담는다. // 전달할 값을 notificationIntent에 담습니다.
                notificationIntent.putExtra("room_index", room_number);
                notificationIntent.putExtra("id", usr);
                notificationIntent.putExtra("name", name);
                notificationIntent.putExtra("delete", delete);
                notificationIntent.putExtra("renewal_screen", "1");
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if(delete.equals("1")){

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mess)) //BitMap 이미지 요구
                            .setContentTitle(title)
                            .setContentText(name + "에게서 매칭취소  ")

                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
                            .setAutoCancel(true); // 눌러야 꺼지는 설정

                    //OREO API 26 이상에서는 채널 필요
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        builder.setSmallIcon(R.drawable.mess); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                        CharSequence channelName = "노티페케이션 채널";
                        String description = "오레오 이상";
                        int importance = NotificationManager.IMPORTANCE_HIGH;// 우선순위 설정

                        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                        channel.setDescription(description);

                        // 노티피케이션 채널을 시스템에 등록
                        assert notificationManager != null;
                        notificationManager.createNotificationChannel(channel);

                    } else
                        builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

                    assert notificationManager != null;
                    notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작
                }else if("0".equals(delete)) {
                    Log.e(TAG, "취소아님!!?!?!?!?" );
                    Log.e(TAG, "취소아님!!?!?!?!?"+room_number );
                    Log.e(TAG, "취소아님!!?!?!?!?" +usr);
                    Log.e(TAG, "취소아님!!?!?!?!?" +name);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mess)) //BitMap 이미지 요구
                            .setContentTitle(title)
                            .setContentText(name + "에게서 채팅도착 ")

                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
                            .setAutoCancel(true); // 눌러야 꺼지는 설정

                    //OREO API 26 이상에서는 채널 필요
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                        CharSequence channelName = "노티페케이션 채널";
                        String description = "오레오 이상";
                        int importance = NotificationManager.IMPORTANCE_HIGH;// 우선순위 설정

                        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                        channel.setDescription(description);

                        // 노티피케이션 채널을 시스템에 등록
                        assert notificationManager != null;
                        notificationManager.createNotificationChannel(channel);

                    } else
                        builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

                    assert notificationManager != null;
                    notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작

                }else if ("3".equals(delete)){
                    Log.e(TAG, "{보호자코드 }간병인의 매칭 수락" );
                }
        }

        public void math_noti() {

            // 채널을 생성 및 전달해 줄수 있는 NotificationManager를 생성한다.
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            // 이동하려는 액티비티를 작성해준다.
            Intent notificationIntent = new Intent(mContext, caregiver_request_list_Activity.class);
            // 노티를 눌러서 이동시 전달할 값을 담는다. // 전달할 값을 notificationIntent에 담습니다.
            notificationIntent.putExtra("notificationId", count);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mess)) //BitMap 이미지 요구
                    .setContentTitle("케어팜알림 ")
                    .setContentText("간병인 을 지원하신분이 있어요 !")

                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
                    .setAutoCancel(true); // 눌러야 꺼지는 설정

            //OREO API 26 이상에서는 채널 필요
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                CharSequence channelName  = "노티페케이션 채널";
                String description = "오레오 이상";
                int importance = NotificationManager.IMPORTANCE_HIGH;// 우선순위 설정

                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName , importance);
                channel.setDescription(description);

                // 노티피케이션 채널을 시스템에 등록
                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);

            }else builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

            assert notificationManager != null;
            notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작

        }

        public Socket getC_socket() {
            return c_socket;
        }

        public void setC_socket(Socket c_socket) {
            this.c_socket = c_socket;
        }

        public String getRecive_msg() {
            return recive_msg;
        }

        public void setRecive_msg(String recive_msg) {
            this.recive_msg = recive_msg;
        }
    }


    public void Tcp_connetion() {
        new Thread(){
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    Log.d(TAG, "소켓에 접속합니다." );
                    //서버소켓
                      c_socket  =new Socket("3.37.212.160",8888);

                    //로컬소켓
                   // c_socket = new Socket("192.168.163.1",8080);
                    PrintWriter printWriter = new PrintWriter(c_socket.getOutputStream());
                    printWriter.println(session.getId());
                    printWriter.flush();

                    ReceiveThread receiveThread = new ReceiveThread(getApplicationContext());
                    receiveThread.setC_socket(c_socket);
                    receiveThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //  c_socket = new Socket("192.168.137.1",8080);

            }
        }.start();

    }






    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("LongLogTag")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "백그라운드 서비스가 시작됩니다." );
        id=session.getId();

        Tcp_connetion();
        return super.onStartCommand(intent, flags, startId);
    }
}