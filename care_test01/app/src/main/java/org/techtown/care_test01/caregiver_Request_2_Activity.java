package org.techtown.care_test01;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.OrientationHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class caregiver_Request_2_Activity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private CalendarView calendarView;
    View.OnClickListener clickListener;
    Button result_bt3;
    private static final String TAG = "Cannot invoke method length() on null object";
    JSONObject request_jsonObject;


    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_request2);
     //   setSupportActionBar((Toolbar) findViewById(R.id.toolbar));


        initViews();
        //그냥 타입 자체를 나누지말고 mult 로 전부 바꾸기

        result_bt3=findViewById(R.id.result_bt3);
        setClickListener();

        String request =session.getSharedPreferences("request",0).getString("request","");
        Log.e(TAG, "onCreate: "+request );
        try {
            request_jsonObject=new JSONObject(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setClickListener() {
       clickListener=new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.O)
           @SuppressLint({"NonConstantResourceId", "LongLogTag"})
           @Override
           public void onClick(View v) {

               switch(v.getId()){
                   case R.id.result_bt3:
                       List<Calendar> days =calendarView.getSelectedDates();
                       StringBuilder result = new StringBuilder();
                       for (int i = 0; i < days.size(); i++){
                           Calendar calendar = days.get(i);
                           final int day = calendar.get(Calendar.DAY_OF_MONTH);
                           final int month = calendar.get(Calendar.MONTH);
                           final int year = calendar.get(Calendar.YEAR);
                           String week= new SimpleDateFormat("EE",Locale.KOREA).format(calendar.getTime());
                           String day_full = (month+1)  + "월" + day + "일";
                           result.append(day_full).append(",");
                       }
                       if(result.toString()==null|| result.toString().equals("")){
                           msg.toast(getApplicationContext(),"간병이 필요하신 날짜를 선택하여 주세요");
                       }else {
                           try {
                               request_jsonObject.put("날짜",result.toString());
                               session.setContext(getApplicationContext());
                               session.getSharedPreferences("request",0);
                               session.setSharedPreferences("request",request_jsonObject.toString());
                                  Intent intent = new Intent(getApplicationContext(),caregiver_Request_3_Activity.class);
                                                               startActivity(intent);

                           } catch (JSONException e) {
                               e.printStackTrace();
                           }

                          // Toast.makeText(caregiver_Request_2_Activity.this, result.toString(), Toast.LENGTH_LONG).show();
                           Log.e(TAG, "onClick: " + result.toString());
                       }
                       break;


               }
           }
       };
       result_bt3.setOnClickListener(clickListener);

    }

    private void initViews(){
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.setCalendarOrientation(OrientationHelper.HORIZONTAL);

        ((RadioGroup)findViewById(R.id.rg_selection_type)).setOnCheckedChangeListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch(item.getItemId()){
           case R.id.clear_selections:
               clearSelectionsMenuClick();
               return  true;

           case R.id.show_selections:
               List<Calendar> days =calendarView.getSelectedDates();
               StringBuilder result = new StringBuilder();
               for (int i = 0; i < days.size(); i++){
                   Calendar calendar = days.get(i);
                   final int day = calendar.get(Calendar.DAY_OF_MONTH);
                   final int month = calendar.get(Calendar.MONTH);
                   final int year = calendar.get(Calendar.YEAR);
                   String week= new SimpleDateFormat("EE",Locale.KOREA).format(calendar.getTime());
                   String day_full = year + "년 "+ (month+1)  + "월 " + day + "일 " + week + "요일";
                   result.append(day_full).append("\n");
               }
               Toast.makeText(caregiver_Request_2_Activity.this, result.toString(), Toast.LENGTH_LONG).show();
                return  true;

         default:
           return  super.onOptionsItemSelected(item);
       }
    }


    private void clearSelectionsMenuClick() {
        calendarView.clearSelections();

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        clearSelectionsMenuClick();

        switch(checkedId){
            case R.id.rb_single:
                calendarView.setSelectionType(SelectionType.SINGLE);
            break;

            case R.id.rb_multiple:
                calendarView.setSelectionType(SelectionType.MULTIPLE);
                break;
//            case R.id.rb_range:
//                calendarView.setSelectionType(SelectionType.RANGE);
//                break;
//
//            case R.id.rb_none:
//                calendarView.setSelectionType(SelectionType.NONE);
//                break;


        }


    }
}