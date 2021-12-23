package org.techtown.care_test01;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class caregiver_Request_4_Activity extends AppCompatActivity {

    private static final String TAG = "Cannot invoke method length() on null object";
    JSONObject request_jsonObject;
    ImageView find_address;
    TextView address_text;
    RadioGroup request_radiogroup;
    RadioButton radio_bt1, radio_bt2;
    EditText age_edit;
    Button result_bt5;
    View.OnClickListener clickListener;
    private int addresscode = 100;
    String adress;
    String[] mobNum;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_request4);

        String request = session.getSharedPreferences("request", 0).getString("request", "");
        Log.e(TAG, "onCreate: " + request);
        try {
            request_jsonObject = new JSONObject(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        find_address = findViewById(R.id.find_address);
        address_text = findViewById(R.id.address_text);
        request_radiogroup = findViewById(R.id.request_radiogroup);
        radio_bt1 = findViewById(R.id.radio_bt1);
        radio_bt2 = findViewById(R.id.radio_bt2);
        age_edit = findViewById(R.id.age_edit);
        result_bt5 = findViewById(R.id.result_bt5);

        request_radiogroup.check(radio_bt1.getId());

        try {
            request_jsonObject.put("sex", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setClickListener();


        request_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_bt1:
                        try {
                            request_jsonObject.put("sex", "1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.radio_bt2:
                        try {
                            request_jsonObject.put("sex", "2");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }


    public void setClickListener() {
        clickListener = new View.OnClickListener() {
            @SuppressLint({"NonConstantResourceId", "LongLogTag"})
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.find_address:

                        //주소검색
                        Log.e(TAG, "지역검색버튼 누름" );
                        Log.e(TAG, "지역검색버튼 누름" );

                        Log.e(TAG, "주소 검색벝늨 클릭" );
                        int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                            Log.i("주소설정페이지", "주소입력창 클릭");
                            Intent i = new Intent(getApplicationContext(), AddressApiActivity.class);
                            // 화면전환 애니메이션 없애기
                            overridePendingTransition(0, 0);

                            // 주소결과
                            resultLauncher.launch(i);

                        } else {
                            Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case R.id.result_bt5:
                        //다음 버튼 클릭
//                        if (age_edit.getText().toString().equals("") || age_edit.getText().toString() == null) {
//                            //입력안함
//                        } else {
//
//                            try {
//                                request_jsonObject.put("age", age_edit.getText().toString());
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            Log.e(TAG, "onClick: ");
//                        }

                        if(address_text.getText().toString().equals("")||address_text.getText().toString()==null){
                            msg.toast(getApplicationContext(),"지역을 입력하여주세요");
                        }else{
                            if(age_edit.getText().toString().equals("")||age_edit.getText().toString()==null){
                                msg.toast(getApplicationContext(),"출생년도를 입력하여주세요");
                            }else{
                                Log.e(TAG, "전부작성완료 " );
                                try {
                                    request_jsonObject.put("age", age_edit.getText().toString().trim());
                                    request_jsonObject.put("adress", address_text.getText().toString());
                                    session.setContext(getApplicationContext());
                                    session.getSharedPreferences("request",0);
                                    session.setSharedPreferences("request",request_jsonObject.toString());
                                    Log.e(TAG, "onClick: "+request_jsonObject.toString() );
                                       Intent intent = new Intent(getApplicationContext(),caregiver_Request_5_Activity.class);
                                                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                        break;

                }
            }
        };

        find_address.setOnClickListener(clickListener);
        result_bt5.setOnClickListener(clickListener);
    }
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() ==addresscode) {
                        assert result.getData() != null;
                        Bundle extras = result.getData().getExtras();
                        Log.e(TAG, "onActivityResult: " +extras.getString("data"));
                        mobNum =extras.getString("data").split("\\|");
                        adress = mobNum[1];
                        address_text.setText(adress);


                    }
                }
            });
}