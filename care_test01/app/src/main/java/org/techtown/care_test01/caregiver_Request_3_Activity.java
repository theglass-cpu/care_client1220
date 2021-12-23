package org.techtown.care_test01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class caregiver_Request_3_Activity extends AppCompatActivity {

    TextView set_time_text, spinner_text;
    Button set_time_bt, result_bt4;
    CheckBox time_extension;
    RadioGroup care_place;
    RadioButton place_home, place_hospital;
    Spinner care_level_spinner;
    String[] items = {"1등급", "2등급", "3등급", "4등급", "5등급", "인지지원등급", "받지않음"};
    private static final String TAG = "Cannot invoke method length() on null object";
    TimePicker timepicker;
    String strTime;
    JSONObject request_jsonObject;


    @SuppressLint({"WrongViewCast", "LongLogTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_request3);

        String request =session.getSharedPreferences("request",0).getString("request","");
        Log.e(TAG, "onCreate: "+request );
        try {
            request_jsonObject=new JSONObject(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //set_time_text=findViewById(R.id.set_time_text);
        spinner_text = findViewById(R.id.spinner_text);
        // set_time_bt=findViewById(R.id.set_time_bt);
        result_bt4 = findViewById(R.id.result_bt4);
        time_extension = findViewById(R.id.time_extension);
        care_place = findViewById(R.id.care_place);
        place_home = findViewById(R.id.place_home);
        place_hospital = findViewById(R.id.place_hospital);
        care_level_spinner = findViewById(R.id.care_level_spinner);
        timepicker = findViewById(R.id.timepicker);

        care_place.check(place_home.getId());
        try {
            request_jsonObject.put("place","0");
            request_jsonObject.put("timeout","N");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        timepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                strTime = hourOfDay + ":" + minute;
              //  Toast.makeText(caregiver_Request_3_Activity.this, strTime, Toast.LENGTH_SHORT).show();
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items
        );
        care_level_spinner.setAdapter(adapter);
        care_level_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_text.setText(items[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_text.setText("받지않음");
            }
        });

        care_place.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint({"NonConstantResourceId", "LongLogTag"})
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.place_home:
                        Log.e(TAG, "집에서간병");
                        try {
                            request_jsonObject.put("place","0");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.place_hospital:
                        Log.e(TAG, "병원에서간병");
                        try {
                            request_jsonObject.put("place","1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

        time_extension.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    Log.e(TAG, "연장가능성있음 ");
                    try {
                        request_jsonObject.put("timeout","Y");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "연장가능서없음");
                    try {
                        request_jsonObject.put("timeout","N");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        result_bt4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {

                if(strTime==null|| strTime.equals("")){
                  msg.toast(getApplicationContext(),"간병시작시간을 알려주세요");
                }else {
                    Log.e(TAG, "완료" + strTime + "||" + spinner_text.getText().toString());
                    try {
                        request_jsonObject.put("timestart", strTime);
                        request_jsonObject.put("care_level", spinner_text.getText().toString());
                        Log.e(TAG, "onClick: " + request_jsonObject.toString());
                        session.setContext(getApplicationContext());
                        session.getSharedPreferences("request",0);
                        session.setSharedPreferences("request",request_jsonObject.toString());
                           Intent intent = new Intent(getApplicationContext(),caregiver_Request_4_Activity.class);
                                                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}