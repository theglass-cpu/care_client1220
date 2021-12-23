package org.techtown.care_test01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class center_data_list_Activity extends AppCompatActivity {

    RecyclerView center_list_recycler_view;
    center_Adapter center_adapter;


    private static final String TAG = "Cannot invoke method length() on null object";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_data_list);
        center_list_recycler_view=findViewById(R.id.center_list_recycler_view);
        center_list_recycler_view.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        center_list_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        center_adapter = new center_Adapter(getApplicationContext(),center_map_Activity.naver_marker_arrayList);
        center_list_recycler_view.setAdapter(center_adapter);

    }
}